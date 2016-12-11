package com.hzq.user.service.impl;

import com.hzq.user.dao.MerchantInfoMapper;
import com.hzq.user.entity.MerchantInfo;
import com.hzq.user.service.MerchantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hzq on 16/12/11.
 */
@Service("merchantInfoService")
public class MerchantInfoServiceImpl implements MerchantInfoService {

    @Autowired
    MerchantInfoMapper merchantInfoMapper;

    @Override
    public MerchantInfo getMerchantInfoById(Integer merchantId) {
        return merchantInfoMapper.getByPk(merchantId);
    }
}
