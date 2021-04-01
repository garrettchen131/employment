package cn.sicnu.cs.employment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("t_emp_exp_internship")
public class EmployeeExpInternship {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long empId;

    private String com; //实习公司

    private String department; //实习部门

    private String position; //实习职位

    private String start; //开始时间

    private String end; //结束时间

    private String content; //实习内容

    private String skill; //实习技能


}