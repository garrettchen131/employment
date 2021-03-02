package cn.sicnu.cs.employment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@With
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName("t_role")
public class Role implements GrantedAuthority, Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String authority;
}
