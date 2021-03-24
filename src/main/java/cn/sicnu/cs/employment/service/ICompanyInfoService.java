package cn.sicnu.cs.employment.service;

import cn.sicnu.cs.employment.domain.entity.CompanyInfo;

public interface ICompanyInfoService {
    /**
     * 添加/修改公司信息
     */
    void addCompanyInfo(CompanyInfo companyInfo, Long userId);

    CompanyInfo getCompanyInfo(Long userId);

    String getComNameById(Long comId);

    boolean isComInfoExisted(Long id);
}
