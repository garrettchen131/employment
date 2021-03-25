package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;
import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.mapper.AdminRoleMapper;
import cn.sicnu.cs.employment.mapper.CompanyMapper;
import cn.sicnu.cs.employment.mapper.EmployeeCompanyMapper;
import cn.sicnu.cs.employment.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cn.sicnu.cs.employment.common.Constants.SAVED_ERROR;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyMapper companyMapper;
    private final EmployeeCompanyMapper employeeCompanyMapper;
    private final AdminRoleMapper adminRoleMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addCompanyInfo(CompanyInfo companyInfo, Long comId) {
        CompanyInfo info = companyInfo.withComId(comId);
        if (ObjectUtils.isEmpty(companyMapper.selectById(comId))) {
            companyMapper.insert(info);
        } else {
            companyMapper.updateById(info);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public CompanyInfo getCompanyInfo(Long comId) {
        if (!isComInfoExisted(comId)) {
            // 说明该用户为普通管理员，则进行调取超级管理员的公司信息
            comId = adminRoleMapper.getAdminById(comId);
        }
        return companyMapper.selectById(comId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String getComNameById(Long comId) {
        return companyMapper.selectComNameById(comId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean isComInfoExisted(Long id) {
        return companyMapper.countComById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateLogo(Long id, String path) {
        int updated = companyMapper.updateHeadImg(id, path);
        if (updated < 1){
            throw new CustomException(SAVED_ERROR, "保存LOGO失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String getHeadImg(Long id) {
        return companyMapper.selectHeadImg(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void addEmployee(Long empId) {
        // 省略判断用户id是否存在
        LocalDate date = LocalDate.now(); // get the current date
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = date.format(dateTimeFormatter);
        Long comId = getCurrentUser().getId();
        if (!isComInfoExisted(comId)) {
            // 说明该用户为普通管理员，则进行调取超级管理员的公司id
            comId = adminRoleMapper.getAdminById(comId);
        }
        employeeCompanyMapper.addEmployeeToCompany(time, empId, comId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void deprecateEmployee(Long empId) {
        // 省略判断用户id是否存在
        LocalDate date = LocalDate.now(); // get the current date
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = date.format(dateTimeFormatter);
        Long comId = getCurrentUser().getId();
        if (!isComInfoExisted(comId)) {
            // 说明该用户为普通管理员，则进行调取超级管理员的公司id
            comId = adminRoleMapper.getAdminById(comId);
        }
        employeeCompanyMapper.deprecateEmployeeToCompany(time, empId, comId);
    }

    @Override
    public List<EmployeeInfo> getEmployeesByStatus(Long status) {
        return companyMapper.listEmployeesByStatus(getCurrentUser().getId(), status);
    }

    @Override
    public String getEmployeeInTime(Long empId) {
        return employeeCompanyMapper.getInTime(empId, getCurrentUser().getId());
    }

    @Override
    public String getEmployeeOffTime(Long empId) {
        return employeeCompanyMapper.getOffTime(empId, getCurrentUser().getId());
    }
}