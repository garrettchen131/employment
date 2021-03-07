package cn.sicnu.cs.employment.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRecruitVo {

    private Long id; //简历id

    private Long comId; //企业id

    private String manager; //招聘负责人

    private String managerStatus; //招聘人职位

    private String position; //招聘职位

    private String money; //职位薪水

    private String description; //岗位描述

    private String workContent; //工作内容

    private String requirement; //工作要求

    private String workPlace; //工作地点

    private String workTime; //工作时间

    private Integer status; //招聘信息状态（0-未发布；1-已发布)

    private String comName; //企业名称

    @Builder.Default
    private String comStatus = "已上市"; //"已上市" “未上市”

    private String website; //公司网址

    private String detail; //公司简介

}