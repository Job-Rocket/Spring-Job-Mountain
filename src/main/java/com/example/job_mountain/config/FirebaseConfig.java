package com.example.job_mountain.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Firebase 설정 파일을 클래스패스에서 읽습니다.
        ClassPathResource serviceAccountFile = new ClassPathResource("jobdongsani-bfeeb-firebase-adminsdk-wsen9-e1808536bb.json");

        // Gson을 사용하여 lenient 모드로 JSON을 읽기
        try (JsonReader reader = new JsonReader(new InputStreamReader(serviceAccountFile.getInputStream()))) {
            reader.setLenient(true);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // FirebaseOptions를 설정합니다.
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountFile.getInputStream()))
                    .setProjectId(jsonObject.get("project_id").getAsString())
                    .setDatabaseUrl(jsonObject.get("database_url").getAsString())
                    .build();

            // FirebaseApp을 초기화하고 반환합니다.
            return FirebaseApp.initializeApp(options);
        }
    }
}