package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.domain.entity.User;

import java.util.Optional;

public interface IUserService {

    boolean isUsernameExisted(String username);

    boolean isEmailExisted(String email);

    boolean isMobileExisted(String mobile);

    void register(boolean isCom, User user);

//    String getUsernameByEmail(String email);

    void resetPassword(User user, String newPassword);

    void activeUser(User user);

//    Optional<User> login(String str, String password, String method);

//    Long LoginByUsername(String username, String password);
//
//    Long LoginByEmail(String email, String password);
}
