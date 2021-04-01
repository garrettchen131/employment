package cn.sicnu.cs.employment.common.util;


import cn.sicnu.cs.employment.exception.CustomException;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static cn.sicnu.cs.employment.common.Constants.OTHER_ERROR;

@Deprecated
@Component
@Slf4j
@RequiredArgsConstructor
public class KaptchaUtil {

    private final DefaultKaptcha defaultKaptcha;

    public void getKaptchaJpg(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        byte[] captchaChallengeAsJpeg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            httpServletRequest.getSession().setAttribute("kapCode", createText);
            log.info("生成验证码={}", createText);
            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
            // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
            captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
            httpServletResponse.setHeader("Cache-Control", "no-store");
            httpServletResponse.setHeader("Pragma", "no-cache");
            httpServletResponse.setDateHeader("Expires", 0);
            httpServletResponse.setContentType("image/jpeg");
            ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
            responseOutputStream.write(captchaChallengeAsJpeg);
            responseOutputStream.flush();
            responseOutputStream.close();
        } catch (Exception e) {
            throw new CustomException(OTHER_ERROR, "验证码图片生成错误");
        }
    }

    public boolean checkKaptchaCode(HttpServletRequest httpServletRequest, String getCode) {
        String kapCode = (String) httpServletRequest.getSession().getAttribute("kapCode");
        if (kapCode != null) {
            if (kapCode.equals(getCode)) {
                httpServletRequest.getSession().removeAttribute("kapCode");
                return true;
            } else {
                return false;
            }
        } else {
            throw new CustomException(OTHER_ERROR, "请重新生成验证码");
        }
    }
}