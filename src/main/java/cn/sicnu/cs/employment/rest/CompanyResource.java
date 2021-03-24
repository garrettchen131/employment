package cn.sicnu.cs.employment.rest;


import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.CompanyInfoVo;
import cn.sicnu.cs.employment.service.ICompanyInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/com")
@RequiredArgsConstructor
public class CompanyResource {

    private final ICompanyInfoService companyInfoService;


    @GetMapping("/info")
    public ResultInfo<CompanyInfoVo> getCompanyInfo() {
        User currentUser = getCurrentUser();
        val companyInfo = companyInfoService.getCompanyInfo(currentUser.getId());
        val companyInfoVo = new CompanyInfoVo();
        BeanUtils.copyProperties(companyInfo, companyInfoVo);
        //补充邮箱和电话
        CompanyInfoVo companyInfoToSend = companyInfoVo
                .withUsername(currentUser.getUsername())
                .withMobile(currentUser.getMobile())
                .withEmail(currentUser.getEmail());
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), companyInfoToSend);
    }
}