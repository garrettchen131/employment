package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.domain.entity.EmployeeExpEducation;
import cn.sicnu.cs.employment.domain.entity.EmployeeExpInternship;
import cn.sicnu.cs.employment.domain.entity.EmployeeExpProject;
import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;

import java.util.Set;

public interface IEmployeeService {
    void addUserInfo(EmployeeInfo userInfo, Long userId);

    EmployeeInfo getUserInfo(Long userId);

    void updateHeadImg(Long userId, String path);

    String getHeadImg(Long id);

    boolean isUserInfoExisted(Long userId);

    Set<EmployeeExpEducation> getEducationExp(Long id);

    Set<EmployeeExpInternship> getInternshipExp(Long id);

    Set<EmployeeExpProject> getProjectExp(Long id);
}
