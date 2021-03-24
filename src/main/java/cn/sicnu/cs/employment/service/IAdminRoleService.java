package cn.sicnu.cs.employment.service;


import cn.sicnu.cs.employment.domain.entity.User;

import java.util.List;

public interface IAdminRoleService {

    void authenticateUser(String username);

    List<User> listRoles(Long id);

    void removeAuthenticateUser(String username);
}