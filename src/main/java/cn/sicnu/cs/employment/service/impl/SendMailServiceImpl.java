package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.common.util.RedisUtils;
import cn.sicnu.cs.employment.common.util.RequestUtil;
import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.service.ISendMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import static cn.sicnu.cs.employment.common.Constants.TIME_OUT;
import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

import java.security.SecureRandom;

import static cn.sicnu.cs.employment.common.Constants.OTHER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMailServiceImpl implements ISendMailService {

    private final JavaMailSenderImpl javaMailSender;
    private final RedisUtils redisUtils;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendEmail( String receiver) throws MailSendException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("【中小型企业人才管理系统】邮箱验证");
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setFrom(sender);

        // 生成验证码
        String verCode = generateCode();
        simpleMailMessage.setText(
                "验证码："
                        + verCode
                        + "，本次验证码五分钟内有效，请及时输入。（请勿泄露此验证码）\n"
        );
        // 发送邮件
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            throw new CustomException(OTHER_ERROR, "邮件发送失败！");
        }

        try {
            Long userId = getCurrentUser().getId();
            // 保存到Redis
            String key = "EmailCode:" + userId + ":code";
            long expTime = 5 * 60;
            redisUtils.set(key, verCode, expTime);
        } catch (Exception e) {
            throw new CustomException(OTHER_ERROR, "Redis出现错误，" + e.getMessage());
        }
    }

    /**
     * 验证邮箱验证码是否正确
     *
     * @param code 验证码
     */
    @Override
    public boolean checkVerifyCode( String code) {
        Long userId = getCurrentUser().getId();
        String key = "EmailCode:" + userId + ":code";
        String redisCode = (String) redisUtils.get(key);
        if (redisCode == null) {
            throw new CustomException(TIME_OUT, "验证码已经过期！请重新发送！");
        }
        if (redisCode.equals(code)){
            redisUtils.expire(key, 0);
            return true;
        } else {
            return false;
        }

}

    public String generateCode() {
        char[] nonceChars = new char[6];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = "01234567890".charAt(new SecureRandom().nextInt(10));
        }
        return new String(nonceChars);
    }
}