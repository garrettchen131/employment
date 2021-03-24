package cn.sicnu.cs.employment.mapper;

import cn.sicnu.cs.employment.domain.entity.CompanyRecruit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@Deprecated
public interface CompanyRecruitMapper extends BaseMapper<CompanyRecruit> {
    Long countById(@Param("id")Long id);

    List<CompanyRecruit> listByComId(Long comId);
}
