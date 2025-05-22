package com.example.pro.DTO.metaDTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Metadata {
    @JsonProperty("display_phone_number")

    private String display_phone_number;
    @JsonProperty("phone_number_id")

    private String phone_number_id;
}
