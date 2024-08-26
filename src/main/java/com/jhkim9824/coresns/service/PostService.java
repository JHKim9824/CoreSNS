package com.jhkim9824.coresns.service;

import com.jhkim9824.coresns.dto.PostDto;
import com.jhkim9824.coresns.entity.*;
import com.jhkim9824.coresns.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserPostLikeRepository userPostLikeRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;

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

        // 해시태그 처리
        Set<String> hashtags = extractHashtags(postDto.getContent());
        for (String tag : hashtags) {
            Hashtag hashtag = hashtagRepository.findByHashtag(tag)
                    .orElseGet(() -> {
                        Hashtag newHashtag = new Hashtag();
                        newHashtag.setHashtag(tag);
                        return hashtagRepository.save(newHashtag);
                    });

            PostHashtag postHashtag = new PostHashtag();
            postHashtag.setPost(savedPost);
            postHashtag.setHashtag(hashtag);
            postHashtagRepository.save(postHashtag);
        }
        
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

    @Transactional
    public void likePost(Long postId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (userPostLikeRepository.existsByUserAndPost(user, post)) {
            throw new RuntimeException("User Already Liked Post");
        }

        UserPostLike like = UserPostLike.builder()
                .user(user)
                .post(post)
                .build();

        userPostLikeRepository.save(like);

        post.incrementLikeCount();
    }

    @Transactional
    public void unlikePost(Long postId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        UserPostLike like = userPostLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("UserPostLike not found"));

        userPostLikeRepository.delete(like);

        post.decrementLikeCount();
    }

    public boolean hasUserLikedPost(Long postId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return userPostLikeRepository.existsByUserAndPost(user, post);
    }

    public List<PostDto> getPostsByHashtag(String hashtag) {
        List<Post> posts = postHashtagRepository.findPostsByHashtag(hashtag);
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private Set<String> extractHashtags(String content) {
        Set<String> hashtags = new HashSet<>();
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            hashtags.add(matcher.group(1));
        }
        return hashtags;
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
