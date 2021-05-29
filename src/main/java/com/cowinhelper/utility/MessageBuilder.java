package com.cowinhelper.utility;

public class MessageBuilder {

    public static String addLineBreak(){
        return "\n";
    }
    public static String bold(Object input){
        return "<b>"+String.valueOf(input)+"</b>";
    }
}
