package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.EmployeeInfoVo;
import cn.sicnu.cs.employment.service.IEmployeeService;
import cn.sicnu.cs.employment.service.IUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.annotations.Param;
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
@RequestMapping("/emp")
@RequiredArgsConstructor
public class EmployeeResource {
    private final IEmployeeService employeeService;
    private final IUploadService uploadService;

    @GetMapping("/info")
    public ResultInfo<EmployeeInfoVo> getUserInfo(@RequestParam(value = "id", required = false)Long id) {
        if(id== null){
            id = getCurrentUser().getId();
        }
        val employee = employeeService.getUserInfo(id);
        val employeeVo = new EmployeeInfoVo();
        BeanUtils.copyProperties(employee, employeeVo);
        //补充教育经历、实习经历、项目经历、头像
        EmployeeInfoVo employeeVoToSend = employeeVo
                .withHeadImg(PREFIX_HEAD_IMG + employee.getHeadImg())
                .withEducationExp(employeeService.getEducationExp(employee.getId()))
                .withInternshipExp(employeeService.getInternshipExp(employee.getId()))
                .withProjectExp(employeeService.getProjectExp(employee.getId()));
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), employeeVoToSend);
    }

    @PostMapping("/img")
    public ResultInfo<Void> addHeadImg(@RequestParam("img") MultipartFile file) {
        if (file.isEmpty()) {
            return ResultInfoUtil.buildError(INPUT_EMPTY, "上传文件为空", getCurrentUrl());
        }
        try {
            FileInputStream fileIn = (FileInputStream) file.getInputStream();
            // 用来获取其他参数
            String fileSaveName = PREFIX_HEAD_IMG + getCurrentUser().getUsername();
            String path = uploadService.uploadImg(fileIn, fileSaveName);
            employeeService.updateHeadImg(getCurrentUser().getId(), path);
            log.info("保存的图片地址={}", path);
        } catch (Exception e) {
            return ResultInfoUtil.buildError(OTHER_ERROR, "上传出错了！" + e.getMessage(), getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/img")
    public ResultInfo<String> getHeadImg(){
        Long id = getCurrentUser().getId();
        String headImg = employeeService.getHeadImg(id);
        if ("".equals(headImg)){
            return ResultInfoUtil.buildError(SAVED_ERROR, "用户暂未设置头像！",getCurrentUrl());
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl(),PREFIX_PIC_STORE + headImg);
    }

}