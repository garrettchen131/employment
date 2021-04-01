package cn.sicnu.cs.employment.service;


import cn.sicnu.cs.employment.domain.entity.AdminRole;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.QueryVo;
import com.github.pagehelper.Page;

import java.util.HashMap;
import java.util.Map;

public interface IAdminRoleService {

    void authenticateUser(User user, String info);

    Page<AdminRole> listRoles(QueryVo query);

    void removeAuthenticateUser(String username);

    Long getAdminById(Long comId);

    boolean hasRoleUser(Long roleId);
}