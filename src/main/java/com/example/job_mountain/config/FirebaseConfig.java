package com.example.job_mountain.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Firebase 설정 파일을 클래스패스에서 읽습니다.
        ClassPathResource serviceAccountFile = new ClassPathResource("jobdongsani-bfeeb-firebase-adminsdk-wsen9-e1808536bb.json");

        // BufferedReader를 사용하여 JSON 파일의 내용을 문자열로 읽습니다.
        String jsonString;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(serviceAccountFile.getInputStream()))) {
            jsonString = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            System.out.println(jsonString); // JSON 내용 출력
        }

        // Gson을 사용하여 JSON 문자열을 파싱합니다.
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // JSON 객체에서 필요한 키가 존재하는지 확인
        if (!jsonObject.has("project_id") || !jsonObject.has("database_url")) {
            throw new IllegalArgumentException("Invalid Firebase configuration: missing required keys");
        }

        // FirebaseOptions를 설정합니다.
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8))))
                .setProjectId(jsonObject.get("project_id").getAsString())
                .setDatabaseUrl(jsonObject.get("database_url").getAsString())
                .build();

        // FirebaseApp을 초기화하고 반환합니다.
        return FirebaseApp.initializeApp(options);
    }
}
