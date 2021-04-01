package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.service.ISendMailService;
import cn.sicnu.cs.employment.service.IUserService;
import cn.sicnu.cs.employment.validation.annotation.ValidPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserResource {

    private final IUserService userService;
    private final ISendMailService sendMailService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/psd")
    public ResultInfo<Void> resetPassword(@RequestParam("old") String oldPsd,
                                          @ValidPassword @RequestParam("new") String newPsd) {

        if (!passwordEncoder.matches(oldPsd, getCurrentUser().getPassword())) {
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
        User userToUpdate = User.builder()
                .id(getCurrentUser().getId())
                .mobile(mobile)
                .email(email)
                .build();
        userService.updateById(userToUpdate);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @PostMapping("/auth")
    public ResultInfo<Void> authenticationUser(@RequestParam(value = "role")Integer role){
        //TODO 添加认证用户需要的信息
        if (role == 1){
            // 认证为企业一级管理员
            userService.activeSuperAdmin(ROLE_ENTERPRISE_SUPER);
        } else if(role == 2) {
            // 认证为高校一级管理员
            userService.activeSuperAdmin(ROLE_UNIVERSITY_SUPER);
        } else if (role == 3){
            // 认证为员工
            userService.activeEmp(getCurrentUser());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }
}
