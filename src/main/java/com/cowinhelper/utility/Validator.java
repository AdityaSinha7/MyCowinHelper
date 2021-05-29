package com.cowinhelper.utility;

import com.cowinhelper.constants.CowinConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Validator {

    public static void validateDate(String dateStr) throws Exception {
        if(Validator.isEmptyString(dateStr)){
            throw new Exception(CowinConstants.Error.INVALID_DATE);
        }
        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        sdf.parse(dateStr);
    }

    public static void validatePincode(String pincode) throws Exception {
        if(Validator.isEmptyString(pincode) || !pincode.matches(CowinConstants.Regex.pincode)){
            throw new Exception(CowinConstants.Error.INVALID_PINCODE);
        }
    }

    public static boolean isEmptyString(String str){
        return null==str || "".equals(str);
    }
}
