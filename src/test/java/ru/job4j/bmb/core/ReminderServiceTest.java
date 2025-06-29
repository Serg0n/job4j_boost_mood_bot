package ru.job4j.bmb.core;

import org.junit.jupiter.api.Test;
import ru.job4j.bmb.content.SentContent;
import ru.job4j.bmb.core.TgUI;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.MoodFakeRepository;
import ru.job4j.bmb.repository.MoodLogFakeRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderServiceTest {

    @Test
    void whenUserDidNotVoteTodayThenSendReminder() {
        List<Content> result = new ArrayList<>();
        var sentContent = new SentContent() {
            @Override
            public void sent(Content content) {
                result.add(content);
            }
        };

        var moodRepository = new MoodFakeRepository();
        moodRepository.save(new Mood("Good", true));

        var moodLogRepository = new MoodLogFakeRepository();
        var user = new User();
        user.setChatId(123456L);

        var oldLog = new MoodLog();
        oldLog.setUser(user);
        long fiveDaysAgo = LocalDate.now()
                .minusDays(5)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        oldLog.setCreatedAt(fiveDaysAgo);
        moodLogRepository.save(oldLog);

        var reminderService = new ReminderService(sentContent, moodLogRepository, new TgUI(moodRepository));
        reminderService.remindUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getText()).isEqualTo("Как настроение?");
    }

    @Test
    void whenUserAlreadyVotedTodayThenNoReminderSent() {
        List<Content> result = new ArrayList<>();
        var sentContent = new SentContent() {
            @Override
            public void sent(Content content) {
                result.add(content);
            }
        };

        var moodRepository = new MoodFakeRepository();
        moodRepository.save(new Mood("Good", true));

        var moodLogRepository = new MoodLogFakeRepository();
        var user = new User();
        user.setChatId(999999L);

        long today = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli();

        var todayLog = new MoodLog();
        todayLog.setUser(user);
        todayLog.setCreatedAt(today);
        moodLogRepository.save(todayLog);

        var reminderService = new ReminderService(sentContent, moodLogRepository, new TgUI(moodRepository));
        reminderService.remindUsers();

        assertThat(result).isEmpty();
    }
}
