package cn.sicnu.cs.employment.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.validation.constraints.NotNull;

@With
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

    private String email; //公司邮箱

    private String mobile; //公司联系方式

    private String username; //登录账号

}