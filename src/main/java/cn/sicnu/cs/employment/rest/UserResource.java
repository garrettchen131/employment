package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.service.IUploadService;
import cn.sicnu.cs.employment.domain.entity.User;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.util.RequestUtil.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserResource {

    private final IUserInfoService userInfoService;
    private final IUserService userService;
    private final IUploadService uploadService;

    @PostMapping("/info")
    public ResultInfo<Void> postUserInfo(@RequestBody UserInfoVo userInfoVo) {
        val userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoVo, userInfo);
        userInfoService.addUserInfo(userInfo, getCurrentUser().getId());
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/info")
    public ResultInfo<UserInfoVo> getUserInfo() {
        User currentUser = getCurrentUser();
        val userInfo = userInfoService.getUserInfo(currentUser.getId());
        val userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        //补充用户名和邮箱和电话
        UserInfoVo UserInfoToSend = userInfoVo
                .withUsername(currentUser.getUsername())
                .withMobile(currentUser.getMobile())
                .withEmail(currentUser.getEmail());
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), UserInfoToSend);
    }

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
        checkVerifyCode(verifyCode);
        User userToUpdate = new User().withId(getCurrentUser().getId())
                .withMobile(mobile)
                .withEmail(email);
        userService.updateById(userToUpdate);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @PostMapping("/img")
    public ResultInfo<Void> addUserHeadImg(@RequestParam("img") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultInfoUtil.buildError(INPUT_EMPTY, "上传文件为空", getCurrentUrl());
        }
        try {
            FileInputStream fileIn = (FileInputStream) file.getInputStream();
            // 用来获取其他参数
            String fileSaveName = "head_img_" + getCurrentUser().getUsername();
            String path = uploadService.uploadImg(fileIn, fileSaveName);
            userInfoService.updateUserHeadImg(getCurrentUser().getId(), path);
            log.info("保存的图片地址={}", path);
        } catch (Exception e) {
            return ResultInfoUtil.buildError(OTHER_ERROR, "上传出错了！" + e.getMessage(), getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/img")
    public ResultInfo<String> getUserHeadImg(){
        Long id = getCurrentUser().getId();
        String headImg = userInfoService.getHeadImg(id);
        if ("".equals(headImg)){
            return ResultInfoUtil.buildError(SAVED_ERROR, "用户暂未设置头像！",getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl(),headImg);
    }
}
