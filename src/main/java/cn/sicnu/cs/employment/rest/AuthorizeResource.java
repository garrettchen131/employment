package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.vo.UserVo;
import cn.sicnu.cs.employment.service.IUserService;
import cn.sicnu.cs.employment.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizeResource {

    private final IUserService userService;

    @PostMapping("/register")
    public ResultInfo<Void> register(@Valid @RequestBody UserVo userVo) {
        if (userService.isUsernameExisted(userVo.getUsername())) {
            log.info("Username existed!");
            return ResultInfoUtil.buildError(USERNAME_EXISTED,"用户名重复",getCurrentUrl());
        }
        if (userService.isEmailExisted(userVo.getEmail())) {
            log.info("Email existed!");
            return ResultInfoUtil.buildError(EMAIL_EXISTED, "邮箱重复", getCurrentUrl());
        }
        if (userService.isMobileExisted(userVo.getEmail())) {
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


    @GetMapping("/me")
    public ResultInfo<Authentication> me() {
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), SecurityContextHolder.getContext().getAuthentication());
    }
}
