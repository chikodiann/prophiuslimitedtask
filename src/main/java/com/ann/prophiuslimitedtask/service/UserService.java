package com.ann.prophiuslimitedtask.service;

import com.ann.prophiuslimitedtask.DTO.*;
import com.ann.prophiuslimitedtask.entity.Comment;
import com.ann.prophiuslimitedtask.entity.Post;
import com.ann.prophiuslimitedtask.entity.User;
import com.ann.prophiuslimitedtask.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User getUserById(Long userId);
    User getUserByEmail(String email);
    List<UserResponse> getFollowerUsersPaginate(Long userId, Integer page, Integer size);
    List<UserResponse> getFollowingUsersPaginate(Long userId, Integer page, Integer size);
    User createNewUser(SignupDto signupDto);
    User updateUserInfo(UpdateUserInfoDto updateUserInfoDto);
    User updateEmail(UpdateEmailDto updateEmailDto);
    User updatePassword(UpdatePasswordDto updatePasswordDto);
    User updateProfilePhoto(MultipartFile photo);
    User verifyEmail(String token);
    void forgotPassword(String email);
    User resetPassword(String token, ResetPasswordDto resetPasswordDto);
    void deleteUserAccount();
    void followUser(Long userId);
    void unfollowUser(Long userId);
    User getAuthenticatedUser();
    List<UserResponse> getUserSearchResult(String key, Integer page, Integer size);
    List<User> getLikesByPostPaginate(Post post, Integer page, Integer size);
    List<User> getLikesByCommentPaginate(Comment comment, Integer page, Integer size);

}
