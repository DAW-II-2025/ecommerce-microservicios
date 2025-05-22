package com.example.pro.DTO.metaDTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor

public class Message {
    private String from;
    private String id;
    private String timestamp;
    private String type;
    private Text text;
}
