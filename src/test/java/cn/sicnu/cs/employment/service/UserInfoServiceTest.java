package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.BaseTest;
import cn.sicnu.cs.employment.domain.entity.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class UserInfoServiceTest extends BaseTest {
    @Autowired
    private IUserInfoService userInfoService;

    private UserInfo userInfo;

    @BeforeEach
    public void testBefore() {
        userInfo = UserInfo.builder().userId(1L).address("afsa").build();
    }


    @Test
    public void testAddInfo() {
        userInfoService.addUserInfo(userInfo.withAddress("aaaa"),1L);

    }


}
