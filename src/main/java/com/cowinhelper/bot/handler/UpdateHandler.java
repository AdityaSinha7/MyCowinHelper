package com.cowinhelper.bot.handler;

import com.cowinhelper.bot.MyCowinHelperBot;
import com.cowinhelper.constants.CowinConstants;
import com.cowinhelper.service.CowinService;
import com.cowinhelper.utility.MessageTemplates;
import com.cowinhelper.utility.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

@Component
@Slf4j
public class UpdateHandler {
    @Autowired
    CowinService cowinService;

    @Autowired
    CommandHandler commandHandler;

    @Autowired
    OptionHandler optionHandler;

    @Autowired
    MyCowinHelperBot myCowinHelperBot;

    public void handleUpdate(Update update) {
        log.info("message: {}", String.valueOf(update.getMessage()));
        // log.info("location: lat:{}, long:{}",update.getMessage().getLocation().getLatitude(),update.getMessage().getLocation().getLongitude());
        SendMessage message = new SendMessage();
        message.setParseMode("html");

        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handleMessages(update, message);
            } else if (update.hasCallbackQuery() && !Validator.isEmptyString(update.getCallbackQuery().getData())) {
                handleCallBackQueries(update, message);
            }
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
        try {
            myCowinHelperBot.execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleMessages(Update update, SendMessage message) {
        message.setChatId(update.getMessage().getChatId().toString());
        if (Objects.nonNull(update.getMessage()) && "/start".equalsIgnoreCase(update.getMessage().getText())) {
            commandHandler.startCommand(update, message);
        } else if (Objects.nonNull(update.getMessage()) && "/delete".equalsIgnoreCase(update.getMessage().getText())) {
            commandHandler.deleteCommand(update, message);
        } else if (Objects.nonNull(update.getMessage()) && "/cancel".equalsIgnoreCase(update.getMessage().getText())) {
            commandHandler.cancelCommand(update, message);
        } else {
            String date = "27-05-2021";
            String availableMessage = MessageTemplates.availiblityMessage(cowinService.getSlotAvailablityByPincode(update.getMessage().getText(), date), date);
            message.setText(availableMessage);
        }
    }

    private void handleCallBackQueries(Update update, SendMessage message) {
        message.setChatId(update.getCallbackQuery().getFrom().getId().toString());
        if (CowinConstants.CallBackCommand.AVALIBLITY_CHECK.equalsIgnoreCase(update.getCallbackQuery().getData())) {
            optionHandler.availiblityCheckOption(update, message);
        } else if (CowinConstants.CallBackCommand.AVALIBLITY_ALERT.equalsIgnoreCase(update.getCallbackQuery().getData())) {
            optionHandler.availiblityAlertOption(update, message);
        } else if (CowinConstants.CallBackCommand.BOOK.equalsIgnoreCase(update.getCallbackQuery().getData())) {
            optionHandler.bookOption(update, message);
        }
    }

}
