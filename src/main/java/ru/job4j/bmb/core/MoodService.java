package ru.job4j.bmb.core;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.engine.RecommendationEngine;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import ru.job4j.bmb.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoodService {

    private final MoodLogRepository moodLogRepository;
    private final RecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    @PostConstruct
    public void init() {
        System.out.println("MoodService инициализирован");
    }

    public MoodService(MoodLogRepository moodLogRepository,
                       RecommendationEngine recommendationEngine,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository) {
        this.moodLogRepository = moodLogRepository;
        this.recommendationEngine = recommendationEngine;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
    }

    public Content chooseMood(User user, Long moodId) {
        MoodLog log = new MoodLog();
        log.setUser(user);
        log.setId(moodId);
        log.setCreatedAt(Instant.now().getEpochSecond());
        moodLogRepository.save(log);
        return recommendationEngine.recommendFor(user.getChatId(), moodId);
    }

    public Optional<Content> weekMoodLogCommand(long chatId, Long clientId) {
        var logs = moodLogRepository.findById(clientId).stream()
                .filter(log -> Instant.ofEpochSecond(log.getCreatedAt())
                        .isAfter(Instant.now().minus(7, ChronoUnit.DAYS)))
                .toList();

        String message = formatMoodLogs(logs, "Ваше настроение за неделю");
        return Optional.of(new Content(chatId, message));
    }

    public Optional<Content> monthMoodLogCommand(long chatId, Long clientId) {
        var logs = moodLogRepository.findById(clientId).stream()
                .filter(log -> Instant.ofEpochSecond(log.getCreatedAt())
                        .isAfter(Instant.now().minus(30, ChronoUnit.DAYS)))
                .toList();

        String message = formatMoodLogs(logs, "Ваше настроение за месяц");
        return Optional.of(new Content(chatId, message));
    }

    public Optional<Content> awards(long chatId, Long clientId) {
        Optional<Achievement> achievements = achievementRepository.findById(clientId);
        String message = achievements.isEmpty()
                ? "Пока нет наград. Всё впереди!"
                : "Ваши награды:\n" + achievements.stream()
                .map(a -> "- " + a.getAward())
                .collect(Collectors.joining("\n"));
        return Optional.of(new Content(chatId, message));
    }

    private String formatMoodLogs(List<MoodLog> logs, String title) {
        if (logs.isEmpty()) {
            return title + ":\nЗаписей не найдено.";
        }
        var sb = new StringBuilder(title + ":\n");
        logs.forEach(log -> {
            String formattedDate = formatter.format(Instant.ofEpochSecond(log.getCreatedAt()));
            sb.append(formattedDate)
                    .append(": ")
                    .append(log.getMood().getText())
                    .append("\n");
        });
        return sb.toString();
    }

    @PreDestroy
    public void destroy() {
        System.out.println("MoodService завершает работу");
    }
}
