package com.ann.prophiuslimitedtask.response;

import com.ann.prophiuslimitedtask.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private User user;
    private Boolean followedByAuthUser;
}
