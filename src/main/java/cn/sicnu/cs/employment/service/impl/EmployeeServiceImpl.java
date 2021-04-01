package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.EmployeeExpEducation;
import cn.sicnu.cs.employment.domain.entity.EmployeeExpInternship;
import cn.sicnu.cs.employment.domain.entity.EmployeeExpProject;
import cn.sicnu.cs.employment.domain.entity.EmployeeInfo;
import cn.sicnu.cs.employment.exception.CustomException;
import cn.sicnu.cs.employment.mapper.EmployeeExpMapper;
import cn.sicnu.cs.employment.mapper.EmployeeInfoMapper;
import cn.sicnu.cs.employment.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Set;

import static cn.sicnu.cs.employment.common.Constants.SAVED_ERROR;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmployeeInfoMapper employeeInfoMapper;
    private final EmployeeExpMapper employeeExpMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addUserInfo(EmployeeInfo employeeInfo, Long userId) {
        EmployeeInfo info = employeeInfo.withUserId(userId);
        if (ObjectUtils.isEmpty(employeeInfoMapper.selectById(userId))) {
            employeeInfoMapper.insert(info);
        } else {
            employeeInfoMapper.updateById(info);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public EmployeeInfo getUserInfo(Long userId) {
        return employeeInfoMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateHeadImg(Long userId, String path) {
        int updated = employeeInfoMapper.updateHeadImg(userId, path);
        if (updated < 1){
            throw new CustomException(SAVED_ERROR, "保存头像失败！");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)

    public String getHeadImg(Long id) {
        return employeeInfoMapper.selectHeadImg(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)

    public boolean isUserInfoExisted(Long userId) {
        return employeeInfoMapper.countByUserId(userId) > 0;
    }

    @Override
    public Set<EmployeeExpEducation> getEducationExp(Long id) {
        return employeeExpMapper.selectEducationExp(id);
    }

    @Override
    public Set<EmployeeExpInternship> getInternshipExp(Long id) {
        return employeeExpMapper.selectInternshipExp(id);

    }

    @Override
    public Set<EmployeeExpProject> getProjectExp(Long id) {
        return employeeExpMapper.selectProjectExp(id);

    }

}
