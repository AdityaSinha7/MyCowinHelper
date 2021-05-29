package com.cowinhelper.utility;

import com.cowinhelper.dto.Center;
import com.cowinhelper.dto.Centers;
import com.cowinhelper.dto.Session;

public class MessageTemplates {
    public static String welcomeMessage(String firstName) {
        StringBuilder message = new StringBuilder();
        message.append("Hey! ").append(MessageBuilder.bold(firstName)).append("!!").append(MessageBuilder.addLineBreak());
        return message.toString();
    }

    public static String startOptionsMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Choose one of the below options:").append(MessageBuilder.addLineBreak())
                .append("1 - For availiblity checking").append(MessageBuilder.addLineBreak())
                .append("2 - For auto notification scheduling").append(MessageBuilder.addLineBreak())
                .append("3 - For initiate booking").append(MessageBuilder.addLineBreak());
        return message.toString();
    }

    public static String availiblityMessage(Centers centers, String date) {
        StringBuilder message = new StringBuilder();
        message.append("[").append(MessageBuilder.bold(date)).append("]").append(MessageBuilder.addLineBreak());
        for (Center center : centers.getCenters()) {
            message.append(MessageBuilder.bold(center.getName())).append("  ").append(center.getPincode()).append(MessageBuilder.addLineBreak());
            for(Session session : center.getSessions()){
                if(session.getDate().equalsIgnoreCase(date)){
                    message.append("Capacity:").append(MessageBuilder.bold(session.getAvailable_capacity())).append(MessageBuilder.addLineBreak());
                    message.append("Age:").append(MessageBuilder.bold(session.getMin_age_limit())).append(MessageBuilder.addLineBreak());
                    message.append("Vaccine:").append(MessageBuilder.bold(session.getVaccine())).append(MessageBuilder.addLineBreak());
                    break;
                }
            }
            message.append(MessageBuilder.addLineBreak());
        }
        return message.toString();
    }
}