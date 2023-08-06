package com.ann.prophiuslimitedtask.DTO;


import com.ann.prophiuslimitedtask.annotation.ValidEmail;
import com.ann.prophiuslimitedtask.annotation.ValidPassword;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmailDto {
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
}
