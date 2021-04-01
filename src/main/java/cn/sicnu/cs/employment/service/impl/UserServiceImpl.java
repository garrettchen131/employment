package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.*;
import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.mapper.*;
import cn.sicnu.cs.employment.service.ICompanyService;
import cn.sicnu.cs.employment.service.IEmployeeService;
import cn.sicnu.cs.employment.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    private final AdminRoleMapper adminRoleMapper;
    private final IEmployeeService employeeService;
    //    private final EmployeeInfoMapper employeeInfoMapper;
    private final ICompanyService companyService;
    //    private final CompanyMapper companyMapper;
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
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void register(User user) {
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
                        userEmail -> user.setId(userEmail.getId())
                );
            }
            User userToUpdate = user
                    .withId(user.getId())
                    .withPassword(passwordEncoder.encode(newPassword));
            userMapper.updateById(userToUpdate);
        } catch (Exception e) {
            throw new CustomException(ERROR_CODE, "更新密码出错！Exception:" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void activeEmp(User user) {
        userMapper.activeUser(user.getId()); // 将用户status改为1
        // 认证普通用户(即员工)
        if (!employeeService.isUserInfoExisted(user.getId())) {
            // 不存在则新建空信息
            EmployeeInfo employeeInfoToAdd = new EmployeeInfo().withUserId(user.getId());
            employeeService.addUserInfo(employeeInfoToAdd, user.getId());
        } else {
            //TODO： 将用户账号与用户信息 user_info 进行绑定
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void activeAdmin(User user, String info) {
        String roleToAdd;
        Set<Role> auth = getCurrentUser().getAuthorities();
        if (auth.contains(new Role((long) 3, ROLE_ENTERPRISE_SUPER))) {
            // 说明是企业用户在进行添加
            roleToAdd = ROLE_ENTERPRISE;
        } else {
            roleToAdd = ROLE_UNIVERSITY;
        }
        // 拿到二级管理员权限
        roleMapper.findOptionalByAuthority(roleToAdd)
                .ifPresentOrElse(
                        role -> {
                            var userToSave = user
                                    .withAuthorities(Set.of(role))
                                    .withPassword(passwordEncoder.encode(DEFAULT_PASS))
                                    .withStatus(true);
                            userMapper.insert(userToSave);
                            roleMapper.addUserRole(userToSave.getId(), role.getId());
                            // 保存两者的联系
                            var adminRole = AdminRole.builder()
                                    .adminId(getCurrentUser().getId())
                                    .roleId(userToSave.getId())
                                    .info(info)
                                    .build();
                            adminRoleMapper.insert(adminRole);
                        },
                        () -> {
                            throw new NoSuchElementException("Cannot find role!");
                        }
                );
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateById(User user) {
        userMapper.updateById(user);
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userMapper.selectIdByUsername(username);
    }

    @Override
    public User getUserById(Long roleId) {
        return userMapper.selectById(roleId);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        boolean statusToUpdate = status == 1;
        userMapper.updateById(new User().withId(id).withStatus(statusToUpdate));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void activeSuperAdmin(String role) {
        roleMapper.findOptionalByAuthority(role).ifPresentOrElse(
                roleToAuth -> {
                    var userToUpdate = User.builder()
                            .id(getCurrentUser().getId())
                            .authorities(Set.of(roleToAuth))
                            .status(true).build();
                    userMapper.updateById(userToUpdate);
                    companyService.addCompanyInfo(new CompanyInfo(), userToUpdate.getId());
                    roleMapper.addUserRole(userToUpdate.getId(), roleToAuth.getId());
                },
                () -> {
                    throw new NoSuchElementException("Cannot find role!");
                }
        );
    }
}

