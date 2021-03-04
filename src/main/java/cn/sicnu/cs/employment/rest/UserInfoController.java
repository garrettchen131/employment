package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.entity.UserInfo;
import cn.sicnu.cs.employment.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/userInfo")
@RequiredArgsConstructor
public class UserInfoController {

    private final IUserInfoService userInfoService;

    /**
     * 获取用户信息
     */
    @GetMapping("/get")
    public ResultInfo<UserInfo> getUserInfo(){
        User user = getCurrentUser();
        Long id = user.getId();
        log.info("userid={}",id);
        UserInfo userInfo = userInfoService.getUserInfo(id);
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), userInfo);
    }
}