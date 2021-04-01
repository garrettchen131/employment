package cn.sicnu.cs.employment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Set;

@With
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName("t_bg")
public class BackgroundInfo {

    @TableId(type = IdType.AUTO)
    private Long id; //记录id

    private Long empId; //绑定员工id

    private Long judgeId; //绑定评价人的id

    private Integer type; //评价类型【阶段性评价、培训经历、奖惩、面试结果】

    private String project; //评价对应的项目

    private String proDescription; //评价对应的项目的描述

    private String proPosition; //评价对应的项目中担任的职务

    private String proTime; //评价对应的项目的开始时间

    private String contribution; //员工对该项目的贡献

    private String attitude; // 态度

    private String content; //内容

    private String time; //时间

    private Set<String> img; //图片


}