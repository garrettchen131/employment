package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.CompanyInfo;
import cn.sicnu.cs.employment.mapper.CompanyInfoMapper;
import cn.sicnu.cs.employment.mapper.UserMapper;
import cn.sicnu.cs.employment.service.ICompanyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class CompanyInfoServiceImpl implements ICompanyInfoService {

    private final UserMapper userMapper;
    private final CompanyInfoMapper companyInfoMapper;

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
        return companyInfoMapper.selectById(comId);
    }
}