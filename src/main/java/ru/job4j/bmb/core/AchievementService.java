package ru.job4j.bmb.core;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.SentContent;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodLogRepository;
import java.util.List;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {
    private final MoodLogRepository moodLogRepository;
    private final AwardRepository awardRepository;
    private final SentContent sender;

    public AchievementService(MoodLogRepository moodLogRepository, AwardRepository awardRepository, @Qualifier("realTelegramBot") SentContent sender) {
        this.moodLogRepository = moodLogRepository;
        this.awardRepository = awardRepository;
        this.sender = sender;
    }

    @PostConstruct
    public void init() {
        System.out.println("AchievementService инициализирован");
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserEvent event) {
        var user = event.getUser();
        List<MoodLog> logs = moodLogRepository.findByUserId(user.getId());

        long goodMoodCount = logs.stream()
                .filter(log -> log.getMood().isGood())
                .count();
        List<Award> allAwards = awardRepository.findAll();
        for (Award award : allAwards) {
            if (goodMoodCount == award.getDays()) {
                sender.sent(new Content(user.getChatId(), "\uD83C\uDFC6 Достижение разблокировано:" + award.getTitle() + "\n" + award.getDescription()));

            }
        }
    }

    @PreDestroy
    public void destroy() {
        System.out.println("AchievementService завершает работу");
    }
}
