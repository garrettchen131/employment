package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.Constants;
import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.common.util.KaptchaUtil;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.UserVo;
import cn.sicnu.cs.employment.service.ISendMailService;
import cn.sicnu.cs.employment.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizeResource {

    private final IUserService userService;
    private final ISendMailService sendMail;

    @PostMapping("/register")
    public ResultInfo<Void> register(@Valid @RequestBody UserVo userVo) {
        if (userService.isUsernameExisted(userVo.getUsername())) {
            log.info("Username existed!");
            return ResultInfoUtil.buildError(USERNAME_EXISTED, "用户名重复", getCurrentUrl());
        }
        if (userService.isEmailExisted(userVo.getEmail())) {
            log.info("Email existed!");
            return ResultInfoUtil.buildError(EMAIL_EXISTED, "邮箱重复", getCurrentUrl());
        }
        if (userService.isMobileExisted(userVo.getMobile())) {
            log.info("Mobile existed!");
            return ResultInfoUtil.buildError(MOBILE_EXISTED, "电话号码重复", getCurrentUrl());
        }
        userService.register(User.builder()
                .username(userVo.getUsername())
                .email(userVo.getEmail())
                .mobile(userVo.getMobile())
                .password(userVo.getPassword())
                .status(false)
                .build());
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }


    @RequestMapping("/me")
    public ResultInfo<Authentication> me() {
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), SecurityContextHolder.getContext().getAuthentication());
    }

//    /**
//     * 获取验证码图片
//     */
//    @GetMapping("/getKap")
//    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
//        kaptchaUtil.getKaptchaJpg(httpServletRequest, httpServletResponse);
//    }

//    @PostMapping("/checkKap")
//    public ResultInfo<Void> checkKaptchaCode(HttpServletRequest httpServletRequest,
//                                             @RequestParam("code") String getCode) {
//        if (!kaptchaUtil.checkKaptchaCode(httpServletRequest, getCode)) {
//            return ResultInfoUtil.buildError(ERROR_CODE, "邮箱验证码错误或已过期", getCurrentUrl());
//        }
//        return ResultInfoUtil.buildSuccess(getCurrentUrl());
//    }

    @PostMapping("/sendEmail")
    public ResultInfo<Void> sendVerifyCodeEmail(@RequestParam("receiver") String receiver) {
        sendMail.sendEmail(receiver);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @PostMapping("/checkEmail")
    public ResultInfo<Void> checkEmailCode(@RequestParam("verifyCode") String verifyCode) {
        boolean check = sendMail.checkVerifyCode(verifyCode);
        if (!check) {
            return ResultInfoUtil.buildError(OTHER_ERROR, "邮箱验证码错误或已过期", getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/test")
    public ResultInfo<User> test() {
//        log.info("password={}",getCurrentUser().getPassword());
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), getCurrentUser());
    }

    @PostMapping("/psd")
    public ResultInfo<Void> forgetPassword(@RequestParam("verifyCode") String code,
                                           @RequestParam("email") String email,
                                           @RequestParam("newPsd") String newPsd) {
        boolean check = sendMail.checkVerifyCode(code);
        if (!check) {
            return ResultInfoUtil.buildError(OTHER_ERROR, "邮箱验证码错误或已过期！", getCurrentUrl());
        }
        if (!userService.isEmailExisted(email)) {
            return ResultInfoUtil.buildError(USER_INPUT_ERROR, "邮箱未被注册！", getCurrentUrl());
        }
        User user = new User().withEmail(email);
        userService.resetPassword(user, newPsd);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }
}
