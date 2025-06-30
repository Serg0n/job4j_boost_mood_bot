package ru.job4j.bmb.core;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.repository.MoodRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TgUI.class)
class TgUITest {
    @Autowired
    private TgUI tgUI;

    @MockBean
    private MoodRepository moodRepository;

    @Test
    void whenBuildButtonsThenReturnCorrectKeyboardMarkup() {
        Mood happy = new Mood();
        happy.setId(1L);
        happy.setText("Happy");

        Mood sad = new Mood();
        sad.setId(2L);
        sad.setText("Sad");

        when(moodRepository.findAll()).thenReturn(List.of(happy, sad));
        InlineKeyboardMarkup result = tgUI.buildButtons();
        assertThat(result).isNotNull();
        assertThat(result.getKeyboard()).hasSize(2);
        InlineKeyboardButton firstButton = result.getKeyboard().get(0).get(0);
        assertThat(firstButton.getText()).isEqualTo("Happy");
        assertThat(firstButton.getCallbackData()).isEqualTo("1");
        InlineKeyboardButton secondButton = result.getKeyboard().get(1).get(0);
        assertThat(secondButton.getText()).isEqualTo("Sad");
        assertThat(secondButton.getCallbackData()).isEqualTo("2");
    }

    @Test
    void whenNoMoodsThenReturnEmptyKeyboard() {
        when(moodRepository.findAll()).thenReturn(List.of());
        InlineKeyboardMarkup result = tgUI.buildButtons();
        assertThat(result).isNotNull();
        assertThat(result.getKeyboard()).isEmpty();
    }
}