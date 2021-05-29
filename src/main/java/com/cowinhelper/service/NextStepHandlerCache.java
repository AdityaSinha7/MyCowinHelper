package com.cowinhelper.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NextStepHandlerCache {

    private static Map<Long, String> cache = new HashMap<>();

    public String pop(Long key) {
        String data = null;
        if (cache.containsKey(key)) {
            data = cache.get(key);
            cache.remove(key);
        }
        return data;
    }

    public String peek(Long key) {
        return cache.get(key);
    }

    public boolean isPresent(String key) {
        return cache.containsKey(key);
    }

    public void push(Long key, String value) {
        cache.put(key, value);
    }
}
