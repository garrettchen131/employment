package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;

public interface IEmployeeService {
    void addUserInfo(EmployeeInfo userInfo, Long userId);

    EmployeeInfo getUserInfo(Long userId);

    void updateHeadImg(Long userId, String path);

    String getHeadImg(Long id);

    boolean isUserInfoExisted(Long userId);
}
