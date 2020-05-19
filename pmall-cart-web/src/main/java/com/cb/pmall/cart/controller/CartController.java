package com.cb.pmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cb.pmall.annotations.LoginRequired;
import com.cb.pmall.beans.OmsCartItem;
import com.cb.pmall.beans.PmsSkuInfo;
import com.cb.pmall.service.CartService;
import com.cb.pmall.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 陈彬
 * date 2020/4/23 9:34 AM
 */
@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    @LoginRequired
    @RequestMapping("addToCart")
    public String addToCart(Integer quantity, String skuId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        // 展示success.html的前端页面(失败!!!!!!!!!!)
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId, null);
        // modelAndView.addObject("skuInfo", skuInfo);
        // modelAndView.addObject("skuNum", quantity);
        session.setAttribute("skuNum", quantity);
        // 设置omsCartItem表数据
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getSpuId());
        omsCartItem.setProductName(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuCode("11111111111");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setQuantity(new BigDecimal(quantity));
        // 根据memberId与skuId判断数据库是否添加过该商品
        OmsCartItem omsCartItemByDB = cartService.isOmsCartItemExsitByUserId(memberId, skuId);
        if (omsCartItemByDB == null) {
            // 购物车未添加过该商品
            omsCartItem.setMemberId(memberId);
            omsCartItem.setMemberNickname(nickname);
            omsCartItem.setQuantity(new BigDecimal(quantity));
            cartService.addCart(omsCartItem);
        } else {
            // 购物车添加过该商品
            // 修改数量与单个商品总价
            omsCartItemByDB.setQuantity(omsCartItemByDB.getQuantity().add(omsCartItem.getQuantity()));
            omsCartItemByDB.setTotalPrice(omsCartItemByDB.getQuantity().multiply(omsCartItemByDB.getPrice()));
            cartService.updateCart(omsCartItemByDB);
        }
        // 更新cache
        cartService.flushDB(memberId);
        return "redirect:success.html";

    }

    @RequestMapping("cartList")
    @LoginRequired
    public String cartList(ModelMap map, HttpServletRequest request) {

        String memberId = (String) request.getAttribute("memberId");
        if (memberId == null) {
            // 用户未登录
            // 跳转登录页面

        } else {
            // 用户已登录
            // 获取所有omsCartItem
            List<OmsCartItem> omsCartItemList = cartService.cartList(memberId);
            map.put("cartList", omsCartItemList);
            map.put("totalAmount", getTotalAmount(omsCartItemList));
        }
        return "cartList";
    }

    @LoginRequired
    @RequestMapping("checkCart")
    public String checkCart(String isChecked, String skuId, ModelMap map, HttpServletRequest request) {
        String memberId = (String) request.getAttribute("memberId");
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setIsChecked(isChecked);
        cartService.checkCart(omsCartItem);
        // 刷新cache
        //cartService.flushDBIsCheckOnly(memberId,skuId);
        cartService.flushDB(memberId);
        // 获取omsCartItems并传回前端
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
        map.put("cartList", omsCartItems);
        BigDecimal totalAmount = getTotalAmount(omsCartItems);
        map.put("totalAmount", totalAmount);
        return "cartListInner";
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0.00");
        for (OmsCartItem omsCartItem : omsCartItems) {
            if (omsCartItem.getIsChecked().equals("1")) {
                // 选中状态,则算入总价
                BigDecimal total = omsCartItem.getQuantity().multiply(omsCartItem.getPrice());
                totalAmount = totalAmount.add(total);
            }
        }
        return totalAmount;
    }


}
