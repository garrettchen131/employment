package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.common.util.BeanConvertUtils;
import cn.sicnu.cs.employment.domain.entity.AdminRole;
import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.*;
import cn.sicnu.cs.employment.service.IAdminRoleService;
import cn.sicnu.cs.employment.service.ICompanyService;
import cn.sicnu.cs.employment.service.IUploadService;
import cn.sicnu.cs.employment.service.IUserService;
import com.github.pagehelper.Page;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUserResource {

    private final IUserService userService;
    private final ICompanyService companyInfoService;
    private final IUploadService uploadService;
    private final IAdminRoleService adminRoleService;

    @PostMapping("/com/info")
    public ResultInfo<Void> updateCompanyInfo(@RequestBody CompanyInfoVo companyInfoVo) {
        val companyInfo = new CompanyInfo();
        BeanUtils.copyProperties(companyInfoVo, companyInfo);
        companyInfoService.addCompanyInfo(companyInfo, getCurrentUser().getId());
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @PostMapping("/com/logo")
    public ResultInfo<Void> addCompanyLogo(@RequestParam("logo") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultInfoUtil.buildError(INPUT_EMPTY, "上传LOGO文件为空！", getCurrentUrl());
        }
        try {
            FileInputStream fileIn = (FileInputStream) file.getInputStream();
            // 用来获取其他参数
            String fileSaveName = PREFIX_COM_LOGO+ getCurrentUser().getUsername();
            String path = uploadService.uploadImg(fileIn, fileSaveName);
            companyInfoService.updateLogo(getCurrentUser().getId(), path);
            log.info("保存的图片地址={}", path);
        } catch (Exception e) {
            return ResultInfoUtil.buildError(OTHER_ERROR, "上传出错了！" + e.getMessage(), getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/com/logo")
    public ResultInfo<String> getCompanyLogo(){
        Long id = getCurrentUser().getId();
        String logo = companyInfoService.getHeadImg(id);
        if ("".equals(logo)){
            return ResultInfoUtil.buildError(SAVED_ERROR, "公司暂未设置头像！",getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl(),logo);
    }

    @PostMapping("/auth")
    public ResultInfo<Void> addAuthenticatedRole(@RequestParam("username")String username,
                                                 @RequestParam("phone")String mobile,
                                                 @RequestParam("info")String info) {
        if(userService.isUsernameExisted(username)){
            return ResultInfoUtil.buildError(ERROR_CODE, "用户名已存在！",getCurrentUrl());
        }
        if (userService.isMobileExisted(mobile)) {
            return ResultInfoUtil.buildError(ERROR_CODE, "该手机号已经被注册使用！", getCurrentUrl());
        }
        User userToAuth = User.builder()
                .username(username)
                .mobile(mobile)
                .build();
        adminRoleService.authenticateUser(userToAuth, info);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/auth")
    public ResultInfo<Pager<AdminRoleVo>> getAllAuthenticatedRoles(@RequestBody QueryVo query) {
        if (query.getUsername() != null){
            query.setUserId(userService.getUserIdByUsername(query.getUsername()));
        }
        Page<AdminRole> rolePage = adminRoleService.listRoles(query);
        List<AdminRoleVo> roleVos = new ArrayList<>();
        for (AdminRole role : rolePage) {
            User roleUser = userService.getUserById(role.getRoleId());
            AdminRoleVo adminRoleVo = AdminRoleVo.builder()
                    .id(role.getRoleId())
                    .username(roleUser.getUsername())
                    .phone(roleUser.getMobile())
                    .position(role.getInfo())
                    .addTime(String.valueOf(roleUser.getCreateTime()))
                    .isEnable(roleUser.getStatus())
                    .build();
            roleVos.add(adminRoleVo);
        }
        Pager<AdminRoleVo> roleVoPage = Pager.<AdminRoleVo>builder()
                .data(roleVos)
                .total(rolePage.getTotal())
                .build();
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), roleVoPage);
    }

    @PostMapping("/auth/status")
    public ResultInfo<Void> activeAuthenticateUser(@RequestParam("id")Long id,
                                                   @RequestParam("flage")Integer status){
        User user = userService.getUserById(id);
        if (user.getStatus() && status==1){
            return ResultInfoUtil.buildError(ERROR_CODE, "目标用户已经为启用状态",getCurrentUrl());
        }
        if (!user.getStatus() && status==0){
            return ResultInfoUtil.buildError(ERROR_CODE, "目标用户已经为禁用状态",getCurrentUrl());
        }
        userService.updateStatus(id, status);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @PutMapping("auth/{roleId}")
    public ResultInfo<Void> resetAuthUserPassword(@PathVariable("roleId")Long roleId){
        if(!adminRoleService.hasRoleUser(roleId)){
            return ResultInfoUtil.buildError(ERROR_CODE, "无权限！该二级用户不属于您的账户",getCurrentUrl());
        }
        userService.resetPassword(new User().withId(roleId), DEFAULT_PASS);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }


}