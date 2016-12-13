package com.hzq.accounting.dao;

import com.hzq.accounting.entity.Accounting;

public interface AccountingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Accounting record);

    int insertSelective(Accounting record);

    Accounting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Accounting record);

    int updateByPrimaryKey(Accounting record);
}