package cn.sicnu.cs.employment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmployeeCompanyMapper{

    void insertEmployeeCompany(@Param("empId")Long empId);

    void addEmployeeToCompany(@Param("time")String time, @Param("empId")Long empId, @Param("comId")Long comId);

    void deprecateEmployeeToCompany(@Param("time")String time, @Param("empId")Long empId, @Param("comId")Long comId);

    String getInTime(@Param("empId")Long empId, @Param("comId")Long comId);

    String getOffTime(@Param("empId")Long empId, @Param("comId")Long comId);
}
