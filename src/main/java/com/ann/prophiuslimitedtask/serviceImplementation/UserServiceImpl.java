package com.ann.prophiuslimitedtask.serviceImplementation;

import com.ann.prophiuslimitedtask.DTO.*;
import com.ann.prophiuslimitedtask.common.AppConstants;
import com.ann.prophiuslimitedtask.common.UserPrincipal;
import com.ann.prophiuslimitedtask.entity.Comment;
import com.ann.prophiuslimitedtask.entity.Post;
import com.ann.prophiuslimitedtask.entity.User;
import com.ann.prophiuslimitedtask.enums.Role;
import com.ann.prophiuslimitedtask.exception.EmailExistsException;
import com.ann.prophiuslimitedtask.exception.InvalidOperationException;
import com.ann.prophiuslimitedtask.exception.SameEmailUpdateException;
import com.ann.prophiuslimitedtask.exception.UserNotFoundException;
import com.ann.prophiuslimitedtask.mapper.MapStructMapper;
import com.ann.prophiuslimitedtask.mapper.MapstructMapperUpdate;
import com.ann.prophiuslimitedtask.repository.UserRepository;
import com.ann.prophiuslimitedtask.response.UserResponse;
import com.ann.prophiuslimitedtask.service.EmailService;
import com.ann.prophiuslimitedtask.service.JwtTokenService;
import com.ann.prophiuslimitedtask.service.UserService;
import com.ann.prophiuslimitedtask.util.FileNamingUtil;
import com.ann.prophiuslimitedtask.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final MapStructMapper mapStructMapper;
    private final MapstructMapperUpdate mapstructMapperUpdate;
    private final Environment environment;
    private final FileNamingUtil fileNamingUtil;
    private final FileUploadUtil fileUploadUtil;



    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No user exists with this email."));
    }


    @Override
    public List<UserResponse> getFollowerUsersPaginate(Long userId, Integer page, Integer size) {
        User targetUser = getUserById(userId);
        return userRepository.findUsersByFollowingUsers(targetUser,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName")))
                .stream().map(this::userToUserResponse).collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getFollowingUsersPaginate(Long userId, Integer page, Integer size) {
        User targetUser = getUserById(userId);
        return userRepository.findUsersByFollowerUsers(targetUser,
                        PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName")))
                .stream().map(this::userToUserResponse).collect(Collectors.toList());
    }

    @Override
    public User createNewUser(SignupDto signupDto) {
        try {
            User user = getUserByEmail(signupDto.getEmail());
            if (user != null) {
                throw new EmailExistsException();
            }
        } catch (UserNotFoundException e) {
            User newUser = new User();
            newUser.setEmail(signupDto.getEmail());
            newUser.setPassword(passwordEncoder.encode(signupDto.getPassword()));
            newUser.setUsername(signupDto.getUsername());
            newUser.setFollowerCount(0);
            newUser.setFollowingCount(0);
            newUser.setEnabled(true);
            newUser.setRole(Role.USER.name());
            User savedUser = userRepository.save(newUser);
            UserPrincipal userPrincipal = new UserPrincipal(savedUser);
            String emailVerifyMail =
                    emailService.buildEmailVerifyMail(jwtTokenService.generateToken(userPrincipal));
            emailService.send(savedUser.getEmail(), AppConstants.VERIFY_EMAIL, emailVerifyMail);
            return savedUser;
        }
        return null;
    }

    @Override
    public User updateUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        User authUser = getAuthenticatedUser();

        mapstructMapperUpdate.updateUserFromUserUpdateDto(updateUserInfoDto, authUser);
        return userRepository.save(authUser);
    }

    @Override
    public User updateEmail(UpdateEmailDto updateEmailDto) {
        User authUser = getAuthenticatedUser();
        String newEmail = updateEmailDto.getEmail();
        String password = updateEmailDto.getPassword();

        if (!newEmail.equalsIgnoreCase(authUser.getEmail())) {
            try {
                User duplicateUser = getUserByEmail(newEmail);
                if (duplicateUser != null) {
                    throw new EmailExistsException();
                }
            } catch (UserNotFoundException e) {
                if (passwordEncoder.matches(password, authUser.getPassword())) {
                    authUser.setEmail(newEmail);
                    User updatedUser = userRepository.save(authUser);
                    UserPrincipal userPrincipal = new UserPrincipal(updatedUser);
                    String emailVerifyMail =
                            emailService.buildEmailVerifyMail(jwtTokenService.generateToken(userPrincipal));
                    emailService.send(updatedUser.getEmail(), AppConstants.VERIFY_EMAIL, emailVerifyMail);
                    return updatedUser;
                } else {
                    throw new InvalidOperationException();
                }
            }
        } else {
            throw new SameEmailUpdateException();
        }
        return null;
    }

    @Override
    public User updatePassword(UpdatePasswordDto updatePasswordDto) {
        User authUser = getAuthenticatedUser();
        if (passwordEncoder.matches(updatePasswordDto.getOldPassword(), authUser.getPassword())) {
            authUser.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
            return userRepository.save(authUser);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public User verifyEmail(String token) {
        String targetEmail = jwtTokenService.getSubjectFromToken(token);
        User targetUser = getUserByEmail(targetEmail);
        return userRepository.save(targetUser);
    }

    @Override
    public User updateProfilePhoto(MultipartFile profilePhoto) {
        User targetUser = getAuthenticatedUser();
        if (!profilePhoto.isEmpty() && profilePhoto.getSize() > 0) {
            String uploadDir = environment.getProperty("upload.user.images");
            String oldPhotoName = targetUser.getProfilePicture();
            String newPhotoName = fileNamingUtil.nameFile(profilePhoto);
            String newPhotoUrl = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.user.images") + File.separator + newPhotoName;
            targetUser.setProfilePicture(newPhotoUrl);
            try {
                if (oldPhotoName == null) {
                    fileUploadUtil.saveNewFile(uploadDir, newPhotoName, profilePhoto);
                } else {
                    fileUploadUtil.updateFile(uploadDir, oldPhotoName, newPhotoName, profilePhoto);
                }
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        return userRepository.save(targetUser);
    }

    @Override
    public void forgotPassword(String email) {
        try {
            User targetUser = getUserByEmail(email);
            UserPrincipal userPrincipal = new UserPrincipal(targetUser);
            String emailVerifyMail =
                    emailService.buildResetPasswordMail(jwtTokenService.generateToken(userPrincipal));
            emailService.send(targetUser.getEmail(), AppConstants.RESET_PASSWORD, emailVerifyMail);
        } catch (UserNotFoundException ignored) {}
    }

    @Override
    public User resetPassword(String token, ResetPasswordDto resetPasswordDto) {
        String targetUserEmail = jwtTokenService.getSubjectFromToken(token);
        User targetUser = getUserByEmail(targetUserEmail);
        targetUser.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        return userRepository.save(targetUser);
    }

    @Override
    public void deleteUserAccount() {
        User authUser = getAuthenticatedUser();
        String profilePhoto = getPhotoNameFromPhotoUrl(authUser.getProfilePicture());
        // delete user profile picture from filesystem if exists
        if (profilePhoto != null && profilePhoto.length() > 0) {
            String uploadDir = environment.getProperty("upload.user.images");
            try {
                fileUploadUtil.deleteFile(uploadDir, profilePhoto);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        userRepository.deleteByEmail(authUser.getEmail());
    }

    @Override
    public void followUser(Long userId) {
        User authUser = getAuthenticatedUser();
        if (!authUser.getId().equals(userId)) {
            User userToFollow = getUserById(userId);
            authUser.getFollowingUsers().add(userToFollow);
            authUser.setFollowingCount(authUser.getFollowingCount() + 1);
            userToFollow.getFollowerUsers().add(authUser);
            userToFollow.setFollowerCount(userToFollow.getFollowerCount() + 1);
            userRepository.save(userToFollow);
            userRepository.save(authUser);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public void unfollowUser(Long userId) {
        User authUser = getAuthenticatedUser();
        if (!authUser.getId().equals(userId)) {
            User userToUnfollow = getUserById(userId);
            authUser.getFollowingUsers().remove(userToUnfollow);
            authUser.setFollowingCount(authUser.getFollowingCount() - 1);
            userToUnfollow.getFollowerUsers().remove(authUser);
            userToUnfollow.setFollowerCount(userToUnfollow.getFollowerCount() - 1);
            userRepository.save(userToUnfollow);
            userRepository.save(authUser);
        } else {
            throw new InvalidOperationException();
        }
    }

    @Override
    public List<UserResponse> getUserSearchResult(String key, Integer page, Integer size) {
        if (key.length() < 3) throw new InvalidOperationException();

        return userRepository.findUserByUsername(
                key,
                PageRequest.of(page, size)
        ).stream().map(this::userToUserResponse).collect(Collectors.toList());
    }

    @Override
    public List<User> getLikesByPostPaginate(Post post, Integer page, Integer size) {
        return userRepository.findUsersByLikedPosts(
                post,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
        );
    }

    @Override
    public List<User> getLikesByCommentPaginate(Comment comment, Integer page, Integer size) {
        return userRepository.findUsersByLikedComments(
                comment,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "firstName", "lastName"))
        );
    }

    public final User getAuthenticatedUser() {
        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return getUserByEmail(authUserEmail);
    }

    private String getPhotoNameFromPhotoUrl(String photoUrl) {
        if (photoUrl != null) {
            String stringToOmit = environment.getProperty("app.root.backend") + File.separator
                    + environment.getProperty("upload.user.images") + File.separator;
            return photoUrl.substring(stringToOmit.length());
        } else {
            return null;
        }
    }

    private UserResponse userToUserResponse(User user) {
        User authUser = getAuthenticatedUser();
        return UserResponse.builder()
                .user(user)
                .followedByAuthUser(user.getFollowerUsers().contains(authUser))
                .build();
    }
}


