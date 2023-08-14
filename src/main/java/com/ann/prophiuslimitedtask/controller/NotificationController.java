package com.ann.prophiuslimitedtask.controller;

import com.ann.prophiuslimitedtask.entity.Notification;
import com.ann.prophiuslimitedtask.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* This class handles operations related to notifications.
 It defines endpoints for retrieving notifications, marking notifications as seen, and marking notifications as read.
 */

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // Get paginated notifications for the authenticated user
    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(
            @RequestParam(name = "receiver") String receiver,
            @RequestParam(name = "sender", required = false) String sender,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        // Ensure valid pagination values
        page = page < 0 ? 0 : page - 1;
        size = size <= 0 ? 5 : size;

        List<Notification> notifications;

        if (sender != null) {
            notifications = notificationService.getNotificationsForAuthUserPaginate(receiver, page, size);
        } else {
            notifications = notificationService.getNotificationsForAuthUserPaginate(receiver, page, size);
        }

        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    // Mark all notifications as seen
    @PostMapping("/notifications/mark-seen")
    public ResponseEntity<?> markAllSeen() {
        notificationService.markAllSeen();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    // Mark all notifications as read
    @PostMapping("/notifications/mark-read")
    public ResponseEntity<?> markAllRead() {
        notificationService.markAllRead();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
