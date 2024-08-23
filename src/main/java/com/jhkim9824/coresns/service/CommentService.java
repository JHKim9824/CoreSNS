package com.jhkim9824.coresns.service;

import com.jhkim9824.coresns.dto.CommentDto;
import com.jhkim9824.coresns.entity.Comment;
import com.jhkim9824.coresns.entity.Post;
import com.jhkim9824.coresns.entity.User;
import com.jhkim9824.coresns.repository.CommentRepository;
import com.jhkim9824.coresns.repository.PostRepository;
import com.jhkim9824.coresns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostService postService;

    @Transactional
    public CommentDto createComment(CommentDto commentDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentDto.getContent());

        Comment savedComment = commentRepository.save(comment);

        postService.updateCommentCount(post.getId());

        return convertToDto(savedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, String userEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!comment.getUser().equals(user)) {
            throw new RuntimeException("You don't have permission to delete this comment");
        }

        Post post = comment.getPost();
        post.removeComment(comment);
    }

    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedDateDesc(postId);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentDto convertToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getContent(),
                comment.getCreatedDate(),
                comment.getUpdatedDate()
        );
    }
}
