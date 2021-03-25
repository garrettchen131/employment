package cn.sicnu.cs.employment.mapper;

import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CompanyInfoMapper extends BaseMapper<CompanyInfo> {

    String selectComNameById(@Param("id") Long id);

    Long countComById(@Param("id") Long id);

    int updateHeadImg(Long id, String path);

    String selectHeadImg(Long id);
}