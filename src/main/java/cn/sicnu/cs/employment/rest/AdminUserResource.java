package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.common.util.BeanConvertUtils;
import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.CompanyInfoVo;
import cn.sicnu.cs.employment.domain.vo.UserVo;
import cn.sicnu.cs.employment.service.IAdminRoleService;
import cn.sicnu.cs.employment.service.ICompanyInfoService;
import cn.sicnu.cs.employment.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.sicnu.cs.employment.common.Constants.ERROR_CODE;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUserResource {

    private final ICompanyInfoService companyInfoService;

    private final IAdminRoleService adminRoleService;

    @PostMapping("/info")
    public ResultInfo<Void> updateCompanyInfo(@RequestBody CompanyInfoVo companyInfoVo) {
        val companyInfo = new CompanyInfo();
        BeanUtils.copyProperties(companyInfoVo, companyInfo);
        companyInfoService.addCompanyInfo(companyInfo, getCurrentUser().getId());
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
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