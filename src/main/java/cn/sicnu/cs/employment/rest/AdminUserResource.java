package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.common.util.BeanConvertUtils;
import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.CompanyInfoVo;
import cn.sicnu.cs.employment.domain.vo.UserVo;
import cn.sicnu.cs.employment.service.IAdminRoleService;
import cn.sicnu.cs.employment.service.ICompanyService;
import cn.sicnu.cs.employment.service.IUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.List;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUserResource {

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
    public ResultInfo<Void> addAuthenticatedRole(@RequestParam("target") String username) {
        adminRoleService.authenticateUser(username);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/auth")
    public ResultInfo<List<UserVo>> getAllAuthenticatedRoles() {
        List<User> roles = adminRoleService.listRoles(getCurrentUser().getId());
        List<UserVo> rolesToSend = BeanConvertUtils.convertListTo(roles, UserVo::new);
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), rolesToSend);
    }

    @DeleteMapping("/auth")
    public ResultInfo<Void> deleteAuthenticateUser(@RequestParam("target")String username){
        adminRoleService.removeAuthenticateUser(username);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }


}