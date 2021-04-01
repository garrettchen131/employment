package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.AdminRole;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.QueryVo;
import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.mapper.AdminRoleMapper;
import cn.sicnu.cs.employment.mapper.UserMapper;
import cn.sicnu.cs.employment.service.IAdminRoleService;
import cn.sicnu.cs.employment.service.IUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static cn.sicnu.cs.employment.common.Constants.ERROR_CODE;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
public class AdminRoleServiceImpl implements IAdminRoleService {

    private final UserMapper userMapper;
    private final IUserService userService;
    private final AdminRoleMapper adminRoleMapper;

    @Override
    public void authenticateUser(User user, String info) {
        // 注册该用户并为其添加权限
        userService.activeAdmin(user, info);
    }

    @Override
    public Page<AdminRole> listRoles(QueryVo query) {
//        PageMethod.startPage(query.get("pa"))
        PageMethod.startPage(query.getPageNum(), query.getPageSize());
       return adminRoleMapper.getAllAdminRoleByPaging(query);
    }

    @Override
    public void removeAuthenticateUser(String username) {
        if (userMapper.countByUsername(username) < 1) {
            throw new CustomException(ERROR_CODE, "找不到目标用户！");
        }
        Long roleId = userMapper.selectIdByUsername(username);
        Long adminId = getCurrentUser().getId();
        adminRoleMapper.deleteRole(adminId, roleId);
    }

    @Override
    public Long getAdminById(Long comId) {
        return adminRoleMapper.getAdminById(comId);
    }

    @Override
    public boolean hasRoleUser(Long roleId) {
        return adminRoleMapper.countRoleByAdmin(getCurrentUser().getId(), roleId) > 0;
    }
}