package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Optional;

public interface IUserService{

    boolean isUsernameExisted(String username);

    boolean isEmailExisted(String email);

    boolean isMobileExisted(String mobile);

    boolean isUserIdExisted(Long id);

    void register(User user);

//    String getUsernameByEmail(String email);

    void resetPassword(User user, String newPassword);

    void activeUser(User user, String role);

    void updateById(User user);

    Long getUserIdByUsername(String username);
}
