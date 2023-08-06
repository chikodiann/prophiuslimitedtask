package com.ann.prophiuslimitedtask.serviceImplementation;

import com.ann.prophiuslimitedtask.entity.Comment;
import com.ann.prophiuslimitedtask.entity.Post;
import com.ann.prophiuslimitedtask.entity.User;
import com.ann.prophiuslimitedtask.exception.CommentNotFoundException;
import com.ann.prophiuslimitedtask.exception.InvalidOperationException;
import com.ann.prophiuslimitedtask.repository.CommentRepository;
import com.ann.prophiuslimitedtask.service.CommentService;
import com.ann.prophiuslimitedtask.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@AllArgsConstructor
public abstract class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

    @Override
    public Comment createNewComment(String content, Post post) {
        User authUser = userService.getAuthenticatedUser();
        Comment newComment = new Comment();
        newComment.setContent(content);
        newComment.setAuthor(authUser);
        newComment.setPost(post);
        newComment.setLikeCount(0);
        newComment.setCreationDate(new Date());
        return commentRepository.save(newComment);
    }

    @Override
    public Comment updateComment(Long commentId, String content) {
        User authUser = userService.getAuthenticatedUser();
        Comment targetComment = getCommentById(commentId);
        if (targetComment.getAuthor().equals(authUser)) {
            targetComment.setContent(content);
            return commentRepository.save(targetComment);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void deleteComment(Long commentId) {
        User authUser = userService.getAuthenticatedUser();
        Comment targetComment = getCommentById(commentId);
        if (targetComment.getAuthor().equals(authUser)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Comment likeComment(Long commentId) {
        User authUser = userService.getAuthenticatedUser();
        Comment targetComment = getCommentById(commentId);
        if (!targetComment.getLikeList().contains(authUser)) {
            targetComment.setLikeCount(targetComment.getLikeCount() + 1);
            targetComment.getLikeList().add(authUser);

            return targetComment;
        } else {
            throw new InvalidOperationException();
        }
    }
}
