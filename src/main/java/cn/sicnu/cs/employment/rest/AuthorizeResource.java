package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.Constants;
import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.entity.UserInfo;
import cn.sicnu.cs.employment.domain.vo.UserVo;
import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.service.ISendMail;
import cn.sicnu.cs.employment.service.IUserService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.context.support.UiApplicationContextUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.util.RequestUtil.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizeResource {

    private final DefaultKaptcha defaultKaptcha;
    private final IUserService userService;
    private final ISendMail sendMail;

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
        userService.register(userVo.getIsCom(),User.builder()
                .username(userVo.getUsername())
                .email(userVo.getEmail())
                .mobile(userVo.getMobile())
                .password(userVo.getPassword())
                .status(false)
                .build());
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

//    /**
//     * 通过邮箱返回用户的用户名
//     */
//
//    @PostMapping("/getName")
//    public ResultInfo<String> login(@RequestParam("email") String email) {
//        log.info("login method is EMAIL ={}=", email);
//        if (!userService.isEmailExisted(email)) {
//            log.info("email ={}= not exists", email);
//            return ResultInfoUtil.buildError(USER_INPUT_ERROR, "邮箱不存在", getCurrentUrl());
//        }
//        String username = userService.getUsernameByEmail(email);
//        return ResultInfoUtil.buildSuccess(getCurrentUrl(), username);
//    }

    @GetMapping("/me")
    public ResultInfo<Authentication> me() {
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * 获取验证码图片
     *
     */
    @GetMapping("/getKap")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {
        byte[] captchaChallengeAsJpeg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            httpServletRequest.getSession().setAttribute("kapCode", createText);
            log.info("生成验证码={}", createText);
            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @PostMapping("/checkKap")
    public ResultInfo<Void> checkKaptchaCode(HttpServletRequest httpServletRequest,
                                             @RequestParam("code") String getCode) {
        String kapCode = (String) httpServletRequest.getSession().getAttribute("kapCode");
        if (kapCode != null) {
            if (kapCode.equals(getCode)) {
                httpServletRequest.getSession().removeAttribute("kapCode");
                return ResultInfoUtil.buildSuccess(getCurrentUrl());
            } else {
                return ResultInfoUtil.buildError(Constants.ERROR_CODE, "验证码错误", getCurrentUrl());
            }
        } else {
            return ResultInfoUtil.buildError(Constants.OTHER_ERROR, "请重新生成验证码", getCurrentUrl());
        }
    }

    @PostMapping("/sendEmail")
    public ResultInfo<Void> sendVerifyCodeEmail(HttpSession httpSession,
                                                @RequestParam("receiver") String receiver) {
        String emailCode = sendMail.sendEmail(receiver);
        httpSession.setAttribute("emailCode", emailCode);
        httpSession.setAttribute("emailTime", System.currentTimeMillis());
        log.info("send code ={}= to ={}=",emailCode, receiver);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @PostMapping("/checkEmail")
    public ResultInfo<Void> checkEmailCode(@RequestParam("verifyCode") String verifyCode) {
        checkVerifyCode(verifyCode);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/test")
    public ResultInfo<User> test(){
//        log.info("password={}",getCurrentUser().getPassword());
        return ResultInfoUtil.buildSuccess(getCurrentUrl(),getCurrentUser());
    }

    @PostMapping("/psd")
    public ResultInfo<Void> forgetPassword(@RequestParam("verifyCode")String code,
                                           @RequestParam("email") String email,
                                           @RequestParam("newPsd")String newPsd){
        checkVerifyCode(code);
        if(!userService.isEmailExisted(email)){
            return ResultInfoUtil.buildError(USER_INPUT_ERROR, "邮箱未被注册！", getCurrentUrl());
        }
        User user = new User().withEmail(email);
        userService.resetPassword(user, newPsd);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }
}
