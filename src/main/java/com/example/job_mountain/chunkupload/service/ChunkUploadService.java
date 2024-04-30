package com.example.job_mountain.chunkupload.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Slf4j
@Service
public class ChunkUploadService {

    // video - 기본 업로드 기능 수행
    public boolean chunkUpload(MultipartFile file) throws IOException {
        // 파일 업로드 위치
        String uploadDir = "video";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 임시 저장 파일 이름
        String filename = file.getOriginalFilename();

        Path filePath = Paths.get(uploadDir, filename);
        // 임시 저장
        Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE);

        log.info("파일 업로드 성공 !");
        return true;
    }

    // shorts - 기본 업로드 기능 수행
    public boolean chunkUpload2(MultipartFile file) throws IOException {
        // 파일 업로드 위치
        String uploadDir = "shorts";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 임시 저장 파일 이름
        String filename = file.getOriginalFilename();

        Path filePath = Paths.get(uploadDir, filename);
        // 임시 저장
        Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE);

        log.info("파일 업로드 성공 !");
        return true;
    }

    public boolean chunkUpload(MultipartFile file, int chunkNumber, int totalChunks) throws IOException {
        // 파일 업로드 위치
        String uploadDir = "src/main/resources/static/upload/video";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 임시 저장 파일 이름
        String filename = file.getOriginalFilename() + ".part" + chunkNumber;

        Path filePath = Paths.get(uploadDir, filename);
        // 임시 저장
        Files.write(filePath, file.getBytes());

        // 마지막 조각이 전송 됐을 경우
        if (chunkNumber == totalChunks-1) {
            String[] split = file.getOriginalFilename().split("\\.");
            String outputFilename = UUID.randomUUID() + "." + split[split.length-1];
            Path outputFile = Paths.get(uploadDir, outputFilename);
            Files.createFile(outputFile);

            // 임시 파일들을 하나로 합침
            for (int i = 0; i < totalChunks; i++) {
                Path chunkFile = Paths.get(uploadDir, file.getOriginalFilename() + ".part" + i);
                Files.write(outputFile, Files.readAllBytes(chunkFile), StandardOpenOption.APPEND);
                // 합친 후 삭제
                Files.delete(chunkFile);
            }
            log.info("파일 업로드 성공 !");
            return true;
        } else {
            return false;
        }
    }
}
