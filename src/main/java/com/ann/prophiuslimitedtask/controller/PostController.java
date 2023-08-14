package com.ann.prophiuslimitedtask.controller;

import com.ann.prophiuslimitedtask.entity.Comment;
import com.ann.prophiuslimitedtask.entity.Post;
import com.ann.prophiuslimitedtask.entity.User;
import com.ann.prophiuslimitedtask.exception.EmptyPostException;
import com.ann.prophiuslimitedtask.response.CommentResponse;
import com.ann.prophiuslimitedtask.response.PostResponse;
import com.ann.prophiuslimitedtask.service.CommentService;
import com.ann.prophiuslimitedtask.service.PostService;
import com.ann.prophiuslimitedtask.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/* This class manages operations related to posts and comments.
It provides endpoints for creating, updating, deleting posts, managing post likes, and handling comments on posts.
 */

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    // Create a new post
    @PostMapping("/create")
    public ResponseEntity<?> createNewPost(
            @RequestParam(value = "content", required = false) Optional<String> content,
            @RequestParam(name = "postPhoto", required = false) Optional<MultipartFile> postPhoto) {
        // Check for empty content and postPhoto
        if (content.isEmpty() && postPhoto.isEmpty()) {
            throw new EmptyPostException();
        }
        ObjectMapper mapper = new ObjectMapper();

        String contentToAdd = content.orElse(null);
        MultipartFile postPhotoToAdd = postPhoto.orElse(null);

        // Create a new post with provided content and photo
        Post createdPost = postService.createNewPost(contentToAdd, postPhotoToAdd);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/update")
    public ResponseEntity<?> updatePost(
            @PathVariable("postId") Long postId,
            @RequestParam(value = "content", required = false) Optional<String> content,
            @RequestParam(name = "postPhoto", required = false) Optional<MultipartFile> postPhoto) throws JsonProcessingException {
        if (content.isEmpty() && postPhoto.isEmpty()) {
            throw new EmptyPostException();
        }

        ObjectMapper mapper = new ObjectMapper();

        String contentToAdd = content.orElse(null);
        MultipartFile postImageToAdd = postPhoto.orElse(null);

        Post updatePost = postService.updatePost(postId, contentToAdd, postImageToAdd);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{postId}/photo/delete")
    public ResponseEntity<?> deletePostPhoto(@PathVariable("postId") Long postId) {
        postService.deletePostPhoto(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable("postId") Long postId) {
        PostResponse foundPostResponse = postService.getPostResponseById(postId);
        return new ResponseEntity<>(foundPostResponse, HttpStatus.OK);
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<?> getPostLikes(@PathVariable("postId") Long postId,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Post targetPost = postService.getPostById(postId);
        List<User> postLikerList = userService.getLikesByPostPaginate(targetPost, page, size);
        return new ResponseEntity<>(postLikerList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable("postId") Long postId) {
        postService.likePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable("postId") Long postId) {
        postService.unlikePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getPostComments(@PathVariable("postId") Long postId,
                                             @RequestParam("page") Integer page,
                                             @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Post targetPost = postService.getPostById(postId);
        List<CommentResponse> postCommentResponseList = commentService.getPostCommentsPaginate(targetPost, page, size);
        return new ResponseEntity<>(postCommentResponseList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/comments/create")
    public ResponseEntity<?> createPostComment(@PathVariable("postId") Long postId,
                                               @RequestParam(value = "content") String content) {
        Comment savedComment = postService.createPostComment(postId, content);
        CommentResponse commentResponse = CommentResponse.builder()
                .comment(savedComment)
                .likedByAuthUser(false)
                .build();
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PostMapping("/{postId}/comments/{commentId}/update")
    public ResponseEntity<?> updatePostComment(@PathVariable("commentId") Long commentId,
                                               @PathVariable("postId") Long postId,
                                               @RequestParam(value = "content") String content) {
        Comment savedComment = postService.updatePostComment(commentId, postId, content);
        return new ResponseEntity<>(savedComment, HttpStatus.OK);
    }

    @PostMapping("/{postId}/comments/{commentId}/delete")
    public ResponseEntity<?> deletePostComment(@PathVariable("commentId") Long commentId,
                                               @PathVariable("postId") Long postId) {
        postService.deletePostComment(commentId, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<?> likePostComment(@PathVariable("commentId") Long commentId) {
        commentService.likeComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/comments/{commentId}/unlike")
    public ResponseEntity<?> unlikePostComment(@PathVariable("commentId") Long commentId) {
        commentService.unlikeComment(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}/likes")
    public ResponseEntity<?> getCommentLikeList(@PathVariable("commentId") Long commentId,
                                                @RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        Comment targetComment = commentService.getCommentById(commentId);
        List<User> commentLikes = userService.getLikesByCommentPaginate(targetComment, page, size);
        return new ResponseEntity<>(commentLikes, HttpStatus.OK);
    }
}
