package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Optional;

public interface IUserService extends IService<User> {

    boolean isUsernameExisted(String username);

    boolean isEmailExisted(String email);

    boolean isMobileExisted(String mobile);

    void register(boolean isCom, User user);

//    String getUsernameByEmail(String email);

    void resetPassword(User user, String newPassword);

    void activeUser(User user);

}
