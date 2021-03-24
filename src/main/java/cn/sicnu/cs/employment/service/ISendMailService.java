package cn.sicnu.cs.employment.service;

public interface ISendMailService {
    void sendEmail(String receiver);

    boolean checkVerifyCode( String code);
}
