package cn.sicnu.cs.employment.domain.vo;

import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class UserResumeVo {

    private Long id; //记录id

    private Long userId; //简历绑定的用户id

    private String comments; //简历备注

    private String createTime; //简历创建时间

    // ======= Vo =========

    private EmployeeInfo employeeInfo;

}