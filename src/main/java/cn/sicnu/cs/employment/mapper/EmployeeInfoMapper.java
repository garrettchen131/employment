package cn.sicnu.cs.employment.mapper;

import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmployeeInfoMapper extends BaseMapper<EmployeeInfo> {

    int updateHeadImg(@Param("id") Long userId, @Param("path") String path);

    String selectHeadImg(@Param("id") Long id);

    long countByUserId(@Param("userId")Long userId);
}
