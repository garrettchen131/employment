package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.service.ISendMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

import static cn.sicnu.cs.employment.common.Constants.OTHER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMailServiceImpl implements ISendMailService {

    private final JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendEmail(String receiver) throws MailSendException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("【中小型企业人才管理系统】邮箱验证");
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setFrom(sender);

        // 生成验证码
        String verCode = generateCode();
        simpleMailMessage.setText(
                "验证码："
                +verCode
                +"，本次验证码五分钟内有效，请及时输入。（请勿泄露此验证码）\n"
        );
        // 发送邮件
        try {
            javaMailSender.send(simpleMailMessage);
        }catch (Exception e){
            throw new CustomException(OTHER_ERROR, "邮件发送失败！");
        }
        return verCode;
    }

    public String generateCode(){
        char[] nonceChars = new char[6];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = "01234567890".charAt(new SecureRandom().nextInt(10));
        }
        return new String(nonceChars);
    }
}