package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.mapper.RoleMapper;
import cn.sicnu.cs.employment.mapper.UserMapper;
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
    public void register(boolean isCom, User user) {
        String ROLE = isCom ? ROLE_ENTERPRISE : ROLE_PERSON;
        roleMapper.findOptionalByAuthority(ROLE)
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
    public void activeUser(User user) {
        Long id = getCurrentUser().getId();
        userMapper.activeUser(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateById(User user) {
        userMapper.updateById(user);
    }

}
