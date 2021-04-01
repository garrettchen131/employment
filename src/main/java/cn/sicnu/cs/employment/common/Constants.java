package cn.sicnu.cs.employment.common;

public class Constants {

    public static final String PROBLEM_BASE_URI = "https://sicnu.cn";

    public static final String ROLE_PERSON = "ROLE_PERSON";
    public static final String ROLE_ENTERPRISE = "ROLE_ENTERPRISE";
    public static final String ROLE_ENTERPRISE_SUPER = "ROLE_ENTERPRISE_SUPER";
    public static final String ROLE_UNIVERSITY = "ROLE_UNIVERSITY";
    public static final String ROLE_UNIVERSITY_SUPER = "ROLE_UNIVERSITY_SUPER";

    // 成功
    public static final int SUCCESS_CODE = 1;
    // 成功提示信息
    public static final String SUCCESS_MESSAGE = "Successful.";
    // 错误
    public static final int ERROR_CODE = 500;
    // 未登录
    public static final int NO_LOGIN_CODE = -100;
    // 请登录提示信息
    public static final String NO_LOGIN_MESSAGE = "Please login.";
    // 错误提示信息
    public static final String ERROR_MESSAGE = "Oops! Something was wrong.";

    public static final int USERNAME_EXISTED = -1;
    public static final int EMAIL_EXISTED = -2;
    public static final int MOBILE_EXISTED = -2;
    public static final int TIME_OUT = -3;
    public static final int PASSWORD_ERROR = -4;
    public static final int INPUT_EMPTY = -5;

    // 其他错误
    public static final int OTHER_ERROR = -9;
    public static final int SAVED_ERROR = -98;
    public static final int USER_INPUT_ERROR = -99;



    public static final String PREFIX_HEAD_IMG = "head_img_";
    public static final String PREFIX_COM_LOGO = "com_logo_";

    // 图片输出时补全地址
    public static final String PREFIX_PIC_STORE = "http://" + "qqkwbsi7n.hn-bkt.clouddn.com" + "/";


    //默认密码
    public static final String DEFAULT_PASS = "123456";


}
