package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.domain.entity.CompanyRecruit;
import cn.sicnu.cs.employment.mapper.CompanyRecruitMapper;
import cn.sicnu.cs.employment.service.ICompanyRecruitService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Deprecated
public class CompanyRecruitServiceImpl implements ICompanyRecruitService {

    private final CompanyRecruitMapper recruitMapper;

    @Override
    public boolean isRecruitExisted(Long recruitId) {
        return recruitMapper.countById(recruitId) > 0;
    }

    @Override
    public boolean updateById(CompanyRecruit recruit) {
        return recruitMapper.updateById(recruit) > 0;
    }

    @Override
    public CompanyRecruit getById(Long recruitId) {
        return recruitMapper.selectById(recruitId);
    }

    @Override
    public boolean removeById(Long recruitId) {
        return recruitMapper.deleteById(recruitId) > 0;
    }

    @Override
    public List<CompanyRecruit> listByComId(Long comId) {
        return recruitMapper.listByComId(comId);
    }

    @Override
    public void save(CompanyRecruit recruit) {
        recruitMapper.insert(recruit);
    }

    @Override
    public List<CompanyRecruit> getAll() {
        return recruitMapper.selectList(null);
    }
}