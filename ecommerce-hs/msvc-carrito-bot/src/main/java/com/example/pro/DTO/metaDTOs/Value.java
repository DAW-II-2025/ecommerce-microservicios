package com.example.pro.DTO.metaDTOs;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {
    @JsonProperty("messaging_product")
    private String messagingProduct;
    private Metadata metadata;
    private List<Contact> contacts  = new ArrayList<>();;
    private List<Message> messages  = new ArrayList<>();;
}
