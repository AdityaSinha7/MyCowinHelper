package com.cowinhelper.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {



    @Test
    void validateDate() {
        try {
            Validator.validateDate("26-05-20121");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}