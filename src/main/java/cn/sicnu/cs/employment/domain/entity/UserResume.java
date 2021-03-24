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
@TableName(value = "t_user_resume")
@Deprecated
public class UserResume {

    @TableId(type = IdType.AUTO)
    private Long id; //记录id

    private Long userId; //简历绑定的用户id

    private String comments; //简历备注

    private String createTime; //简历创建时间

}