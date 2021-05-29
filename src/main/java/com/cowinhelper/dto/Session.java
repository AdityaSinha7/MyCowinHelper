package com.cowinhelper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Session {
    private String date;
    private int available_capacity;
    private int min_age_limit;
    private String vaccine;
    private List<String> slots;
    private int available_capacity_dose1;
    private int available_capacity_dose2;
}
