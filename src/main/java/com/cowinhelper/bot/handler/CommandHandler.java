package com.cowinhelper.bot.handler;

import com.cowinhelper.constants.CowinConstants;
import com.cowinhelper.entity.User;
import com.cowinhelper.utility.MessageBuilder;
import com.cowinhelper.utility.MessageTemplates;
import com.cowinhelper.utility.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class CommandHandler extends AbstractHandler {

    public void startCommand(User user, SendMessage message) {
        StringBuilder msg = new StringBuilder(MessageTemplates.welcomeMessage(user.getFirstName()));
        msg.append("Your saved pincode is ").append(MessageBuilder.bold(user.getPincode())).append(" and age is ")
                .append(MessageBuilder.bold(user.getAge())).append(MessageBuilder.addLineBreak());
        msg.append("To change your info send /delete command otherwise choose one of the below options to proceed");
        addOptionsReplyKeyboard(message);
        message.setText(msg.toString());
    }

    public void deleteCommand(User user, SendMessage message) {
        StringBuilder msg = new StringBuilder();
        userService.deleteUser(user);
        log.info("User:{} is deleted from the system!", user.getFirstName());
        msg.append("User deleted, hope to see you again!").append(MessageBuilder.addLineBreak());
        msg.append("Give /start command to begin anytime");
        message.setText(msg.toString());
    }

    public void cancelCommand(Update update, SendMessage message) {
    }

    public void fetchInfo(User user,SendMessage message){
        StringBuilder msg = new StringBuilder();
        if(Validator.isEmptyString(user.getPincode()) && user.getAge()==0){
            //first time info fetch
            msg.append(MessageTemplates.welcomeMessage(user.getFirstName()));
            msg.append("I will need some of your info before we can proceed").append(MessageBuilder.addLineBreak());
            msg.append("Please give me your ").append(MessageBuilder.bold("PINCODE"));
            nextStepHandlerCache.push(user.getId(), CowinConstants.HandlerCacheKey.GET_PINCODE);
        }
        else if(user.getAge()==0){
            //have pincode but not age
            msg.append("Please tell me your ").append(MessageBuilder.bold("AGE"));
            nextStepHandlerCache.push(user.getId(), CowinConstants.HandlerCacheKey.GET_AGE);
        }else if(Validator.isEmptyString(user.getPincode())){
            //have age but not pincode
            msg.append("Please give me your ").append(MessageBuilder.bold("PINCODE"));
            nextStepHandlerCache.push(user.getId(), CowinConstants.HandlerCacheKey.GET_PINCODE);
        }
        message.setText(msg.toString());
    }

    public void shareCommand(SendMessage message) {
        StringBuilder msg = new StringBuilder();
        //msg.append("<script async src=\"https://telegram.org/js/telegram-widget.js?15\" data-telegram-share-url=\"http://telegram.me/MyCowinHelperBot\"></script>");
        //msg.append("http://telegram.me/MyCowinHelperBot");
        msg.append("Share <a href=\"http://telegram.me/MyCowinHelperBot/\">Link</a>");
        message.setText(msg.toString());
    }
}
