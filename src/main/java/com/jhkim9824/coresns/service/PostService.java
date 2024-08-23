package com.jhkim9824.coresns.service;

import com.jhkim9824.coresns.dto.PostDto;
import com.jhkim9824.coresns.entity.Post;
import com.jhkim9824.coresns.entity.User;
import com.jhkim9824.coresns.repository.PostRepository;
import com.jhkim9824.coresns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostDto createPost(PostDto postDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setUser(user);
        post.setContent(postDto.getContent());
        post.setLocation(postDto.getLocation());
        post.setMainImageUrl(postDto.getMainImageUrl());

        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }

    public PostDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return convertToDto(post);
    }

    @Transactional
    public PostDto updatePost(Long postId, PostDto postDto, String userEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You don't have permission to update this post");
        }

        post.setContent(postDto.getContent());
        post.setLocation(postDto.getLocation());
        post.setMainImageUrl(postDto.getMainImageUrl());

        Post updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);
    }

    @Transactional
    public void deletePost(Long postId, String userEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("You don't have permission to delete this post");
        }

        postRepository.delete(post);
    }

    // 게시물 조회 시 댓글 수 업데이트

    @Transactional
    public void updateCommentCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setCommentCount(post.getComments().size());
        postRepository.save(post);
    }

    private PostDto convertToDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getUser().getId(),
                post.getContent(),
                post.getLocation(),
                post.getCreatedDate(),
                post.getUpdatedDate(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getMainImageUrl()
        );
    }
}
