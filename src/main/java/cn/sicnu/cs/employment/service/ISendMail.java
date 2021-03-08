package cn.sicnu.cs.employment.service;

import org.springframework.mail.MailSendException;

public interface ISendMail {

    String sendEmail(String receiver) throws MailSendException;
}
