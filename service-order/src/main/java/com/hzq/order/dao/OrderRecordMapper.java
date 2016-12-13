package com.hzq.order.dao;

import com.hzq.base.dao.Dao;
import com.hzq.order.entity.OrderRecord;

public interface OrderRecordMapper extends Dao<OrderRecord,Integer> {
    OrderRecord getOrderRecordByBankOrderNo(String bankOrderNo);
}