package com.example.job_mountain.file;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${app.auth.firebase-bucket}")
    private String firebaseBucket;

    private static final String downPath_front = "https://firebasestorage.googleapis.com/v0/b/jobdongsani-bfeeb.appspot.com/o/";
    private static final String downPath_back = "?alt=media";
    private static final Integer STATUS = 1;


    public String uploadFiles(MultipartFile file, String name) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        InputStream content = file.getInputStream();
        Blob blob = bucket.create(name, content, file.getContentType());
        return blob.getMediaLink();
    }

    public String saveFile(Long id, MultipartFile multipartFile, String fileName) {
        try {
            String name = FilenameUtils.getBaseName(multipartFile.getOriginalFilename()) + "-" + fileName + id
                    + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            String link = uploadFiles(multipartFile, name);
            return downPath_front + name + downPath_back;
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