package cn.sicnu.cs.employment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@With
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName("t_admin_role")
public class AdminRole {

//    @TableId(type = IdType.INPUT)
    private Long adminId; //一级管理员

//    @TableId(type = IdType.INPUT)
    private Long roleId; //二级管理员

    private String info; //二级管理员的信息

    private String mobile; //二级管理员的联系方式

}