package com.cb.pmall.service;

import com.cb.pmall.beans.OmsCartItem;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/24 11:13 PM
 */
public interface CartService {
    OmsCartItem isOmsCartItemExsitByUserId(String memberId, String skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemByDB);

    void flushDB(String memberId);

    void flushDBIsCheckOnly(String memberId,String skuId);

    List<OmsCartItem> cartList(String memberId);

    void checkCart(OmsCartItem omsCartItem);

    void removeItems(List<OmsCartItem> omsCartItemDelList, String memberId);
}
