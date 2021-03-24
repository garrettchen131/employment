package cn.sicnu.cs.employment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName(value = "t_resume_recruit")
@Deprecated
public class ResumeRecruit {

    @TableId(type = IdType.AUTO)
    private Long id; // 记录id

    private Long resumeId; // 简历表id

    private Long recruitId; // 招聘表id

    private Integer status; //简历状态（1-已投递；2-已查看；3-已收藏；4-待面试；5-不合适）

    private String sendTime; //投递时间

    private String favoTime; //收藏时间

    private String rejectTime; //拒绝时间
}