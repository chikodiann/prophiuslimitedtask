package com.ann.prophiuslimitedtask.serviceImplementation;

import com.ann.prophiuslimitedtask.entity.Comment;
import com.ann.prophiuslimitedtask.entity.Post;
import com.ann.prophiuslimitedtask.entity.User;
import com.ann.prophiuslimitedtask.exception.EmptyCommentException;
import com.ann.prophiuslimitedtask.exception.InvalidOperationException;
import com.ann.prophiuslimitedtask.exception.PostNotFoundException;
import com.ann.prophiuslimitedtask.repository.PostRepository;
import com.ann.prophiuslimitedtask.response.PostResponse;
import com.ann.prophiuslimitedtask.service.CommentService;
import com.ann.prophiuslimitedtask.service.PostService;
import com.ann.prophiuslimitedtask.service.UserService;
import com.ann.prophiuslimitedtask.util.FileNamingUtil;
import com.ann.prophiuslimitedtask.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final Environment environment;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    @Override
    public PostResponse getPostResponseById(Long postId) {
        User authUser = userService.getAuthenticatedUser();
        Post foundPost = getPostById(postId);
        return PostResponse.builder()
                .post(foundPost)
                .likedByAuthUser(foundPost.getLikeList().contains(authUser))
                .build();
    }

    @Override
    public List<PostResponse> getTimelinePostsPaginate(Integer page, Integer size) {
        User authUser = userService.getAuthenticatedUser();
        List<User> followingList = authUser.getFollowingUsers();
        followingList.add(authUser);
        List<Long> followingListIds = followingList.stream().map(User::getId).toList();
        return postRepository.findPostsByAuthorIdIn(
                        followingListIds,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(this::postToPostResponse).collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getPostsByUserPaginate(User author, Integer page, Integer size) {
        return postRepository.findPostsByAuthor(
                        author,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateCreated")))
                .stream().map(this::postToPostResponse).collect(Collectors.toList());
    }

    @Override
    public Post createNewPost(String content, MultipartFile postPhoto) {
        User authUser = userService.getAuthenticatedUser();
        Post newPost = new Post();
        newPost.setContent(content);
        newPost.setAuthor(authUser);
        newPost.setLikesCount(0L);
        newPost.setCommentsCount(0L);
        newPost.setDateCreated(new Date());

        if (postPhoto != null && postPhoto.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.post.images");
            String newPhotoName = fileNamingUtil.nameFile(postPhoto);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator + newPhotoName;
            newPost.setPostPhoto(newPhotoUrl);
            try {
                fileUploadUtil.saveNewFile(uploadDir, newPhotoName, postPhoto);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        return newPost;
    }

    @Override
    public Post updatePost(Long postId, String content, MultipartFile postPhoto) {
        Post targetPost = getPostById(postId);
        if (StringUtils.isNotEmpty(content)) {
            targetPost.setContent(content);
        }

        if (postPhoto != null && postPhoto.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.post.images");
            String oldPhotoName = getPhotoNameFromPhotoUrl(targetPost.getPostPhoto());
            String newPhotoName = fileNamingUtil.nameFile(postPhoto);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator + newPhotoName;
            targetPost.setPostPhoto(newPhotoUrl);
            try {
                if (oldPhotoName == null) {
                    fileUploadUtil.saveNewFile(uploadDir, newPhotoName, postPhoto);
                } else {
                    fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, postPhoto);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        return targetPost;
    }

    @Override
    public void deletePost(Long postId) {
        User authUser = userService.getAuthenticatedUser();
        Post targetPost = getPostById(postId);

        if (targetPost.getPostPhoto() != null) {
                String uploadDir = environment.getProperty("upload.post.images");
                String photoName = getPhotoNameFromPhotoUrl(targetPost.getPostPhoto());
                try {
                    fileUploadUtil.deleteFile(uploadDir, photoName);
                } catch (IOException ignored) {}
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void deletePostPhoto(Long postId) {
        User authUser = userService.getAuthenticatedUser();
        Post targetPost = getPostById(postId);

        if (targetPost.getAuthor().equals(authUser)) {
            if (targetPost.getPostPhoto() != null) {
                String uploadDir = environment.getProperty("upload.post.images");
                String photoName = getPhotoNameFromPhotoUrl(targetPost.getPostPhoto());
                try {
                    fileUploadUtil.deleteFile(uploadDir, photoName);
                } catch (IOException ignored) {}
            }

            targetPost.setPostPhoto(null);
            postRepository.save(targetPost);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void likePost(Long postId) {
        User authUser = userService.getAuthenticatedUser();
        Post targetPost = getPostById(postId);
        if (!targetPost.getLikeList().contains(authUser)) {
            targetPost.setLikesCount(targetPost.getLikesCount() + 1);
            targetPost.getLikeList().add(authUser);
            postRepository.save(targetPost);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void unlikePost(Long postId) {
        User authUser = userService.getAuthenticatedUser();
        Post targetPost = getPostById(postId);
        if (targetPost.getLikeList().contains(authUser)) {
            targetPost.setLikesCount(targetPost.getLikesCount()-1);
            targetPost.getLikeList().remove(authUser);
            postRepository.save(targetPost);

        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Comment createPostComment(Long postId, String content) {
        if (StringUtils.isEmpty(content)) throw new EmptyCommentException();

        User authUser = userService.getAuthenticatedUser();
        Post targetPost = getPostById(postId);
        Comment savedComment = commentService.createNewComment(content, targetPost);
        targetPost.setCommentsCount(targetPost.getCommentsCount()+1);
        postRepository.save(targetPost);

        return savedComment;
    }

    @Override
    public Comment updatePostComment(Long commentId, Long postId, String content) {
        if (StringUtils.isEmpty(content)) throw new EmptyCommentException();

        return commentService.updateComment(commentId, content);
    }

    @Override
    public void deletePostComment(Long commentId, Long postId) {
        Post targetPost = getPostById(postId);
        commentService.deleteComment(commentId);
        targetPost.setCommentsCount(targetPost.getCommentsCount()-1);
        postRepository.save(targetPost);
    }

    private String getPhotoNameFromPhotoUrl(String photoUrl) {
        if (photoUrl != null) {
            String stringToOmit = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.post.images") + File.separator;
            return photoUrl.substring(stringToOmit.length());
        } else {
            return null;
        }
    }

    private PostResponse postToPostResponse(Post post) {
        User authUser = userService.getAuthenticatedUser();
        return PostResponse.builder()
                .post(post)
                .likedByAuthUser(post.getLikeList().contains(authUser))
                .build();
    }
}
