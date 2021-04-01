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
@TableName("t_emp_exp_project")
public class EmployeeExpProject {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long empId;

    private String project; //项目名称

    private String role; //项目角色

    private String link; //项目连接

    private String start; //开始时间

    private String end; //结束时间

    private String description; //项目描述

    private String summary; //项目总结
}