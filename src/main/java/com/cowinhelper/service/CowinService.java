package com.cowinhelper.service;

import com.cowinhelper.constants.CowinConstants;
import com.cowinhelper.dto.Centers;
import com.cowinhelper.utility.HttpUtils;
import com.cowinhelper.utility.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

@Service
@Slf4j
public class CowinService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Centers getSlotAvailablityByPincode(String pincode, String date) {
        Centers reponse = null;
        try {
            Validator.validatePincode(pincode);
            Validator.validateDate(date);
            String URL = MessageFormat.format(CowinConstants.ApiUrl.CALENDAR_URL_PINCODE_PUBLIC, pincode, date);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, HttpUtils.buildGenericHttpEntity(), String.class);
            log.info("Response : {}", responseEntity.getBody());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                reponse = objectMapper.readValue(responseEntity.getBody(), Centers.class);
            }
        } catch (Exception e) {
            log.error("Exception:{}",e.getMessage(),e);
            //log.error(CowinConstants.Error.SOMETHING_WENT_WRONG);
        }
        return reponse;
    }
}
