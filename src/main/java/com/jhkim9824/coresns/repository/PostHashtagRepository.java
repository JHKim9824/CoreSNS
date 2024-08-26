package com.jhkim9824.coresns.repository;

import com.jhkim9824.coresns.entity.Post;
import com.jhkim9824.coresns.entity.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    List<PostHashtag> findByPost(Post post);

    @Query("SELECT ph.post FROM PostHashtag ph WHERE ph.hashtag.hashtag = :hashtag")
    List<Post> findPostsByHashtag(String hashtag);
}
