package com.ann.prophiuslimitedtask.repository;

import com.ann.prophiuslimitedtask.entity.Comment;
import com.ann.prophiuslimitedtask.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post, Pageable pageable);
}
