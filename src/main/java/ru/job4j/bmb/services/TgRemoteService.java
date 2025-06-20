package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.core.TgUI;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.Map;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;
    private final UserRepository userRepository;
    private final TgUI tgUI;

    private static final Map<String, String> MOOD_RESP = Map.of(
            "lost_sock", "Носки — это коварные создания. Но не волнуйся, второй обязательно найдётся!",
            "cucumber", "Огурец тоже дело серьёзное! Главное, не мариноваться слишком долго.",
            "dance_ready", "Супер! Танцуй, как будто никто не смотрит. Или, наоборот, как будто все смотрят!",
            "need_coffee", "Кофе уже в пути! Осталось только подождать... И ещё немного подождать...",
            "sleepy", "Пора на боковую! Даже супергерои отдыхают, ты не исключение."
    );

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           UserRepository userRepository,
                           TgUI tgUI) {
        this.botName = botName;
        this.botToken = botToken;
        this.userRepository = userRepository;
        this.tgUI = tgUI;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    private void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            var chatId = update.getCallbackQuery().getMessage().getChatId();
            send(new SendMessage(String.valueOf(chatId), MOOD_RESP.get(data)));
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            long chatId = message.getChatId();

            if ("/start".equals(message.getText())) {
                var user = userRepository.findByClientId(message.getFrom().getId());
                if (user == null) {
                    user = new User();
                    user.setClientId(message.getFrom().getId());
                    user.setChatId(chatId);
                    userRepository.save(user);
                }
            }
            var reply = new SendMessage(String.valueOf(chatId), "Как настроение сегодня?");
            reply.setReplyMarkup(tgUI.buildButtons());
            send(reply);
        }
    }
}
