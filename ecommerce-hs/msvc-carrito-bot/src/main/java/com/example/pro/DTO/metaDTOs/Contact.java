package com.example.pro.DTO.metaDTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor

public class Contact {
    private Profile profile;
    @JsonProperty("wa_id")

    private String wa_id;
}
