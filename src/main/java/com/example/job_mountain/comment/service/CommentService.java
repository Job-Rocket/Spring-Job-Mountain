package com.example.job_mountain.comment.service;

import com.example.job_mountain.comment.domain.Comment;
import com.example.job_mountain.comment.dto.CommentDto;
import com.example.job_mountain.comment.repository.CommentRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 취준생/현직자 댓글 저장
    public Object createComment(UserPrincipal userPrincipal, Long postId, CommentDto.CreateComment createComment) {

        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();
        Optional<Post> findPost = postRepository.findByPostId(postId);
        if (findPost.isEmpty()) {
            return new PostDto.PostResponse(ExceptionCode.POST_NOT_FOUND);
        }

        // 내용이 있어야 댓글 저장 가능
        if (!createComment.getContent().isEmpty()) {
            Comment comment = Comment.builder()
                    .createComment(createComment)
                    .user(user)
                    .build();
            commentRepository.save(comment);
            return new CommentDto.CommentListResponse(ExceptionCode.COMMENT_SAVE_OK);
        } else { // 내용이 없을 때 반환하는 로직
            return new CommentDto.CommentResponse(ExceptionCode.COMMENT_SAVE_FAIL);
        }
    }


    // 취준생/현직자 댓글 수정
    public Object updateComment(UserPrincipal userPrincipal, Long postId, Long commentId, CommentDto.CreateComment createComment) {

        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();

        Optional<Post> findPost = postRepository.findByPostId(postId);
        if (findPost.isEmpty()) {
            return new PostDto.PostResponse(ExceptionCode.POST_NOT_FOUND);
        }

        Optional<Comment> findComment = commentRepository.findByCommentId(commentId);
        if (findComment.isEmpty()) {
            return new CommentDto.CommentResponse(ExceptionCode.COMMENT_NOT_FOUND);
        }
        Comment comment = findComment.get();

        if (!comment.getSiteUser().equals(user)) {
            return new CommentDto.CommentResponse(ExceptionCode.INVALID_USER);
        }

        if (!createComment.getContent().isEmpty()) {
            comment.updateComment(createComment);
            commentRepository.save(comment);
            return new CommentDto.CommentListResponse(ExceptionCode.COMMENT_UPDATE_OK);
        } else {
            return new CommentDto.CommentResponse(ExceptionCode.COMMENT_SAVE_FAIL);
        }
    }

    // 취준생/현직자 댓글 삭제
    public Object deleteComment(UserPrincipal userPrincipal, Long postId, Long commentId) {
        SiteUser user = userRepository.findByUserId(userPrincipal.getUserId()).get();

        Optional<Post> findPost = postRepository.findByPostId(postId);
        if (findPost.isEmpty()) {
            return new PostDto.PostResponse(ExceptionCode.POST_NOT_FOUND);
        }

        Optional<Comment> findComment = commentRepository.findByCommentId(commentId);
        if (findComment.isEmpty()) {
            return new CommentDto.CommentResponse(ExceptionCode.COMMENT_NOT_FOUND);
        }
        Comment comment = findComment.get();

        if (!comment.getSiteUser().equals(user)) {
            return new CommentDto.CommentResponse(ExceptionCode.INVALID_USER);
        }
        commentRepository.delete(comment);
        return new CommentDto.CommentListResponse(ExceptionCode.COMMENT_DELETE_OK);
    }

    // 댓글 좋아요 증가
    public Object addNumLikes(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setNumLikes(comment.getNumLikes() + 1);
            this.commentRepository.save(comment);
            return new CommentDto.CommentResponse(ExceptionCode.COMMENT_NUMLIKES_OK);
        } else {
            return new CommentDto.CommentResponse(ExceptionCode.COMMENT_NOT_FOUND);
        }
    }

    // 특정 게시물에 대한 댓글 반환
    public Object findCommentsByPostId(Long postId) {
        Optional<Post> findPost = postRepository.findByPostId(postId);
        if (findPost.isEmpty()) {
            return new PostDto.PostResponse(ExceptionCode.POST_NOT_FOUND);
        }

        List<Comment> comments = commentRepository.findAllByPostPostId(postId);
        return new CommentDto.CommentListResponse(ExceptionCode.COMMENT_GET_OK, comments);
    }
}
