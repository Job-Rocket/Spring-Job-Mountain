package com.example.job_mountain.chunkupload.controller;

import com.example.job_mountain.chunkupload.service.ChunkUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class ChunkUploadController {

    private final ChunkUploadService chunkUploadService;

    // video - 기본 업로드 API
    @PostMapping("/video")
    public ResponseEntity<String> chunkUpload(@RequestPart("file") MultipartFile file) throws IOException {
        boolean isDone = chunkUploadService.chunkUpload(file);

        return isDone ?
                ResponseEntity.ok("File uploaded successfully") :
                ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
    }

    // shorts - 기본 업로드 API
    @PostMapping("/shorts")
    public ResponseEntity<String> shortsUpload(@RequestPart("file") MultipartFile file) throws IOException {
        boolean isDone = chunkUploadService.chunkUpload2(file);

        return isDone ?
                ResponseEntity.ok("File uploaded successfully") :
                ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
    }


    // html 으로 확인할 수 있도록 하는 코드
    @GetMapping("/chunk")
    public String chunkUploadPage() {
        return "chunk";
    }

    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity<String> chunkUpload(@RequestParam("chunk") MultipartFile file,
                                              @RequestParam("chunkNumber") int chunkNumber,
                                              @RequestParam("totalChunks") int totalChunks) throws IOException {
        boolean isDone = chunkUploadService.chunkUpload(file, chunkNumber, totalChunks);

        return isDone ?
                ResponseEntity.ok("File uploaded successfully") :
                ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
    }

    @GetMapping("/health")
    public String getHealthCheck() {
        return "It Works!!!";
    }
}
