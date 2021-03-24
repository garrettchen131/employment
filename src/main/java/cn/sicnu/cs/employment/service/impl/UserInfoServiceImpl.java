package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.UserInfo;
import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.mapper.UserInfoMapper;
import cn.sicnu.cs.employment.mapper.UserMapper;
import cn.sicnu.cs.employment.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import static cn.sicnu.cs.employment.common.Constants.SAVED_ERROR;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements IUserInfoService {

    private final UserInfoMapper userInfoMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addUserInfo(UserInfo userInfo, Long userId) {
//        System.out.println(userInfo.getPersonName());
        UserInfo info = userInfo.withUserId(userId);
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

    @Override
    public void updateUserHeadImg(Long userId, String path) {
        int updated = userInfoMapper.updateHeadImg(userId, path);
        if (updated < 1){
            throw new CustomException(SAVED_ERROR, "保存头像失败！");
        }
    }

    @Override
    public String getHeadImg(Long id) {
        return userInfoMapper.selectHeadImg(id);
    }

    @Override
    public boolean isUserInfoExsisted(Long userId) {
        return userInfoMapper.countByUserId(userId) > 0;
    }

}
