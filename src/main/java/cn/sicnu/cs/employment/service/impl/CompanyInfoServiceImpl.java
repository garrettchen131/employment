package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.mapper.AdminRoleMapper;
import cn.sicnu.cs.employment.mapper.CompanyInfoMapper;
import cn.sicnu.cs.employment.mapper.UserMapper;
import cn.sicnu.cs.employment.service.ICompanyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static cn.sicnu.cs.employment.common.util.RequestUtil.getCurrentUser;

@Service
@RequiredArgsConstructor
public class CompanyInfoServiceImpl implements ICompanyInfoService {

    private final UserMapper userMapper;
    private final CompanyInfoMapper companyInfoMapper;
    private final AdminRoleMapper adminRoleMapper;

    @Override
    public void addCompanyInfo(CompanyInfo companyInfo, Long comId) {
        CompanyInfo info = companyInfo.withComId(comId);
        if (ObjectUtils.isEmpty(companyInfoMapper.selectById(comId))) {
            companyInfoMapper.insert(info);
        } else {
            companyInfoMapper.updateById(info);
        }
        userMapper.activeUser(comId);
    }

    @Override
    public CompanyInfo getCompanyInfo(Long comId) {
        if (!isComInfoExisted(comId)){
            // 说明该用户为普通管理员，则进行调取超级管理员的公司信息
            comId = adminRoleMapper.getAdminById(comId);
        }
        return companyInfoMapper.selectById(comId);
    }

    @Override
    public String getComNameById(Long comId) {
        return companyInfoMapper.selectComNameById(comId);
    }

    @Override
    public boolean isComInfoExisted(Long id) {
        return companyInfoMapper.countComById(id) > 0;
    }
}