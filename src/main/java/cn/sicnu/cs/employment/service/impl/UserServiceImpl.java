package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.Role;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.entity.UserInfo;
import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.mapper.RoleMapper;
import cn.sicnu.cs.employment.mapper.UserInfoMapper;
import cn.sicnu.cs.employment.mapper.UserMapper;
import cn.sicnu.cs.employment.service.IUserInfoService;
import cn.sicnu.cs.employment.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Set;

import static cn.sicnu.cs.employment.common.Constants.*;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;
    private final IUserInfoService userInfoService;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isUsernameExisted(String username) {
        return userMapper.countByUsername(username) > 0;
    }

    @Override
    public boolean isEmailExisted(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    @Override
    public boolean isMobileExisted(String mobile) {
        return userMapper.countByMobile(mobile) > 0;
    }

    @Override
    public boolean isUserIdExisted(Long id) {
        return userMapper.countByUserId(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void register( User user) {
        roleMapper.findOptionalByAuthority(ROLE_PERSON)
                .ifPresentOrElse(
                        role -> {
                            val userToSave = user
                                    .withAuthorities(Set.of(role))
                                    .withPassword(passwordEncoder.encode(user.getPassword()));
                            userMapper.insert(userToSave);
                            roleMapper.addUserRole(userToSave.getId(), role.getId());
                        },
                        () -> {
                            throw new NoSuchElementException("Cannot find role");
                        }
                );
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void resetPassword(User user, String newPassword) {
        try {
            if (user.getId() == null) {
                // 表示是通过找回密码【邮箱进入的此方法】
                userMapper.findOptionalByEmail(user.getEmail()).ifPresent(
                        // 添加id
                        userEmail -> user.withEmail(userEmail.getEmail())
                );
            }
            User userToUpdate = new User();
            userToUpdate.withId(user.getId())
                    .withPassword(passwordEncoder.encode(newPassword));
            userMapper.updateById(userToUpdate);
        } catch (Exception e) {
            throw new CustomException(ERROR_CODE, "更新密码出错！Exception:" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void activeUser(User user, String role) {
        roleMapper.findOptionalByAuthority(role)
                .ifPresentOrElse(
                        roleToAdd -> {
                            Set<Role> authorities = user.getAuthorities();
                            authorities.add(roleToAdd);
                            roleMapper.addUserRole(user.getId(), roleToAdd.getId());
                        },
                        () -> {
                            throw new NoSuchElementException("Cannot find role!");
                        }
                );
        userMapper.activeUser(user.getId());
        if (!userInfoService.isUserInfoExsisted(user.getId())) {
            // 不存在则新建空信息
            userInfoService.addUserInfo(null, user.getId());
        } else {
            //TODO： 将用户账号与用户信息 user_info 进行绑定
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateById(User user) {
        userMapper.updateById(user);
    }


}
