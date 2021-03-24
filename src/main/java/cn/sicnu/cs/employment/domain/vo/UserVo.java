package cn.sicnu.cs.employment.domain.vo;

import cn.sicnu.cs.employment.validation.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@PasswordMatches
public class UserVo {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @ValidPassword
    private String password;

//    private String matchingPassword;

    @Size(min = 11, max = 11)
    private String mobile;

    @Email(message = "{ValidEmail.userVo}")
    @NotNull
    private String email;

}
