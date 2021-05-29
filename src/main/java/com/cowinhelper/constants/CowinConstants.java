package com.cowinhelper.constants;

public interface CowinConstants {
    interface ApiUrl {
        String CALENDAR_URL_PINCODE = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/calendarByPin?pincode={0}&date={1}";
    }

    interface Regex {
        String pincode = "^[1-9][0-9]{5}$";
    }

    interface Error {
        String INVALID_PINCODE = "You have entered an invalid pincode";
        String INVALID_AGE = "You have entered an invalid age";
        String INVALID_DATE = "You have entered an invalid date format.Please use DD-MM-YYYY format";
        String SOMETHING_WENT_WRONG = "Something went wrong, please try again later";
    }

    interface StartCommand {
        String AVALIBLITY_CHECK = "Instant Check";
        String AVALIBLITY_ALERT = "Alert";
        String BOOK = "Book";
    }

    interface CallBackCommand {
        String AVALIBLITY_CHECK = "Instant Check";
        String AVALIBLITY_ALERT = "Alert";
        String BOOK = "Book";
    }
    interface HandlerCacheKey{
        String GET_PINCODE = "get_pincode";
        String GET_AGE = "get_age";
        String START_OPTIONS = "start_options";
    }
}
