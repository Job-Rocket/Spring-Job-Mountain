package com.example.job_mountain.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Firebase 설정 파일을 가져옵니다.
        ClassPathResource serviceAccountFile = new ClassPathResource("jobdongsani-bfeeb-firebase-adminsdk-wsen9-e1808536bb.json");

        // FirebaseOptions를 설정합니다.
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountFile.getInputStream()))
                .build();

        // FirebaseApp을 초기화하고 반환합니다.
        return FirebaseApp.initializeApp(options);
    }
}

//package com.example.job_mountain.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Configuration
//public class FirebaseConfig {
//
//    @Bean
//    public FirebaseApp firebaseApp() throws IOException {
//        // 환경 변수에서 Firebase 설정 파일 내용을 가져옵니다.
//        String firebaseConfig = System.getenv("FIREBASE_SERVICE_ACCOUNT");
//
//        if (firebaseConfig == null) {
//            throw new IllegalStateException("FIREBASE_SERVICE_ACCOUNT environment variable is not set.");
//        }
//
//        // 환경 변수에서 읽어온 내용을 InputStream으로 변환합니다.
//        ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8));
//
//        // FirebaseOptions를 설정합니다.
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
//                .build();
//
//        // FirebaseApp을 초기화하고 반환합니다.
//        return FirebaseApp.initializeApp(options);
//    }
//}
