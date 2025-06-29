package ru.job4j.bmb.core;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.content.SentContent;
import ru.job4j.bmb.repository.MoodLogFakeRepository;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ReminderService {
    private final SentContent sentContent;
    private final MoodLogRepository moodLogRepository;
    private final TgUI tgUI;

    public ReminderService(SentContent sentContent, MoodLogRepository moodLogRepository, TgUI tgUI) {
        this.sentContent = sentContent;
        this.moodLogRepository = moodLogRepository;
        this.tgUI = tgUI;
    }

    @Scheduled(fixedRateString = "${recommendation.alert.period}")
    public void remindUsers() {
        var startOfDay = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        var endOfDay = LocalDate.now()
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli() - 1;
        for (var user : moodLogRepository.findUsersWhoDidNotVoteToday(startOfDay, endOfDay)) {
            var content = new Content(user.getChatId());
            content.setText("Как настроение?");
            content.setMarkup(tgUI.buildButtons());
            sentContent.sent(content);
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("ReminderService инициализирован");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("ReminderService завершает работу");
    }
}
