package com.cowinhelper.bot.handler;

import com.cowinhelper.entity.User;
import com.cowinhelper.service.CowinService;
import com.cowinhelper.utility.MessageTemplates;
import com.cowinhelper.utility.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class OptionHandler extends AbstractHandler {

    @Autowired
    CowinService cowinService;

    public void availiblityCheckOption(Update update, SendMessage message) {

        User user = userRepository.findById(update.getCallbackQuery().getFrom().getId()).get();
        if (Validator.isEmptyString(user.getPincode()) || user.getAge() == 0) {
            message.setText("Details are missing please run /start command");
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String ourformat = formatter.format(date.getTime());
        String availableMessage = MessageTemplates.availiblityMessage(cowinService.getSlotAvailablityByPincode(user.getPincode(), ourformat), ourformat);
        message.setText(availableMessage);
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
