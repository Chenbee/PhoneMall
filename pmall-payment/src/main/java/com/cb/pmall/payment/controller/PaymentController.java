package com.cb.pmall.payment.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.cb.pmall.annotations.LoginRequired;
import com.cb.pmall.beans.OmsOrder;
import com.cb.pmall.beans.PaymentInfo;
import com.cb.pmall.mq.ActiveMQUtil;
import com.cb.pmall.payment.config.AlipayConfig;
import com.cb.pmall.service.OrderService;
import com.cb.pmall.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈彬
 * date 2020/5/15 5:52 PM
 */
@Controller
public class PaymentController {
    @Autowired
    AlipayClient alipayClient;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Reference
    OrderService orderService;

    @RequestMapping("callback")
    @LoginRequired(loginSuccess = true)
    public String callback(String orderId, HttpServletRequest request, ModelMap modelMap) throws JMSException {
        // 修改支付状态
        OmsOrder omsOrder = orderService.getOrderById(orderId);
        omsOrder.setStatus("1");
        orderService.updateOrder(omsOrder);
        return "finish";
    }

    @RequestMapping("index")
    @LoginRequired(loginSuccess = true)
    public String index(String orderId, BigDecimal totalAmount, HttpServletRequest request, ModelMap modelMap) {
        modelMap.put("orderId", orderId);
        modelMap.put("totalAmount", totalAmount);
        return "paymentindex";
    }


    // 无法验签通过故使用简单的支付款收款码代替

   /*
    @Autowired
    PaymentService paymentService;

   @RequestMapping("/alipay/callback/return")
    @LoginRequired(loginSuccess = true)
    public String aliPayCallBackReturn(HttpServletRequest request, ModelMap modelMap) {

        // 回调请求中获取支付宝参数
        String sign = request.getParameter("sign");
        String trade_no = request.getParameter("trade_no");
        String out_trade_no = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String subject = request.getParameter("subject");
        String call_back_content = request.getQueryString();


        // 通过支付宝的paramsMap进行签名验证，2.0版本的接口将paramsMap参数去掉了，导致同步请求没法验签
        if (StringUtils.isNotBlank(sign)) {
            // 验签成功
            // 更新用户的支付状态

            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setOrderSn(out_trade_no);
            paymentInfo.setPaymentStatus("已支付");
            paymentInfo.setAlipayTradeNo(trade_no);// 支付宝的交易凭证号
            paymentInfo.setCallbackContent(call_back_content);//回调请求字符串
            paymentInfo.setCallbackTime(new Date());

            paymentService.updatePayment(paymentInfo);

        }

        return "finish";
    }


    @RequestMapping("/alipay/submit")
    @LoginRequired(loginSuccess = true)
    @ResponseBody
    public String alipay(String outTradeNo, BigDecimal totalAmount, HttpServletRequest request, ModelMap modelMap) {

        // 获得一个支付宝请求的客户端(它并不是一个链接，而是一个封装好的http的表单请求)
        String form = null;
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request

        // 回调函数
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);

        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", outTradeNo);
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");
        map.put("total_amount", 0.01);
        map.put("subject", "商品价格为0.01是为了测试");

        String param = JSON.toJSONString(map);

        alipayRequest.setBizContent(param);

        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
             System.out.println(form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        // 生成并且保存用户的支付信息
        OmsOrder omsOrder = orderService.getOrderByOutTradeNo(outTradeNo);
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(omsOrder.getId());
        paymentInfo.setOrderSn(outTradeNo);
        paymentInfo.setPaymentStatus("未付款");
        paymentInfo.setSubject(omsOrder.getReceiverName()+"选购的商品");
        paymentInfo.setTotalAmount(totalAmount);
        paymentService.savePaymentInfo(paymentInfo);

        // 向消息中间件发送一个检查支付状态(支付服务消费)的延迟消息队列
        // paymentService.sendDelayPaymentResultCheckQueue(outTradeNo, 5);

        // 提交请求到支付宝
        return form;
    }*/
}
