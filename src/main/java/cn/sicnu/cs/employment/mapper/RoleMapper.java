package cn.sicnu.cs.employment.mapper;

import cn.sicnu.cs.employment.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;


@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    Optional<Role> findOptionalByAuthority(@Param("authority") String authority);

    int addUserRole(@Param("userId") long userId, @Param("roleId") long roleId);

}
