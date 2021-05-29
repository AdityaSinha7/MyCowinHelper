package com.cowinhelper.bot.handler;

import com.cowinhelper.constants.CowinConstants;
import com.cowinhelper.entity.User;
import com.cowinhelper.repository.UserRepository;
import com.cowinhelper.service.NextStepHandlerCache;
import com.cowinhelper.utility.MessageBuilder;
import com.cowinhelper.utility.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHandler {

    @Autowired
    protected NextStepHandlerCache nextStepHandlerCache;

    @Autowired
    protected UserRepository userRepository;

    protected void addOptionsReplyKeyboard(SendMessage message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().build();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(new InlineKeyboardButton(CowinConstants.StartCommand.AVALIBLITY_CHECK) {{
            setCallbackData(CowinConstants.StartCommand.AVALIBLITY_CHECK);
        }});
        keyboardButtons.add(new InlineKeyboardButton(CowinConstants.StartCommand.AVALIBLITY_ALERT) {{
            setCallbackData(CowinConstants.StartCommand.AVALIBLITY_ALERT);
        }});
        keyboardButtons.add(new InlineKeyboardButton(CowinConstants.StartCommand.BOOK) {{
            setCallbackData(CowinConstants.StartCommand.BOOK);
        }});
        inlineKeyboardMarkup.setKeyboard(new ArrayList<List<InlineKeyboardButton>>() {{
            add(keyboardButtons);
        }});
        message.setReplyMarkup(inlineKeyboardMarkup);
    }

    protected InlineKeyboardMarkup getInlineKeyboardWithCallBackData(String buttonName, String callbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().build();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(new InlineKeyboardButton(buttonName) {{
            setCallbackData(callbackData);
        }});
        inlineKeyboardMarkup.setKeyboard(new ArrayList<List<InlineKeyboardButton>>() {{
            add(keyboardButtons);
        }});
        return inlineKeyboardMarkup;
    }

    protected InlineKeyboardMarkup getInlineKeyboardWithCallBackData(List<String> buttonNames, List<String> callbackDatas) {
        if (buttonNames.size() == callbackDatas.size()) {
            InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().build();
            List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
            for (int i = 0; i < buttonNames.size(); i++) {
                int index = i;
                keyboardButtons.add(new InlineKeyboardButton(buttonNames.get(i)) {{
                    setCallbackData(callbackDatas.get(index));
                }});
            }
            inlineKeyboardMarkup.setKeyboard(new ArrayList<List<InlineKeyboardButton>>() {{
                add(keyboardButtons);
            }});
            return inlineKeyboardMarkup;
        }
        return null;
    }

    protected void updatePincode(User user, String pincode, SendMessage message) {
        try {
            Validator.validatePincode(pincode);
            user.setPincode(pincode);
            userRepository.save(user);
            if (user.getAge() == 0) {
                nextStepHandlerCache.push(user.getId(), CowinConstants.HandlerCacheKey.GET_AGE);
                StringBuilder msg = new StringBuilder();
                msg.append("Please tell me your ").append(MessageBuilder.bold("AGE"));
                message.setText(msg.toString());
            } else {
                nextStepHandlerCache.push(user.getId(), CowinConstants.HandlerCacheKey.START_OPTIONS);
            }
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }

    protected void updateAge(User user, Object ageObj, SendMessage message) {
        try {
            int age = Validator.validateAge(ageObj);
            user.setAge(age);
            userRepository.save(user);
            if (Validator.isEmptyString(user.getPincode())) {
                nextStepHandlerCache.push(user.getId(), CowinConstants.HandlerCacheKey.GET_PINCODE);
                StringBuilder msg = new StringBuilder();
                msg.append("Please give me your ").append(MessageBuilder.bold("pincode"));
                message.setText(msg.toString());
            } else {
                nextStepHandlerCache.push(user.getId(), CowinConstants.HandlerCacheKey.START_OPTIONS);
            }
        } catch (Exception e) {
            message.setText(e.getMessage());
        }
    }
}
