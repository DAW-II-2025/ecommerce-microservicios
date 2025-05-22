package com.example.pro.model;

import com.example.pro.DTO.metaDTOs.Text;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class requestMessage {
    @JsonProperty("messaging_product")
    private String messaging_product;
    @JsonProperty("recipient_type")
    private String recipient_type;
    private String to;
    private String type;
    private Text text;
    private ImageMessage image;
    
    public requestMessage(String to, String type, ImageMessage image,Text text) {
	super();
	this.to = to;
	this.type = type;
	this.image = image;
	this.text = text;
	messaging_product = "whatsapp";
	recipient_type = "individual";
    }
    
    
}
