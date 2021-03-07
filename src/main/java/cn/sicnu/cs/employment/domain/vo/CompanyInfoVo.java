package cn.sicnu.cs.employment.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoVo {

    @NotNull(message = "公司名称不能为空")
    private String comName; //公司名称

    private String runTime; //上市时间

    private String address; //公司地址

    private String manager; //公司负责人

    private Integer status;  //公司状态（0-未上市；1-已上市）

    private String type; //公司类型

    private String website; //公司网址

    private String detail; //公司简介

}