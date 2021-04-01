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

    void activeEmp(User user);

    void activeAdmin(User user, String info);

    void updateById(User user);

    Long getUserIdByUsername(String username);

    User getUserById(Long roleId);

    void updateStatus(Long id, Integer status);

    void activeSuperAdmin(String role);
}
