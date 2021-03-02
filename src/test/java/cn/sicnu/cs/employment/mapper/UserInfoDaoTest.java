package cn.sicnu.cs.employment.mapper;

import cn.sicnu.cs.employment.BaseTest;
import cn.sicnu.cs.employment.domain.entity.UserInfo;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserInfoDaoTest extends BaseTest {

    @Autowired
    private UserInfoMapper userInfoMapper;

    private UserInfo userInfo;

    @BeforeEach
    public void testBefore() {
        userInfo = UserInfo.builder().userId(1L).address("afsa").build();
    }

    @Test
    public void test() {
        val insert = userInfoMapper.insert(userInfo);
        System.out.println(insert);
    }

    @Test
    public void testSelect() {
        val info = userInfoMapper.selectById(1L);
        System.out.println(info);
    }


}
