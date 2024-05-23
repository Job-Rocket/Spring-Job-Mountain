package com.example.job_mountain.shorts.service;

import com.example.job_mountain.file.FileService;
import com.example.job_mountain.job.domain.Job;
import com.example.job_mountain.job.repository.JobRepository;
import com.example.job_mountain.security.UserPrincipal;
import com.example.job_mountain.shorts.domain.Shorts;
import com.example.job_mountain.shorts.dto.ShortsDto;
import com.example.job_mountain.shorts.repository.ShortsRepository;
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
public class ShortsService {

    private final ShortsRepository shortsRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    // 이력서 저장
    public Object createShorts(UserPrincipal userPrincipal, Long jobId, ShortsDto.CreateShorts createShorts, MultipartFile file) {
        // 이력서 생성
        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();
        Job job = jobRepository.findByJobId(jobId).get();

        // 제목, 파일 둘다 있어야 이력서를 저장함
        if (!createShorts.getTitle().isEmpty() && (file != null && !file.isEmpty())) {

            // 파일 저장 후 이력서 생성
            Shorts shorts = Shorts.builder()
                    .createShorts(createShorts)
                    .job(job)
                    .user(user)
                    .build();

            // 파일 저장 로직 추가
            if (file != null && !file.isEmpty()) {
                String file2 = fileService.saveFile(shorts.getShortsId(), file, "shorts");
                shorts.setFile(file2);

                shortsRepository.save(shorts);
                return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_SAVE_OK);
            } else {
                return new ShortsDto.ShortsResponse(ExceptionCode.FILE_STORAGE_ERROR);
            }

        } else if (createShorts.getTitle().isEmpty()) { // 제목 없을 때 반환하는 로직
            return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_SAVE_FAIL1);
        }
        else { // 제목은 있으나 파일이 없을 때 반환하는 로직
            return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_SAVE_FAIL2);
        }
    }

    // 이력서 수정
    public Object updateShorts(UserPrincipal userPrincipal, Long shortsId, ShortsDto.CreateShorts updateShorts, MultipartFile file) {
        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();
        Optional<Shorts> findShorts = shortsRepository.findByShortsId(shortsId);

        if (findShorts.isEmpty()) {
            return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_NOT_FOUND);
        }
        Shorts shorts = findShorts.get();

        if (!shorts.getSiteUser().equals(user)) {
            return new ShortsDto.ShortsResponse(ExceptionCode.INVALID_USER);
        }

        // 이력서 제목 수정
        shorts.setTitle(updateShorts.getTitle());

        // 이력서 파일 수정
        if (file != null && !file.isEmpty()) {

            // 기존 파일 삭제
            if (shorts.getFile() != null && !shorts.getFile().isEmpty()) {
                fileService.deleteFile(shorts.getShortsId(), shorts.getFile(), "shorts");
            }

            // 새로운 파일 저장
            String file2 = fileService.saveFile(shorts.getShortsId(), file, "shorts");
            shorts.setFile(file2);
        }

        shortsRepository.save(shorts);
        return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_UPDATE_OK);
    }


    // 이력서 삭제
    public Object deleteShorts(UserPrincipal userPrincipal, Long shortsId) {
        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();

        Optional<Shorts> findShorts = shortsRepository.findByShortsId(shortsId);
        if (findShorts.isEmpty()) {
            return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_NOT_FOUND);
        }
        Shorts shorts = findShorts.get();

        if (!shorts.getSiteUser().equals(user)) {
            return new ShortsDto.ShortsResponse(ExceptionCode.INVALID_USER);
        }

        // 파일 삭제
        if (shorts.getFile() != null && !shorts.getFile().isEmpty()) {
            fileService.deleteFile(shorts.getShortsId(), shorts.getFile(), "shorts");
        }

        shortsRepository.delete(shorts);
        return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_DELETE_OK);
    }

    public Object findAllShorts() {
        List<Shorts> all = shortsRepository.findAllByOrderByCreatedAtDesc();
        return new ShortsDto.ShortsListResponse(ExceptionCode.RESUME_GET_OK, all);
    }

    // 비디오 이력서 호출
    public Object getShorts(Long id) {
        Optional<Shorts> optionalShorts = shortsRepository.findById(id);
        if (optionalShorts.isPresent()) {
            return optionalShorts.get();
        } else {
            return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_NOT_FOUND);
        }
    }

    // 조회시-조회수 증가
    public Object getView(Long id) {
        Optional<Shorts> optionalShorts = shortsRepository.findById(id);
        if (optionalShorts.isPresent()) {
            Shorts shorts = optionalShorts.get();
            shorts.setView(shorts.getView() + 1);
            this.shortsRepository.save(shorts);
            return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_GET_OK, shorts);
        } else {
            return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_NOT_FOUND);
        }
    }

    // 좋아요 증가
    public Object addNumLikes(Long id) {
        Optional<Shorts> optionalShorts = shortsRepository.findById(id);
        if (optionalShorts.isPresent()) {
            Shorts shorts = optionalShorts.get();
            shorts.setNumLikes(shorts.getNumLikes() + 1);
            this.shortsRepository.save(shorts);
            return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_NUMLIKES_OK);
        } else {
            return new ShortsDto.ShortsResponse(ExceptionCode.RESUME_NOT_FOUND);
        }
    }

    // 좋아요기준 Top5 Shorts
    public Object getTop5ShortsByNumLikes(){
        List<Shorts> Top5Shorts = shortsRepository.findTop5ByOrderByNumLikesDesc();
        return new ShortsDto.ShortsListResponse(ExceptionCode.RESUME_GET_OK, Top5Shorts);
    }

}
