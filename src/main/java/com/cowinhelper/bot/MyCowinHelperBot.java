package com.cowinhelper.bot;

import com.cowinhelper.bot.handler.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MyCowinHelperBot extends TelegramLongPollingBot {

    @Value("${cowinhelper.bot.username}")
    private String username;

    @Value("${cowinhelper.bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Autowired
    UpdateHandler updateHandler;

    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.handleUpdate(update);
    }
}
