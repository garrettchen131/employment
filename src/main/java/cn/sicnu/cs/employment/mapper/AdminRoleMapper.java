package cn.sicnu.cs.employment.mapper;

import cn.sicnu.cs.employment.domain.entity.AdminRole;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.domain.vo.QueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    Long getAdminById(@Param("id")Long id);

//    void insertRole(@Param("adminId") Long adminId, @Param("roleId") Long roleId, @P);

//    List<User> listRolesByAdminId(@Param("adminId")Long id);

    void deleteRole(@Param("adminId") Long adminId, @Param("roleId") Long roleId);

    Page<AdminRole> getAllAdminRoleByPaging(QueryVo query);

    Long countRoleByAdmin(Long adminId, Long roleId);

}
