package com.ann.prophiuslimitedtask.controller;

import com.ann.prophiuslimitedtask.response.PostResponse;
import com.ann.prophiuslimitedtask.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TimelineController {
    private final PostService postService;
    @GetMapping( "") // Get paginated timeline posts for the authenticated user
    public ResponseEntity<?> getTimelinePosts(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        // Ensure valid pagination values
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        // Retrieve timeline posts and return as response
        List<PostResponse> timelinePosts = postService.getTimelinePostsPaginate(page, size);
        return new ResponseEntity<>(timelinePosts, HttpStatus.OK);
    }
}

/* This class deals with the user's timeline and provides an endpoint
to retrieve paginated timeline posts for the authenticated user
 */