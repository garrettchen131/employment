package cn.sicnu.cs.employment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@With
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName(value = "t_emp_com")
public class EmployeeCompany {

    @TableId(type = IdType.AUTO)
    private Long id; //记录id

    private Long empId; //员工id

    private Long comId; //公司id

    private Integer status; //当前员工和公司的状态（0招聘； 1-在职； 2-离职）

    private String inTime; //入职时间

    private String offTime; //离职时间

    private String salary; //薪资

    private String position; //职位

}