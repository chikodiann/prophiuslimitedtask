package com.ann.prophiuslimitedtask.response;

;
import com.ann.prophiuslimitedtask.entity.Post;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Post post;
    private Boolean likedByAuthUser;
}
