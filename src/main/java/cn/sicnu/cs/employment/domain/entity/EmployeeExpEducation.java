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
@TableName("t_emp_exp_education")
public class EmployeeExpEducation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long empId;

    private String school; //学校名称

    private String type; //类型（全日制）

    private String major; //专业

    private String level; //等级（本科、研究生）

    private String start; //开始时间

    private String end; //结束时间

    private String experience ; //经历（课代表）

}