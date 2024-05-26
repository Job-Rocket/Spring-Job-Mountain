package com.example.job_mountain.file;

import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;

    private static final String downPath_front = "https://firebasestorage.googleapis.com/v0/b/jobdongsani-bfeeb.appspot.com/o/";
    private static final String downPath_back = "?alt=media";
    private static final Integer STATUS = 1;


    public String uploadFiles(MultipartFile file, String name) throws IOException {
        // Firebase Storage의 버킷 가져오기
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        // 파일의 InputStream 가져오기
        InputStream content = file.getInputStream();

        // 메타데이터에 access token 추가
        Map<String, String> metadata = new HashMap<>();
        String access_token = UUID.randomUUID().toString();
        metadata.put("firebaseStorageDownloadTokens", access_token);  // 적절한 토큰 값 설정

        // BlobInfo 생성 시 메타데이터 설정
        BlobId blobId = BlobId.of(bucket.getName(), name);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .setMetadata(metadata)
                .build();
        Storage storage = bucket.getStorage();
        Blob blob = storage.create(blobInfo, content);
        // 업로드된 파일의 미디어 링크 반환
        return access_token;
    }

    public String saveFile(Long id, MultipartFile multipartFile, String fileName) {
        try {
            String name = FilenameUtils.getBaseName(multipartFile.getOriginalFilename()) + "-" + fileName + id
                    + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            String access_token = uploadFiles(multipartFile, name);
            return downPath_front + name + downPath_back + "&token=" + access_token;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 이미지 삭제를 위한 메소드 추가
    public void deleteFile(Long id, String imagePath, String fileName) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
            String name = FilenameUtils.getBaseName(imagePath) + "-" + fileName + id
                    + "." + FilenameUtils.getExtension(imagePath);
            Blob blob = bucket.get(name);
            if (blob != null) {
                blob.delete();
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}