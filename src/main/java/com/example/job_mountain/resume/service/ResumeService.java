package com.example.job_mountain.resume.service;

import com.example.job_mountain.job.domain.Job;
import com.example.job_mountain.job.repository.JobRepository;
import com.example.job_mountain.resume.domain.Resume;
import com.example.job_mountain.resume.dto.ResumeDto;
import com.example.job_mountain.resume.repository.ResumeRepository;
import com.example.job_mountain.security.UserPrincipal;
import com.example.job_mountain.user.domain.SiteUser;
import com.example.job_mountain.user.repository.UserRepository;
import com.example.job_mountain.validation.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // 이력서 저장
    public Object createResume(UserPrincipal userPrincipal, Long jobId, ResumeDto.CreateResume createResume, MultipartFile file) {
        // 이력서 생성
        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();
        Job job = jobRepository.findByJobId(jobId).get();

        // 제목, 파일 둘다 있어야 이력서를 저장함
        if (!createResume.getTitle().isEmpty() && (file != null && !file.isEmpty())) {

            // 파일 저장 후 이력서 생성
            Resume resume = Resume.builder()
                    .createResume(createResume)
                    .job(job)
                    .user(user)
                    .build();

            // 파일 저장 로직 추가
            try {
                String filename = file.getOriginalFilename();
                String uploadDir = "video";

                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                Path filePath = Paths.get(uploadDir, filename);
                Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE);
                resume.setFile(filePath.toString());

                resumeRepository.save(resume);
                return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_SAVE_OK);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResumeDto.ResumeResponse(ExceptionCode.FILE_STORAGE_ERROR);
            }

        } else if (createResume.getTitle().isEmpty()) { // 제목 없을 때 반환하는 로직
            return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_SAVE_FAIL1);
        }
        else { // 제목은 있으나 파일이 없을 때 반환하는 로직
            return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_SAVE_FAIL2);
        }
    }

    // 이력서 수정
    public Object updateResume(UserPrincipal userPrincipal, Long resumeId, ResumeDto.CreateResume updateResume, MultipartFile file) {
        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();
        Optional<Resume> findResume = resumeRepository.findByResumeId(resumeId);

        if (findResume.isEmpty()) {
            return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_NOT_FOUND);
        }

        Resume resume = findResume.get();
        if (!resume.getSiteUser().equals(user)) {
            return new ResumeDto.ResumeResponse(ExceptionCode.INVALID_USER);
        }

        // 이력서 제목 수정
        resume.setTitle(updateResume.getTitle());

        // 이력서 파일 수정
        if (file != null && !file.isEmpty()) {

            // 기존 파일 삭제
            if (findResume.get().getFile() != null && !findResume.get().getFile().isEmpty()) {
                try {
                    Path filePath = Paths.get(findResume.get().getFile());
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResumeDto.ResumeResponse(ExceptionCode.FILE_DELETE_ERROR);
                }
            }

            // 새로운 파일 저장
            String filename = file.getOriginalFilename();
            String uploadDir = "video";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Path filePath = Paths.get(uploadDir, filename);
            try {
                Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE);
                resume.setFile(filePath.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return new ResumeDto.ResumeResponse(ExceptionCode.FILE_STORAGE_ERROR);
            }
        }

        resumeRepository.save(resume);
        return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_UPDATE_OK);
    }


    // 이력서 삭제
    public Object deleteResume(UserPrincipal userPrincipal, Long resumeId) {
        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();

        Optional<Resume> findResume = resumeRepository.findByResumeId(resumeId);
        if (findResume.isEmpty()) {
            return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_NOT_FOUND);
        }
        Resume resume = findResume.get();

        if (!resume.getSiteUser().equals(user)) {
            return new ResumeDto.ResumeResponse(ExceptionCode.INVALID_USER);
        }

        // 파일 삭제
        if (findResume.get().getFile() != null && !findResume.get().getFile().isEmpty()) {
            try {
                Path filePath = Paths.get(findResume.get().getFile());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResumeDto.ResumeResponse(ExceptionCode.FILE_DELETE_ERROR);
            }
        }

        resumeRepository.delete(resume);
        return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_DELETE_OK);
    }

    public Object findAllResumes() {
        List<Resume> all = resumeRepository.findAllByOrderByCreatedAtDesc();
        return new ResumeDto.ResumeListResponse(ExceptionCode.RESUME_GET_OK, all);
    }

    public Object getResume(Long id) {
        Optional<Resume> optionalResume = resumeRepository.findById(id);
        if (optionalResume.isPresent()) {
            return optionalResume.get();
        } else {
            throw new IllegalArgumentException("Resume not found with id: " + id);
        }
    }

    // 조회수
    public Object getView(Long id) {
        Optional<Resume> optionalResume = resumeRepository.findById(id);
        if (optionalResume.isPresent()) {
            Resume resume = optionalResume.get();
            resume.setView(resume.getView() + 1);
            this.resumeRepository.save(resume);
            return resume;
        } else {
            throw new IllegalArgumentException("Resume not found");
        }
    }

}
