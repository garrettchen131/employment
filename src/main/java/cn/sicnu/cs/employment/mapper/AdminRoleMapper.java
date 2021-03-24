package cn.sicnu.cs.employment.mapper;

import cn.sicnu.cs.employment.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface AdminRoleMapper {

    Long getAdminById(@Param("id")Long id);

    void insertRole(@Param("adminId") Long adminId, @Param("roleId") Long roleId);

    List<User> listRolesByAdminId(@Param("adminId")Long id);

    void deleteRole(@Param("adminId") Long adminId, @Param("roleId") Long roleId);
}
