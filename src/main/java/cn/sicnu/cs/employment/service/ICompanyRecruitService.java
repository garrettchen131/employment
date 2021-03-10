package cn.sicnu.cs.employment.service;


import cn.sicnu.cs.employment.domain.entity.CompanyRecruit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ICompanyRecruitService{

    boolean isRecruitExisted(Long recruitId);

    boolean updateById(CompanyRecruit recruit);

    CompanyRecruit getById(Long recruitId);

    boolean removeById(Long recruitId);

    List<CompanyRecruit> listByComId(Long comId);

    void save(CompanyRecruit recruit);

    List<CompanyRecruit> getAll();
}