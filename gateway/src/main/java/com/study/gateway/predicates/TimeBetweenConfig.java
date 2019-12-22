package com.study.gateway.predicates;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeBetweenConfig {

    private LocalTime start;
    private LocalTime end;
}
