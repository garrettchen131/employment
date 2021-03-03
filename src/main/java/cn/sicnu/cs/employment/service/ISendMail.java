package cn.sicnu.cs.employment.service;

import org.springframework.mail.MailSendException;

/**
 * @author CaiKe
 * @create 2021/3/3
 */
public interface ISendMail {

    String sendEmail(String receiver) throws MailSendException;
}
