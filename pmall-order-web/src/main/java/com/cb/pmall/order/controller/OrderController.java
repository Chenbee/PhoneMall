package com.cb.pmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cb.pmall.annotations.LoginRequired;
import com.cb.pmall.beans.OmsCartItem;
import com.cb.pmall.beans.OmsOrder;
import com.cb.pmall.beans.OmsOrderItem;
import com.cb.pmall.beans.UmsMemberReceiveAddress;
import com.cb.pmall.service.CartService;
import com.cb.pmall.service.OrderService;
import com.cb.pmall.service.UmsMemberReceiveAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 陈彬
 * date 2020/5/14 8:43 PM
 */
@Controller
public class OrderController {

    @Reference
    CartService cartService;

    @Reference
    OrderService orderService;

    @Reference
    UmsMemberReceiveAddressService addressService;


    @LoginRequired
    @RequestMapping("orderList")
    public String orderList(ModelMap map, HttpServletRequest request) {

        return "list";
    }

    @LoginRequired
    @RequestMapping("submitOrder")
    public ModelAndView submitOrder(String receiveAddressId, BigDecimal totalAmount, String tradeCode, ModelMap map, HttpServletRequest request) {
        // 获取memberId
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");
        // 检验交易码
        String success = orderService.checkTradeCode(memberId, tradeCode);
        if (success.equals("success")) {
            // 构建订单
            OmsOrder omsOrder = new OmsOrder();
            omsOrder.setAutoConfirmDay(7);// 自动确认收货时间
            omsOrder.setCreateTime(new Date());
            omsOrder.setMemberId(memberId);
            omsOrder.setMemberUsername(nickname);
            omsOrder.setNote("快点发货");
            String outTradeNo = "pmall";
            outTradeNo = outTradeNo + System.currentTimeMillis();// 将毫秒时间戳拼接到外部订单号
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
            outTradeNo = outTradeNo + sdf.format(new Date());// 将时间字符串拼接到外部订单号
            omsOrder.setOrderSn(outTradeNo);//外部订单号
            omsOrder.setPayAmount(totalAmount);
            omsOrder.setOrderType(1);
            UmsMemberReceiveAddress umsMemberReceiveAddress = addressService.getAddress(receiveAddressId);
            omsOrder.setReceiverCity(umsMemberReceiveAddress.getCity());
            omsOrder.setReceiverDetailAddress(umsMemberReceiveAddress.getDetailAddress());
            omsOrder.setReceiverName(umsMemberReceiveAddress.getName());
            omsOrder.setReceiverPhone(umsMemberReceiveAddress.getPhoneNumber());
            omsOrder.setReceiverPostCode(umsMemberReceiveAddress.getPostCode());
            omsOrder.setReceiverProvince(umsMemberReceiveAddress.getProvince());
            omsOrder.setReceiverRegion(umsMemberReceiveAddress.getRegion());
            // 当前日期加一天，一天后配送
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            Date time = c.getTime();
            omsOrder.setReceiveTime(time);
            omsOrder.setSourceType(0);
            omsOrder.setStatus("0");
            omsOrder.setOrderType(0);
            omsOrder.setTotalAmount(totalAmount);

            // 获取该用户的购物车列表
            List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
            // 创建之后删除的cartItem集合
            List<OmsCartItem> omsCartItemDelList = new ArrayList<>();
            // 创建新Order集合用于存放购物车各个选中商品
            List<OmsOrderItem> omsOrderItems = new ArrayList<>();
            for (OmsCartItem omsCartItem : omsCartItems) {
                if (omsCartItem.getIsChecked().equals("1")) {
                    // 该商品为被选中状态
                    OmsOrderItem omsOrderItem = new OmsOrderItem();
                    omsOrderItem.setProductName(omsCartItem.getProductName());
                    omsOrderItem.setProductPic(omsCartItem.getProductPic());
                    omsOrderItem.setOrderSn(outTradeNo);// 外部订单号，用来和其他系统进行交互，防止重复
                    omsOrderItem.setProductCategoryId(omsCartItem.getProductCategoryId());
                    omsOrderItem.setProductPrice(omsCartItem.getPrice());
                    omsOrderItem.setRealAmount(omsCartItem.getTotalPrice());
                    omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                    omsOrderItem.setProductSkuCode("111111111111");
                    omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());
                    omsOrderItem.setProductId(omsCartItem.getProductId());
                    omsOrderItem.setProductSn("仓库对应的商品编号");// 在仓库中的skuId
                    // 存入集合
                    omsOrderItems.add(omsOrderItem);
                    omsCartItemDelList.add(omsCartItem);
                }
            }
            // 将orderItems存入到OmsOrder里
            omsOrder.setOmsOrderItems(omsOrderItems);
            // 将order存入到数据库
            String orderId = orderService.saveOrder(omsOrder);
            // 删除购物车内内容
            // cartService.removeItems(omsCartItemDelList,memberId);
            // 重定向到支付页面
            ModelAndView mv = new ModelAndView("redirect:http://localhost:8087/index?orderId="+orderId+"&totalAmount="+totalAmount);
            return mv;
        }else {
            ModelAndView mv = new ModelAndView("tradeFail");
            return mv;
        }
    }

    @LoginRequired
    @RequestMapping("toTrade")
    public String toTrade(ModelMap map, HttpServletRequest request) {
        // 获取用户id
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        // 创建新Order集合用于存放购物车各个选中商品
        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        // 获取该用户的购物车列表
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);

        // 计算出总金额并返回
        BigDecimal totalAmount = getTotalAmount(omsCartItems);
        map.put("totalAmount", totalAmount);

        for (OmsCartItem omsCartItem : omsCartItems) {
            if (omsCartItem.getIsChecked().equals("1")) {
                // 该商品为被选中状态
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                omsOrderItem.setProductName(omsCartItem.getProductName());
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                omsOrderItems.add(omsOrderItem);
            }
        }
        map.put("omsOrderItems", omsOrderItems);

        // 查询收货地址并存入到ModelMap中
        List<UmsMemberReceiveAddress> addressList = addressService.getAllAddress(memberId);
        map.put("userAddressList", addressList);

        // 生成交易码，为了在提交订单时做交易码的校验
        String tradeCode = orderService.getTradeCode(memberId);
        map.put("tradeCode", tradeCode);
        return "trade";
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
