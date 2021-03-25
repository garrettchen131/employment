package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;

import java.util.List;

public interface ICompanyService {
    /**
     * 添加/修改公司信息
     */
    void addCompanyInfo(CompanyInfo companyInfo, Long userId);

    CompanyInfo getCompanyInfo(Long userId);

    String getComNameById(Long comId);

    boolean isComInfoExisted(Long id);

    void updateLogo(Long id, String path);

    String getHeadImg(Long id);

    void addEmployee(Long empId);

    void deprecateEmployee(Long empId);

    List<EmployeeInfo> getEmployeesByStatus(Long status);

    String getEmployeeInTime(Long id);

    String getEmployeeOffTime(Long id);
}
