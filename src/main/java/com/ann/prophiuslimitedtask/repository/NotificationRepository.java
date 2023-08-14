package com.ann.prophiuslimitedtask.repository;

import com.ann.prophiuslimitedtask.entity.Comment;
import com.ann.prophiuslimitedtask.entity.Notification;
import com.ann.prophiuslimitedtask.entity.Post;
import com.ann.prophiuslimitedtask.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByReceiverAndOwningPostAndType(User receiver, Post owningPost, String type);
    List<Notification> findNotificationsByReceiver(User receiver, Pageable pageable);
    List<Notification> findNotificationsByReceiverAndIsSeenIsFalse(User receiver);
    List<Notification> findNotificationsByReceiverAndIsReadIsFalse(User receiver);
    void deleteNotificationByOwningPost(Post owningPost);
    void deleteNotificationByOwningComment(Comment owningComment);
    List<Notification> findNotificationsByReceiverAndSenderAndIsSeenIsFalse(User authUser, String receiver, String sender, PageRequest dateUpdated);

    List<Notification> findNotificationsByReceiverAndIsSeenIsFalse(User authUser, String receiver, PageRequest dateUpdated);
}
