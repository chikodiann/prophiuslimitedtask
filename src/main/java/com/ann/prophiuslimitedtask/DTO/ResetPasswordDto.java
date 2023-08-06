package com.ann.prophiuslimitedtask.DTO;

import com.ann.prophiuslimitedtask.annotation.PasswordRepeatEqual;
import com.ann.prophiuslimitedtask.annotation.ValidPassword;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordRepeatEqual(
        passwordFieldFirst = "password",
        passwordFieldSecond = "passwordRepeat"
)
public class ResetPasswordDto {
    @ValidPassword
    private String password;
    private String passwordRepeat;
}
