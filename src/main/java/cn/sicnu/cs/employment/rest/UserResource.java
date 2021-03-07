package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.UserInfo;
import cn.sicnu.cs.employment.domain.vo.UserInfoVo;
import cn.sicnu.cs.employment.service.IUserInfoService;
import cn.sicnu.cs.employment.service.IUserService;
import cn.sicnu.cs.employment.validation.annotation.ValidPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import static cn.sicnu.cs.employment.common.Constants.OTHER_ERROR;
import static cn.sicnu.cs.employment.common.Constants.PASSWORD_ERROR;
import static cn.sicnu.cs.employment.common.util.RequestUtil.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserResource {

    private final IUserInfoService userInfoService;
    private final IUserService userService;

    @PostMapping("/info")
    public ResultInfo<Void> postUserInfo(@RequestBody UserInfoVo userInfoVo) {
        System.out.println("===========userInfo=" + userInfoVo);
        val userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoVo, userInfo);
        userInfoService.addUserInfo(userInfo, getCurrentUser().getId());
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/info")
    public ResultInfo<UserInfoVo> getUserInfo() {
        val userInfo = userInfoService.getUserInfo(getCurrentUser().getId());
        val password = getCurrentUser().getPassword();
        log.info("user={}", password);
        val userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), userInfoVo);
    }

    @PostMapping("/psd")
    public ResultInfo<Void> resetPassword(@RequestParam("old") String oldPsd,
                                          @ValidPassword @RequestParam("new") String newPsd){
        if(!getCurrentUser().getPassword().equals(oldPsd)){
            return ResultInfoUtil.buildError(PASSWORD_ERROR, "密码错误！", getCurrentUrl());
        }
        try {
            userService.resetPassword(getCurrentUser(), newPsd);
        }catch (Exception e){
            return ResultInfoUtil.buildError(OTHER_ERROR,"密码更新失败！",getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }
}
