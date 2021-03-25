package cn.sicnu.cs.employment.rest;


import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.common.util.BeanConvertUtils;
import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.CompanyInfoVo;
import cn.sicnu.cs.employment.domain.vo.EmployeeInfoVo;
import cn.sicnu.cs.employment.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUrl;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Slf4j
@RestController
@RequestMapping("/com")
@RequiredArgsConstructor
public class CompanyResource {

    private final ICompanyService companyService;

    @GetMapping("/info")
    public ResultInfo<CompanyInfoVo> getCompanyInfo() {
        User currentUser = getCurrentUser();
        val companyInfo = companyService.getCompanyInfo(currentUser.getId());
        val companyInfoVo = new CompanyInfoVo();
        BeanUtils.copyProperties(companyInfo, companyInfoVo);
        //补充邮箱和电话
        CompanyInfoVo companyInfoToSend = companyInfoVo
                .withUsername(currentUser.getUsername())
                .withMobile(currentUser.getMobile())
                .withEmail(currentUser.getEmail())
                .withLogo(companyService.getHeadImg(currentUser.getId()));
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), companyInfoToSend);
    }

    @PostMapping("/emp")
    public ResultInfo<Void> addEmployee(@RequestParam("empId") Long empId) {
        companyService.addEmployee(empId);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    @DeleteMapping("/emp")
    public ResultInfo<Void> deleteEmployee(@RequestParam("empId") Long empId) {
        companyService.deprecateEmployee(empId);
        return ResultInfoUtil.buildSuccess(getCurrentUrl());
    }

    /**
     * @param status 0；招聘，》》 1；在职；》》 2；离职
     */
    @GetMapping("/emp")
    public ResultInfo<List<EmployeeInfoVo>> getAllEmployee(@RequestParam("status") Long status) {
        List<EmployeeInfo> employees = companyService.getEmployeesByStatus(status);
        List<EmployeeInfoVo> employeeVos = BeanConvertUtils.convertListTo(employees, EmployeeInfoVo::new);
        // 为每位员工附上入职时间（离职时间）
        if (status == 1) {
            for (EmployeeInfoVo employeeVo : employeeVos) {
                String inTimeTime = companyService.getEmployeeInTime(employeeVo.getId());
                employeeVo.setInTime(inTimeTime);
            }
        } else if (status == 2) {
            for (EmployeeInfoVo employeeVo : employeeVos) {
                String offTime = companyService.getEmployeeOffTime(employeeVo.getId());
                employeeVo.setOffTime(offTime);
            }
        }
        return ResultInfoUtil.buildSuccess(getCurrentUrl(), employeeVos);
    }

}