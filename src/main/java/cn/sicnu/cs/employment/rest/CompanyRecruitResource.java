package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.common.util.BeanConvertUtils;
import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.domain.entity.CompanyRecruit;
import cn.sicnu.cs.employment.domain.vo.CompanyRecruitVo;
import cn.sicnu.cs.employment.service.ICompanyInfoService;
import cn.sicnu.cs.employment.service.ICompanyRecruitService;
import cn.sicnu.cs.employment.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/com/recruit")
@RequiredArgsConstructor
public class CompanyRecruitResource {

    private final ICompanyRecruitService recruitService;
    private final ICompanyInfoService infoService;
    private final IUserService userService;

    @PutMapping("/info")
    public ResultInfo<Void> updateCompanyRecruit(@RequestBody CompanyRecruitVo recruitVo) {
        CompanyRecruit recruit = new CompanyRecruit();
        BeanUtils.copyProperties(recruitVo, recruit);
        boolean saved = recruitService.updateById(recruit);
        if (!saved) {
            return ResultInfoUtil.buildError(OTHER_ERROR, "新增失败！", getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/info")
    public ResultInfo<CompanyRecruitVo> getCompanyRecruit(@RequestParam("id") Long recruitId) {
        if (!recruitService.isRecruitExisted(recruitId)) {
            return ResultInfoUtil.buildError(ERROR_CODE, "未找到id对应招聘信息", getCurrentUrl());
        }
        CompanyRecruit recruit = recruitService.getById(recruitId);
        CompanyRecruitVo recruitVo = new CompanyRecruitVo();
        BeanUtils.copyProperties(recruit, recruitVo);
        // 查询招聘对应公司信息
        CompanyInfo companyInfo = infoService.getCompanyInfo(recruit.getComId());
        // 补充招聘Vo中的公司信息
        CompanyRecruitVo recruitVoToSend = recruitVo
                .withComName(companyInfo.getComName())
                .withDetail(companyInfo.getDetail())
                .withComStatus(companyInfo.getStatus())
                .withWebsite(companyInfo.getWebsite());
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), recruitVoToSend);
    }

    @PostMapping("/info")
    public ResultInfo<Void> addCompanyRecruit(@RequestBody CompanyRecruitVo recruitVo) {
        CompanyRecruit recruit = new CompanyRecruit();
        BeanUtils.copyProperties(recruitVo, recruit);
        // 补充comId
        CompanyRecruit recruitToSave = recruit.withComId(getCurrentUser().getId());
        recruitService.save(recruitToSave);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @DeleteMapping("/info")
    public ResultInfo<Void> deleteCompanyRecruit(@RequestParam("id") Long recruitId) {
        if (!recruitService.isRecruitExisted(recruitId)) {
            return ResultInfoUtil.buildError(ERROR_CODE, "未找到id对应招聘信息", getCurrentUrl());
        }
        boolean deleted = recruitService.removeById(recruitId);
        if (!deleted) {
            return ResultInfoUtil.buildError(ERROR_CODE, "删除失败！", getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/list")
    public ResultInfo<List<CompanyRecruitVo>> listCompanyRecruitByComId(@RequestParam("id")Long comId){
        if (!userService.isUserIdExisted(comId)){
            return ResultInfoUtil.buildError(ERROR_CODE, "公司不存在！", getCurrentUrl());
        }
        List<CompanyRecruit> recruits = recruitService.listByComId(comId);
        // 拷贝至Vo
        List<CompanyRecruitVo> recruitVos = BeanConvertUtils.convertListTo(recruits, CompanyRecruitVo::new);
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), recruitVos);
    }

    @GetMapping("/all")
    public ResultInfo<List<CompanyRecruitVo>> getAllCompanyRecruit(){
        List<CompanyRecruit> recruits = recruitService.getAll();
        List<CompanyRecruitVo> recruitVos = BeanConvertUtils.convertListTo(recruits, CompanyRecruitVo::new);
        // 添加comName
        for (CompanyRecruitVo recruitVo : recruitVos) {
            recruitVo.setComName(infoService.getComNameById(recruitVo.getComId()));
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), recruitVos);
    }
}