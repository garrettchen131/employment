package cn.sicnu.cs.employment.mapper;

import cn.sicnu.cs.employment.BaseTest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDaoTest extends BaseTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        val user = userMapper.selectById(1);
        System.out.println(user);
//        val roles = userMapper.findRolesByUserId(1L);
//        roles.stream().forEach(System.out::println);
    }

    @Test
    public void testFindOptionalByUsername() {
        val user = userMapper.findOptionalByUsername("zxc");
        System.out.println(user);
        System.out.println(user.getClass());
    }
}
