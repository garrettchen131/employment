package cn.sicnu.cs.employment.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryVo {

    private Integer pageNum; //当前页数

    private Integer pageSize; //每页显示页数

    private String username; //用户名

    private Long userId; //用户id

}