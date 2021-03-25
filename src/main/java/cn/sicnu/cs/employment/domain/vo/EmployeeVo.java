package cn.sicnu.cs.employment.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Date;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeVo {

    private String username; //账号

    private Long comId; // 当前公司id

    private String nickName; //昵称

    private String mobile; //手机

    private String email; //邮箱

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

    private String headImg;  //头像链接
}
