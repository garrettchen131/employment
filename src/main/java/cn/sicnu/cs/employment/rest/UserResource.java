package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.service.ISendMailService;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.service.IUserService;
import cn.sicnu.cs.employment.validation.annotation.ValidPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.util.RequestUtil.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserResource {

    private final IUserService userService;
    private final ISendMailService sendMailService;


    @PostMapping("/psd")
    public ResultInfo<Void> resetPassword(@RequestParam("old") String oldPsd,
                                          @ValidPassword @RequestParam("new") String newPsd) {

        if (!getCurrentUser().getPassword().equals(oldPsd)) {
            return ResultInfoUtil.buildError(PASSWORD_ERROR, "密码错误！", getCurrentUrl());
        }
        userService.resetPassword(getCurrentUser(), newPsd);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @PostMapping("/update")
    public ResultInfo<Void> updateUserMobileOrEmail(
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam("verifyCode") String verifyCode) {

        // 验证邮箱验证码
        boolean check = sendMailService.checkVerifyCode(verifyCode);
        if (! check) {
            return ResultInfoUtil.buildError(OTHER_ERROR, "邮箱验证码错误或已过期", getCurrentUrl());
        }
        User userToUpdate = new User().withId(getCurrentUser().getId())
                .withMobile(mobile)
                .withEmail(email);
        userService.updateById(userToUpdate);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }



    @PostMapping("/auth")
    public ResultInfo<Void> authenticationUser(@RequestParam("role") String role){
        //TODO 用户认证相关代码，目前恒返回成功
        //TODO 用户认证成功后，将用户账号与 user_info 绑定


        userService.activeUser(getCurrentUser(), role);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }
}
