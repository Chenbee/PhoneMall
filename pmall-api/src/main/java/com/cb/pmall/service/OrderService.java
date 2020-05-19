package com.cb.pmall.service;

import com.cb.pmall.beans.OmsOrder;

/**
 * @author 陈彬
 * date 2020/5/14 8:55 PM
 */
public interface OrderService {
    String getTradeCode(String memberId);

    String checkTradeCode(String memberId, String tradeCode);

    String saveOrder(OmsOrder omsOrder);

    OmsOrder getOrderById(String orderId);

    void updateOrder(OmsOrder omsOrder);
}
