package com.cowinhelper.bot.handler;

import com.cowinhelper.entity.User;
import com.cowinhelper.utility.MessageBuilder;
import com.cowinhelper.utility.MessageTemplates;
import com.cowinhelper.utility.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Slf4j
@Component
public class CommandHandler extends AbstractHandler {

    public void startCommand(Update update, SendMessage message) {
        StringBuilder msg = new StringBuilder(MessageTemplates.welcomeMessage(update.getMessage().getFrom().getFirstName()));

        Optional<User> user = userRepository.findById(update.getMessage().getChatId());
        if (!user.isPresent()) {
            msg.append("I will need some of your info before we can proceed.").append(MessageBuilder.addLineBreak());
            msg.append("Please give me your ").append(MessageBuilder.bold("pincode"));
        } else if (user.isPresent() && Validator.isEmptyString(user.get().getPincode())) {
            msg.append("Please give me your ").append(MessageBuilder.bold("PINCODE"));
        } else if (user.isPresent() && user.get().getAge() == 0) {
            msg.append("Please tell me your ").append(MessageBuilder.bold("AGE"));
        }


        message.setText(msg.toString());
        addOptionsReplyKeyboard(message);
    }

    public void deleteCommand(Update update, SendMessage message) {
        userRepository.deleteById(update.getMessage().getChatId());
        log.info("User:{} is deleted from the system!", update.getMessage().getFrom().getFirstName());
    }

    public void cancelCommand(Update update, SendMessage message) {
    }
}
