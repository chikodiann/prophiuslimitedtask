package com.ann.prophiuslimitedtask.repository;

import com.ann.prophiuslimitedtask.entity.Comment;
import com.ann.prophiuslimitedtask.entity.Post;
import com.ann.prophiuslimitedtask.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    void deleteByEmail(String email);

    List<User> findUsersByFollowerUsers(User user, Pageable pageable);

    List<User> findUsersByLikedPosts(Post post, Pageable pageable);
    List<User> findUsersByLikedComments(Comment comment, Pageable pageable);
    List<User> findUserByUsername(String name, Pageable pageable);

    Optional<User> findByEmail(String email);

    List<User> findUsersByFollowingUsers(User user, Pageable pageable);
}
