package com.example.job_mountain.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Firebase 설정 파일을 클래스패스에서 읽습니다.
        ClassPathResource serviceAccountFile = new ClassPathResource("jobdongsani-bfeeb-firebase-adminsdk-wsen9-e1808536bb.json");
        System.out.println("시작!!");
        // InputStream을 사용하여 GoogleCredentials을 초기화합니다.
        try (InputStream inputStream = serviceAccountFile.getInputStream()) {
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            // FirebaseApp을 초기화하고 반환합니다.
            return FirebaseApp.initializeApp(options);
        }
    }
}
