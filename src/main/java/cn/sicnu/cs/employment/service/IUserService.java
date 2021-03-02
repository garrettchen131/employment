package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.domain.entity.User;

public interface IUserService {

    boolean isUsernameExisted(String username);

    boolean isEmailExisted(String email);

    boolean isMobileExisted(String mobile);

    void register(User user);


}
