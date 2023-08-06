package com.ann.prophiuslimitedtask.DTO;

import com.ann.prophiuslimitedtask.annotation.PasswordRepeatEqual;
import com.ann.prophiuslimitedtask.annotation.ValidEmail;
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
public class SignupDto {
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
    private String passwordRepeat;
    private String username;

}
