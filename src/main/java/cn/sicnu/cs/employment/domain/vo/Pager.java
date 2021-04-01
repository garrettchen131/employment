package cn.sicnu.cs.employment.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pager<T> {

    private Long total; //数据总数

    private List<T> data; //数据

}