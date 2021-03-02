package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.BaseTest;
import cn.sicnu.cs.employment.domain.entity.User;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class UserServiceTest extends BaseTest {
    @Autowired
    private IUserService userService;

    @Test
    public void testRegister() {
        userService.register(User.builder()
                .username("abcde")
                .email("abcde@qq.com")
                .password("passwordabcde")
                .mobile("17381579851")
                .build());

    }

    @Test
    public void testIsExistedUsername() {
        System.out.println(userService.isUsernameExisted("zxcc"));
        System.out.println(userService.isUsernameExisted("zxc"));
    }

    @Test
    public void testBean() {
        val user = User.builder().username("zxc").build();
        System.out.println(user);
    }
}
