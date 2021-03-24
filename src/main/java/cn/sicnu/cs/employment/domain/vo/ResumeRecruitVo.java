package cn.sicnu.cs.employment.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class ResumeRecruitVo {

    private Long id; // 记录id

    private Long resumeId; // 简历表id

    private Long recruitId; // 招聘表id

    private Integer status; //简历状态（1-已投递；2-已查看；3-已收藏；4-待面试；5-不合适）

    private String sendTime; //投递时间

    private String favoTime; //收藏时间

    private String rejectTime; //拒绝时间

    // VO

    private String position; //投递的职位名称

    private String address; //投递职业所在地区

    private String request; //投递需求

    private String[] detail; // 投递要求[数组]
}