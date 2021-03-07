package cn.sicnu.cs.employment.common.util;


import cn.sicnu.cs.employment.common.ResultInfo;
import cn.sicnu.cs.employment.common.ResultInfoUtil;
import cn.sicnu.cs.employment.domain.entity.User;
import cn.sicnu.cs.employment.exception.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

import static cn.sicnu.cs.employment.common.Constants.*;


public class RequestUtil {

    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static String getCurrentUrl() {
        return getCurrentRequest().getServletPath();
    }

    public static User getCurrentUser() {
        return (User) getCurrentRequest().getAttribute("user");
    }

    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 验证邮箱验证码是否正确
     * @param verifyCode 验证码
     */
    public static void checkVerifyCode(String verifyCode) {
        HttpSession session = getCurrentRequest().getSession();
        String emailCode = (String) session.getAttribute("emailCode");
        if (emailCode == null) {
            throw new CustomException(OTHER_ERROR, "请重新发送验证码！");
        }
        Long emailTime = (Long) session.getAttribute("emailTime");
        if (System.currentTimeMillis() - emailTime > 1000 * 60 * 5) {
            throw new CustomException(TIME_OUT, "验证码已超过有效期！");
        }
        if (!emailCode.equals(verifyCode)) {
            throw new CustomException(ERROR_CODE, "验证码错误！");
        }
        session.removeAttribute("emailCode");
        session.removeAttribute("emailTime");
    }
}

