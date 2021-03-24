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
@TableName(value = "t_com_info")
@Deprecated
public class CompanyInfo {

    @TableId(type = IdType.INPUT)
    private Long comId; //主键

    private String comName; //公司名称

    private String runTime; //上市时间

    private String address; //公司地址

    private String manager; //公司负责人

    @Builder.Default
    private Integer status = 1; //公司状态（0-未上市；1-已上市）

    private String type; //公司类型

    private String website; //公司网址

    private String detail; //公司简介

}