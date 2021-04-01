package cn.sicnu.cs.employment.domain.vo;


import cn.sicnu.cs.employment.domain.entity.EmployeeExpEducation;
import cn.sicnu.cs.employment.domain.entity.EmployeeExpInternship;
import cn.sicnu.cs.employment.domain.entity.EmployeeExpProject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Date;
import java.util.Set;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfoVo {

    private Long id; //用户真正的信息id

    private Long userId;  // 绑定用户账号的id

    private Long comId; //绑定公司id

    private String personName; //姓名

    private String gender; //性别

    private String birthDay; //出生年月

    private String wechat ;//微信

    private String address; //所在地址

    private String mobile; //电话

    private Integer nowStatus; // 当前状态编号

    private String nowStatusName; //当前状态名称

    private String graduateStatus; //毕业状态名称（22年应届生）

    private String graduateLevel; //毕业等级（本科、研究生）

    private String advantage; //个人优势

    private String graduateSchool; //毕业院校

    private String major; //专业

    private String education;  //学历、

    private String signature; //个性签名

    private String headImg;  //头像链接

    // =============VO 各种经历===============

    private Set<EmployeeExpEducation> educationExp; // 教育经历

    private Set<EmployeeExpInternship> internshipExp; // 实习经历

    private Set<EmployeeExpProject> projectExp; //项目经历

    //===========VO 与公司的关系 ================
    private String inTime; //入职时间

    private String offTime; //离职时间
    

}
