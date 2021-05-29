package com.cowinhelper.bot.handler;

import com.cowinhelper.dto.Centers;
import com.cowinhelper.entity.User;
import com.cowinhelper.service.CowinService;
import com.cowinhelper.utility.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class OptionHandler extends AbstractHandler {

    @Autowired
    CowinService cowinService;

    public void availiblityCheckOption(User user, SendMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String todayDate = LocalDate.now().format(formatter);
        String tommorowDate = LocalDate.now().plusDays(1).format(formatter);
        Centers centersToday = cowinService.getSlotAvailablityByPincode(user.getPincode(), todayDate);
        Centers centersTommorow = cowinService.getSlotAvailablityByPincode(user.getPincode(), tommorowDate);
        StringBuilder msg = new StringBuilder(MessageTemplates.availiblityMessage(centersToday, todayDate));
        msg.append(MessageTemplates.availiblityMessage(centersTommorow, tommorowDate));
        message.setText(msg.toString());
    }

    public void availiblityAlertOption(User user, SendMessage message) {
        message.setText("This option is under DEV. Please try other options");
        //addOptionsReplyKeyboard(message);
    }

    public void bookOption(User user, SendMessage message) {
        message.setText("This option is under DEV. Please try other options");
        //addOptionsReplyKeyboard(message);
    }
}
