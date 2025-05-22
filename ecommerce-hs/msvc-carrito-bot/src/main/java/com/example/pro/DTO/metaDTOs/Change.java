package com.example.pro.DTO.metaDTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = true)

public class Change {
    private String field;
    private Value value;
}
