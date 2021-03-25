package cn.sicnu.cs.employment.rest;


import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.CompanyInfoVo;
import cn.sicnu.cs.employment.service.ICompanyInfoService;
import cn.sicnu.cs.employment.service.IUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.Constants.SAVED_ERROR;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/com")
@RequiredArgsConstructor
public class CompanyResource {

    private final ICompanyInfoService companyInfoService;
    private final IUploadService uploadService;


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
                .withEmail(currentUser.getEmail())
                .withLogo(companyInfoService.getHeadImg(currentUser.getId()));
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), companyInfoToSend);
    }

    @PostMapping("/logo")
    public ResultInfo<Void> addUserHeadImg(@RequestParam("logo") MultipartFile file) {
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

    @GetMapping("/logo")
    public ResultInfo<String> getUserHeadImg(){
        Long id = getCurrentUser().getId();
        String logo = companyInfoService.getHeadImg(id);
        if ("".equals(logo)){
            return ResultInfoUtil.buildError(SAVED_ERROR, "公司暂未设置头像！",getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl(),logo);
    }
}