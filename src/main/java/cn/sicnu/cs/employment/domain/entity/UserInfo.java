package cn.sicnu.cs.employment.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("t_user_info")
public class UserInfo {

    @TableId(type = IdType.INPUT)
    private Long userId;

    private String personName; //姓名

    private String birthDay; //出生年月

    private String address; //所在地址

    private String homeAddress; //户籍地址

    private String education;  //学历

    private String major;  //专业

    private String certificate;  //证书

    private String school;  //学校

    private String schoolStart;  //入学年月

    private String schoolEnd; // 毕业年月

    private String training;  //培训经历

    private String skill;  //技能

    private String comName;  //所在公司

    private String position;  //职位

    private String workStart;  //入职年月

    private String workEnd;  //离职年月

    private String workContent;  //工作内容

    private String workCharacter;  //工作性质

    private String exStatus;  //求职状态

    private String exSalary;   //期望资薪

    private String exAddress;  //期望工作地点

    private String exPosition;  //期望职位

    private String gender;  //性别

    private String projectEx;  //项目经历

}
