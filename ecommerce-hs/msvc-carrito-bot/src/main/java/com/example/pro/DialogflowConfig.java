package com.example.pro;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class DialogflowConfig {
    
    @Value("${google.dialogflow.credentials}")
    private String pathCredencial;

    @Bean
    public SessionsClient sessionsClient() throws IOException {
        Path jsonPath = Path.of(pathCredencial);
        GoogleCredentials credentials = GoogleCredentials.fromStream(Files.newInputStream(jsonPath));
        
        SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        return SessionsClient.create(sessionsSettings);
    }
}
