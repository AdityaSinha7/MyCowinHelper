package com.cowinhelper.utility;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;

public class HttpUtils {

    public static HttpHeaders buildGenericHttpHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.put("User-Agent", Arrays.asList("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"));
        return headers;
    }

    public static HttpEntity<String> buildGenericHttpEntity(){
        HttpHeaders headers = new HttpHeaders();
        //headers.put("User-Agent", Arrays.asList("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36"));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return entity;
    }
}
