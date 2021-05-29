package com.cowinhelper.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class OptionHandler extends AbstractHandler {

    public void availiblityCheckOption(Update update, SendMessage message) {
        message.setText("Please enter your pincode");
    }

    public void availiblityAlertOption(Update update, SendMessage message) {
        message.setText("This option is under DEV. Please try other options");
        //addOptionsReplyKeyboard(message);
    }

    public void bookOption(Update update, SendMessage message) {
        message.setText("This option is under DEV. Please try other options");
        //addOptionsReplyKeyboard(message);
    }
}
