package cn.sicnu.cs.employment.mapper;

import cn.sicnu.cs.employment.domain.entity.EmployeeExpEducation;
import cn.sicnu.cs.employment.domain.entity.EmployeeExpInternship;
import cn.sicnu.cs.employment.domain.entity.EmployeeExpProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface EmployeeExpMapper{

    Set<EmployeeExpEducation> selectEducationExp(@Param("id")Long id);

    Set<EmployeeExpProject> selectProjectExp(@Param("id")Long id);

    Set<EmployeeExpInternship> selectInternshipExp(@Param("id")Long id);
}
