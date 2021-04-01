package cn.sicnu.cs.employment.domain.vo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleVo {

    private Long id; //人员id

    private String username; //用户名

    private String phone; //联系电话

    private String position; // 职位信息

    private String addTime; //添加时间

    private boolean isEnable; // 账号是否停用



}