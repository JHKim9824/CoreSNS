package com.jhkim9824.coresns.repository;

import com.jhkim9824.coresns.entity.User;
import com.jhkim9824.coresns.entity.Post;
import com.jhkim9824.coresns.entity.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {
    Optional<UserPostLike> findByUserAndPost(User user, Post post);

    boolean existsByUserAndPost(User user, Post post);

    void deleteByUserAndPost(User user, Post post);

    int countByPost(Post post);
}
