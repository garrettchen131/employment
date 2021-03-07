package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.UserInfo;
import cn.sicnu.cs.employment.domain.vo.UserInfoVo;
import cn.sicnu.cs.employment.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.sicnu.cs.employment.common.util.RequestUtil.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserResource {

    private final IUserInfoService userInfoService;

    @PostMapping("/info")
    public ResultInfo<Void> postUserInfo(UserInfoVo userInfoVo) {
        val userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoVo, userInfo);
        userInfoService.addUserInfo(userInfo, getCurrentUser().getId());
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/info")
    public ResultInfo<UserInfoVo> getUserInfo() {
        val userInfo = userInfoService.getUserInfo(getCurrentUser().getId());
        val userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), userInfoVo);
    }
}
