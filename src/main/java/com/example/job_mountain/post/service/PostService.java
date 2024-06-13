package com.example.job_mountain.post.service;

import com.example.job_mountain.post.domain.Post;
import com.example.job_mountain.post.dto.PostDto;
import com.example.job_mountain.post.repository.PostRepository;
import com.example.job_mountain.security.UserPrincipal;
import com.example.job_mountain.user.domain.SiteUser;
import com.example.job_mountain.user.repository.UserRepository;
import com.example.job_mountain.validation.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 취준생/현직자 게시글 저장
    public Object createPost(UserPrincipal userPrincipal, PostDto.CreatePost createPost) {

        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();

        // 제목, 내용 둘다 있어야 게시글을 저장함
        if (!createPost.getTitle().isEmpty() && !createPost.getContent().isEmpty()) {

            Post post = Post.builder()
                    .createPost(createPost)
                    .user(user)
                    .build();

            return new PostDto.PostResponse(ExceptionCode.POST_SAVE_OK);

        } else { // 제목 또는 내용이 없을 때 반환하는 로직
            return new PostDto.PostResponse(ExceptionCode.POST_SAVE_FAIL);
        }
    }

    // 취준생/현직자 게시글 수정
    public Object updatePost(UserPrincipal userPrincipal, Long postId, PostDto.CreatePost createPost) {

        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();
        Optional<Post> findPost = postRepository.findByPostId(postId);
        if (findPost.isEmpty()) {
            return new PostDto.PostResponse(ExceptionCode.POST_NOT_FOUND);
        }
        Post post = findPost.get();

        if (!post.getSiteUser().equals(user)) {
            return new PostDto.PostResponse(ExceptionCode.INVALID_USER);
        }

        if (!createPost.getTitle().isEmpty() && !createPost.getContent().isEmpty()) {
            post.updatePost(createPost);
            postRepository.save(post);
            return new PostDto.PostResponse(ExceptionCode.POST_UPDATE_OK);
        } else {
            return new PostDto.PostResponse(ExceptionCode.FILE_NOT_FOUND);
        }
    }

    // 취준생/현직자 게시글 삭제
    public Object deletePost(UserPrincipal userPrincipal, Long postId) {
        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();

        Optional<Post> findPost = postRepository.findByPostId(postId);
        if (findPost.isEmpty()) {
            return new PostDto.PostResponse(ExceptionCode.POST_NOT_FOUND);
        }
        Post post = findPost.get();

        if (!post.getSiteUser().equals(user)) {
            return new PostDto.PostResponse(ExceptionCode.INVALID_USER);
        }
        postRepository.delete(post);
        return new PostDto.PostResponse(ExceptionCode.POST_DELETE_OK);
    }

    // 전체 게시글 불러오기
    public Object findAllPosts() {
        List<Post> all = postRepository.findAllByOrderByCreatedAtDesc();
        return new PostDto.PostListResponse(ExceptionCode.POST_GET_OK, all);
    }

    // 조회시-조회수 증가
    public Object getView(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setView(post.getView() + 1);
            this.postRepository.save(post);
            return new PostDto.PostResponse(ExceptionCode.POST_GET_OK, post);
        } else {
            return new PostDto.PostResponse(ExceptionCode.POST_NOT_FOUND);
        }
    }

    // 좋아요 증가
    public Object addNumLikes(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setNumLikes(post.getNumLikes() + 1);
            this.postRepository.save(post);
            return new PostDto.PostResponse(ExceptionCode.POST_NUMLIKES_OK);
        } else {
            return new PostDto.PostResponse(ExceptionCode.POST_NOT_FOUND);
        }
    }

    // 좋아요기준 top6 게시글 불러오기
    public Object getTop6PostsByNumLikes(){
        List<Post> Top6Posts = postRepository.findTop6ByOrderByNumLikesDesc();
        return new PostDto.PostListResponse(ExceptionCode.POST_GET_OK, Top6Posts);
    }

}
