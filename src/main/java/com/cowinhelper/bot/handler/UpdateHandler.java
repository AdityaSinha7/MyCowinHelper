package com.cowinhelper.bot.handler;

import com.cowinhelper.bot.MyCowinHelperBot;
import com.cowinhelper.constants.CowinConstants;
import com.cowinhelper.entity.User;
import com.cowinhelper.utility.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        logIncomingMessgae(update);
        SendMessage message = new SendMessage();
        message.setParseMode("html");

        try {
            Long userId = 0L;
            String firstName = null;
            String userName = null;
            if (update.hasMessage()) {
                userId = update.getMessage().getChatId();
                firstName = update.getMessage().getFrom().getFirstName();
                userName = update.getMessage().getFrom().getUserName();
            } else if (update.hasCallbackQuery()) {
                userId = update.getCallbackQuery().getFrom().getId();
                firstName = update.getCallbackQuery().getFrom().getFirstName();
                userName = update.getCallbackQuery().getFrom().getUserName();
            } else {
                log.error(CowinConstants.Error.SOMETHING_WENT_WRONG);
                return;
            }

            Optional<User> user = userService.findUser(userId);
            message.setChatId(userId.toString());
            if (user.isPresent()) {
                if (update.hasMessage()) {
                    handleMessages(update, user.get(), message);
                } else if (update.hasCallbackQuery()) {
                    handleCallBackQueries(update, user.get(), message);
                } else {
                    log.error("Unknown command");
                    return;
                }
            } else {
                if (update.hasMessage() && "/delete".equalsIgnoreCase(update.getMessage().getText())) {
                    message.setText("User not registered, please send /start command to begin");
                } else if (update.hasMessage() && "/share".equalsIgnoreCase(update.getMessage().getText())) {
                    //get bot sharable link
                    commandHandler.shareCommand(message);
                }
                //Register the new user
                User newUser = userService.registerUser(userId, firstName, userName);
                commandHandler.fetchInfo(newUser, message);
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

    private void handleMessages(Update update, User user, SendMessage message) {
        if (CowinConstants.HandlerCacheKey.GET_PINCODE.equalsIgnoreCase(nextStepHandlerCache.peek(user.getId()))) {
            //pincode input request
            nextStepHandlerCache.pop(user.getId());
            updatePincode(user, message, update.getMessage().getText());
        } else if (CowinConstants.HandlerCacheKey.GET_AGE.equalsIgnoreCase(nextStepHandlerCache.peek(user.getId()))) {
            //age input request
            nextStepHandlerCache.pop(user.getId());
            updateAge(user, message, update.getMessage().getText());
        } else if ("/start".equalsIgnoreCase(update.getMessage().getText())) {
            //show all 3 options
            commandHandler.startCommand(user, message);
        } else if ("/delete".equalsIgnoreCase(update.getMessage().getText())) {
            //delete user info from DB
            commandHandler.deleteCommand(user, message);
        } else if ("/share".equalsIgnoreCase(update.getMessage().getText())) {
            //get bot sharable link
            commandHandler.shareCommand(message);
        }
    }

    private void handleCallBackQueries(Update update, User user, SendMessage message) {
        if (update.hasCallbackQuery() && Validator.validateAgeAndPincode(user)) {
            //option call and we have the user data
            if (CowinConstants.CallBackCommand.AVALIBLITY_CHECK.equals(update.getCallbackQuery().getData())) {
                optionHandler.availiblityCheckOption(user, message);
            } else if (CowinConstants.CallBackCommand.AVALIBLITY_ALERT.equals(update.getCallbackQuery().getData())) {
                optionHandler.availiblityAlertOption(user, message);
            } else if (CowinConstants.CallBackCommand.BOOK.equals(update.getCallbackQuery().getData())) {
                optionHandler.bookOption(user, message);
            }
        } else if (update.hasCallbackQuery() && !Validator.validateAgeAndPincode(user)) {
            //option call but we don't have the user data
            commandHandler.fetchInfo(user, message);
        }
    }

    private void logIncomingMessgae(Update update) {
        if (update.hasCallbackQuery()) {
            log.info("User:{} CallbackQuery:{} UserId:{}", update.getCallbackQuery().getFrom().getFirstName(), update.getCallbackQuery().getData(), update.getCallbackQuery().getFrom().getId());
        } else if (update.hasMessage()) {
            log.info("User:{} Message:{} UserId:{}", update.getMessage().getFrom().getFirstName(), update.getMessage().getText(), update.getMessage().getChatId());
        } else {
            log.info("Neither a callback nor a message");
        }
    }

}
