package cn.sicnu.cs.employment.rest;


import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
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

    @PostMapping("/info")
    public ResultInfo<Void> postCompanyInfo(@RequestBody CompanyInfoVo companyInfoVo) {
        val companyInfo = new CompanyInfo();
        BeanUtils.copyProperties(companyInfoVo, companyInfo);
        companyInfoService.addCompanyInfo(companyInfo, getCurrentUser().getId());
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/info")
    public ResultInfo<CompanyInfoVo> getCompanyInfo() {
        val companyInfo = companyInfoService.getCompanyInfo(getCurrentUser().getId());
        val companyInfoVo = new CompanyInfoVo();
        BeanUtils.copyProperties(companyInfo, companyInfoVo);
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), companyInfoVo);
    }
}