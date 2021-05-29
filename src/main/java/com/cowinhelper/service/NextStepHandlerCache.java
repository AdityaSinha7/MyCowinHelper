package com.cowinhelper.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NextStepHandlerCache {

    private static Map<String, String> cache = new HashMap<>();

    public String pop(String key) {
        String data = null;
        if (cache.containsKey(key)) {
            data = cache.get(key);
            cache.remove(key);
        }
        return data;
    }

    public String peek(String key) {
        return cache.get(key);
    }

    public boolean isPresent(String key) {
        return cache.containsKey(key);
    }

    public void push(String key, String value) {
        cache.put(key, value);
    }
}
