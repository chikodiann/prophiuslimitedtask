package com.ann.prophiuslimitedtask.repository;

import com.ann.prophiuslimitedtask.entity.Post;
import com.ann.prophiuslimitedtask.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostsByAuthor(User author, Pageable pageable);
    List<Post> findPostsByAuthorIdIn(List<Long> followingUserIds, Pageable pageable);

}
