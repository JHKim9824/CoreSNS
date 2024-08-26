package com.jhkim9824.coresns.controller;

import com.jhkim9824.coresns.dto.PostDto;
import com.jhkim9824.coresns.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        PostDto createdPost = postService.createPost(postDto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        PostDto post = postService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId,
                                              @Valid @RequestBody PostDto postDto,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        PostDto updatedPost = postService.updatePost(postId, postDto, userDetails.getUsername());
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(postId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        postService.likePost(postId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        postService.unlikePost(postId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<Boolean> hasUserLikedPost(@PathVariable Long postId,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        boolean hasLiked = postService.hasUserLikedPost(postId, userDetails.getUsername());
        return ResponseEntity.ok(hasLiked);
    }



    @GetMapping("/hashtag/{hashtag}")
    public ResponseEntity<?> getPostsByHashtag(@PathVariable String hashtag) {
        if (!isValidHashtag(hashtag)) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    "The hashtag should only contain alphanumeric characters and underscores.");
            problemDetail.setTitle("Invalid hashtag format");
            return ResponseEntity.badRequest().body(problemDetail);
        }
        List<PostDto> posts = postService.getPostsByHashtag(hashtag);
        return ResponseEntity.ok(posts);
    }

    private boolean isValidHashtag(String hashtag) {
        return hashtag.matches("^[a-zA-Z0-9_-]+$");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
