package cn.sicnu.cs.employment.mapper;

import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyMapper extends BaseMapper<CompanyInfo> {

    String selectComNameById(@Param("id") Long id);

    Long countComById(@Param("id") Long id);

    int updateHeadImg(Long id, String path);

    String selectHeadImg(Long id);

    List<EmployeeInfo> listEmployeesByStatus(@Param("comId")Long comId, @Param("status")Long status);

}