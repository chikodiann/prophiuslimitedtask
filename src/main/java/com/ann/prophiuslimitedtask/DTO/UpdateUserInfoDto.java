package com.ann.prophiuslimitedtask.DTO;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoDto {
    @NotEmpty
    private String username;
}

