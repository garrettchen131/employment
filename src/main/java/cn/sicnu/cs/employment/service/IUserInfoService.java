package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.domain.entity.UserInfo;

public interface IUserInfoService {
    void addUserInfo(UserInfo userInfo, Long userId);

    UserInfo getUserInfo(Long userId);
}
