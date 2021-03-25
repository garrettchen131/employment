package cn.sicnu.cs.employment.rest;

import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.EmployeeVo;
import cn.sicnu.cs.employment.service.IEmployeeService;
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
@RequestMapping("/emp")
@RequiredArgsConstructor
public class EmployeeResource {
    private final IEmployeeService employeeService;
    private final IUploadService uploadService;


    @PostMapping("/info")
    public ResultInfo<Void> postUserInfo(@RequestBody EmployeeVo employeeVo) {
        val employee = new EmployeeInfo();
        BeanUtils.copyProperties(employeeVo, employee);
        employeeService.addUserInfo(employee, getCurrentUser().getId());
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @GetMapping("/info")
    public ResultInfo<EmployeeVo> getUserInfo() {
        User currentUser = getCurrentUser();
        val employee = employeeService.getUserInfo(currentUser.getId());
        val employeeVo = new EmployeeVo();
        BeanUtils.copyProperties(employee, employeeVo);
        //补充用户名和邮箱和电话
        EmployeeVo employeeVoToSend = employeeVo
                .withUsername(currentUser.getUsername())
                .withMobile(currentUser.getMobile())
                .withEmail(currentUser.getEmail());
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
        return ResultInfoUtil.buildSuccess(getCurrentUrl(),headImg);
    }

}