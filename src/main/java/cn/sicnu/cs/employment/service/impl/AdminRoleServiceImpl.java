package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.mapper.AdminRoleMapper;
import cn.sicnu.cs.employment.service.IAdminRoleService;
import cn.sicnu.cs.employment.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sicnu.cs.employment.common.Constants.ERROR_CODE;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
public class AdminRoleServiceImpl implements IAdminRoleService {
    private final IUserService userService;
    private final AdminRoleMapper adminRoleMapper;

    @Override
    public void authenticateUser(String username) {
        if (! userService.isUsernameExisted(username)) {
            throw new CustomException(ERROR_CODE, "找不到授权用户！");
        }
        Long roleId = userService.getUserIdByUsername(username);
        Long adminId = getCurrentUser().getId();
        adminRoleMapper.insertRole(adminId, roleId);
    }

    @Override
    public List<User> listRoles(Long id) {
        return adminRoleMapper.listRolesByAdminId(id);
    }

    @Override
    public void removeAuthenticateUser(String username) {
        if (! userService.isUsernameExisted(username)) {
            throw new CustomException(ERROR_CODE, "找不到目标用户！");
        }
        Long roleId = userService.getUserIdByUsername(username);
        Long adminId = getCurrentUser().getId();
        adminRoleMapper.deleteRole(adminId, roleId);
    }

    @Override
    public Long getAdminById(Long comId) {
        return adminRoleMapper.getAdminById(comId);
    }
}