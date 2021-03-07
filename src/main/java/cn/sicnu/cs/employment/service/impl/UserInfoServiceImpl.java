package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.UserInfo;
import cn.sicnu.cs.employment.mapper.UserInfoMapper;
import cn.sicnu.cs.employment.mapper.UserMapper;
import cn.sicnu.cs.employment.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements IUserInfoService {

    private final UserInfoMapper userInfoMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addUserInfo(UserInfo userInfo, Long userId) {
        UserInfo info = userInfo.withUserId(userInfo.getUserId());
        if (ObjectUtils.isEmpty(userInfoMapper.selectById(userId))) {
            userInfoMapper.insert(info);
        } else {
            userInfoMapper.updateById(info);
        }
    }

    @Override
    public UserInfo getUserInfo(Long userId) {
        return userInfoMapper.selectById(userId);
    }
}
