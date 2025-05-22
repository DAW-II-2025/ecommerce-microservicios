package com.example.pro.services.Impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.pro.services.IDialogflowService;

class DialogflowServiceTest {
    
    @Autowired
    private IDialogflowService _DialogflowService;

    @Test
    void testDetectIntent() {
	System.out.println( _DialogflowService.detectIntent("hola", "es"));
    }

}
