package com.example.job_mountain.resume.service;

import com.example.job_mountain.file.FileService;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

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
            if (file != null && !file.isEmpty()) {
                String file2 = fileService.saveFile(resume.getResumeId(), file, "resume");
                resume.setFile(file2);

                resumeRepository.save(resume);
                return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_SAVE_OK);
            } else {
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
            if (resume.getFile() != null && !resume.getFile().isEmpty()) {
                fileService.deleteFile(resume.getResumeId(), resume.getFile(), "resume");
            }

            // 새로운 파일 저장
            String file2 = fileService.saveFile(resume.getResumeId(), file, "resume");
            resume.setFile(file2);
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
        if (resume.getFile() != null && !resume.getFile().isEmpty()) {
            fileService.deleteFile(resume.getResumeId(), resume.getFile(), "resume");
        }

        resumeRepository.delete(resume);
        return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_DELETE_OK);
    }

    public Object findAllResumes() {
        List<Resume> all = resumeRepository.findAllByOrderByCreatedAtDesc();
        return new ResumeDto.ResumeListResponse(ExceptionCode.RESUME_GET_OK, all);
    }

    // 비디오 이력서 호출
    public Object getResume(Long id) {
        Optional<Resume> optionalResume = resumeRepository.findById(id);
        if (optionalResume.isPresent()) {
            return optionalResume.get();
        } else {
            return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_NOT_FOUND);
        }
    }

    // 조회시-조회수 증가
    public Object getView(Long id) {
        Optional<Resume> optionalResume = resumeRepository.findById(id);
        if (optionalResume.isPresent()) {
            Resume resume = optionalResume.get();
            resume.setView(resume.getView() + 1);
            this.resumeRepository.save(resume);
            return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_GET_OK, resume);
        } else {
            return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_NOT_FOUND);
        }
    }

    // 좋아요 증가
    public Object addNumLikes(Long id) {
        Optional<Resume> optionalResume = resumeRepository.findById(id);
        if (optionalResume.isPresent()) {
            Resume resume = optionalResume.get();
            resume.setNumLikes(resume.getNumLikes() + 1);
            this.resumeRepository.save(resume);
            return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_NUMLIKES_OK);
        } else {
            return new ResumeDto.ResumeResponse(ExceptionCode.RESUME_NOT_FOUND);
        }
    }
    // 좋아요기준 Top5 Resume
    public Object getTop5ResumesByNumLikes(){
        List<Resume> Top5Resumes = resumeRepository.findTop5ByOrderByNumLikesDesc();
        return new ResumeDto.ResumeListResponse(ExceptionCode.RESUME_GET_OK, Top5Resumes);
    }

}
