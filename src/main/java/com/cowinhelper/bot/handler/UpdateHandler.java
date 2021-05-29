package com.cowinhelper.bot.handler;

import com.cowinhelper.bot.MyCowinHelperBot;
import com.cowinhelper.constants.CowinConstants;
import com.cowinhelper.entity.User;
import com.cowinhelper.utility.MessageBuilder;
import com.cowinhelper.utility.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class UpdateHandler extends AbstractHandler {

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
                message.setChatId(update.getMessage().getChatId().toString());
                handleMessages(update, message);
            } else if (update.hasCallbackQuery() && !Validator.isEmptyString(update.getCallbackQuery().getData())) {
                message.setChatId(update.getCallbackQuery().getFrom().getId().toString());
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
        if (Objects.nonNull(update.getMessage()) && "/start".equalsIgnoreCase(update.getMessage().getText())) {
            commandHandler.startCommand(update, message, update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getUserName());
        } else if (Objects.nonNull(update.getMessage()) && "/delete".equalsIgnoreCase(update.getMessage().getText())) {
            commandHandler.deleteCommand(update, message);
        } else if (Objects.nonNull(update.getMessage()) && "/cancel".equalsIgnoreCase(update.getMessage().getText())) {
            commandHandler.cancelCommand(update, message);
        } else {
            //data insertion
            Optional<User> user = userRepository.findById(update.getMessage().getChatId());
            String nextStep = nextStepHandlerCache.peek(update.getMessage().getChatId());
            if (!user.isPresent()) {
                message.setText("Please enter /start command to begin");
            } else {
                midSteps(user.get(), update, message, nextStep);
            }
//            String date = "27-05-2021";
//            String availableMessage = MessageTemplates.availiblityMessage(cowinService.getSlotAvailablityByPincode(update.getMessage().getText(), date), date);
//            message.setText(availableMessage);
        }
    }

    private void handleCallBackQueries(Update update, SendMessage message) {
        Optional<User> user = userRepository.findById(Long.valueOf(update.getCallbackQuery().getFrom().getId().toString()));
        if (user.isPresent()) {
            if (CowinConstants.CallBackCommand.AVALIBLITY_CHECK.equalsIgnoreCase(update.getCallbackQuery().getData())) {
                optionHandler.availiblityCheckOption(update, message);
            } else if (CowinConstants.CallBackCommand.AVALIBLITY_ALERT.equalsIgnoreCase(update.getCallbackQuery().getData())) {
                optionHandler.availiblityAlertOption(update, message);
            } else if (CowinConstants.CallBackCommand.BOOK.equalsIgnoreCase(update.getCallbackQuery().getData())) {
                optionHandler.bookOption(update, message);
            } else if (CowinConstants.HandlerCacheKey.GET_PINCODE.equalsIgnoreCase(update.getCallbackQuery().getData())
                    || CowinConstants.HandlerCacheKey.GET_AGE.equalsIgnoreCase(update.getCallbackQuery().getData())
                    || CowinConstants.HandlerCacheKey.START_OPTIONS.equalsIgnoreCase(update.getCallbackQuery().getData())) {
                midSteps(user.get(), update, message, update.getCallbackQuery().getData());
            }
        } else {
            //if user is trying to use options without giving pincode and age info
            commandHandler.startCommand(update, message, update.getCallbackQuery().getFrom().getFirstName(), update.getCallbackQuery().getFrom().getUserName());
        }
    }

    private void midSteps(User user, Update update, SendMessage message, String nextStep) {
        if (CowinConstants.HandlerCacheKey.GET_PINCODE.equalsIgnoreCase(nextStep)) {
            if(Objects.nonNull(update.getMessage()) && !Validator.isEmptyString(update.getMessage().getText())){
                updatePincode(user, update.getMessage().getText(), message);
            }else {
                StringBuilder msg = new StringBuilder();
                msg.append("Please give me your ").append(MessageBuilder.bold("pincode"));
                nextStepHandlerCache.push(user.getId(), CowinConstants.HandlerCacheKey.GET_PINCODE);
                message.setText(msg.toString());
            }
        } else if (CowinConstants.HandlerCacheKey.GET_AGE.equalsIgnoreCase(nextStep)) {
            if(Objects.nonNull(update.getMessage()) && !Validator.isEmptyString(update.getMessage().getText())){
                updateAge(user, update.getMessage().getText(), message);
            }else {
                StringBuilder msg = new StringBuilder();
                msg.append("Please give me your ").append(MessageBuilder.bold("AGE"));
                nextStepHandlerCache.push(user.getId(), CowinConstants.HandlerCacheKey.GET_AGE);
                message.setText(msg.toString());
            }
        }
        nextStep = nextStepHandlerCache.peek(update.getMessage().getChatId());
        if (CowinConstants.HandlerCacheKey.START_OPTIONS.equalsIgnoreCase(nextStep)) {
            message.setText("Please select one of the below options to proceed");
            addOptionsReplyKeyboard(message);
        }
    }

}
