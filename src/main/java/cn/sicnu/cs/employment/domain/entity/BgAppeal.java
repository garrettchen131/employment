package cn.sicnu.cs.employment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Set;

@With
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName("t_bg_appeal")
public class BgAppeal {

    @TableId(type = IdType.AUTO)
    private Long id; //记录id

    private Integer type; //类型【1-申请，2-申诉】

    private Long bgId; //（申诉时）对应申诉评价的id

    private Long empId; //（申请时）对应申请人的id

    private Integer status; //申诉当前的状态（0未发送；1-已发送）

    private String title; // 标题

    private String content; //内容

    private String time; //最近一次更新时间（发送时间）

    private Set<String> img;


    private Long respId; //绑定处理人的id

    private Integer respStatus; //处理状态（-1未处理，0驳回，1批准）

    private String respContent; //回复的内容

    private String respTime; //处理的时间

}