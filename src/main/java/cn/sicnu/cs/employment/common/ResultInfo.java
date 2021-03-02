package cn.sicnu.cs.employment.common;

import lombok.*;

import java.io.Serializable;

/**
 * 公共返回对象
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResultInfo<T> implements Serializable {
    
    private Integer code;
    private String message;
    private String path;
    private T data;
    
}