package com.cb.pmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cb.pmall.beans.UmsMember;
import com.cb.pmall.service.UmsMemberService;
import com.cb.pmall.utils.CookieUtil;
import com.cb.pmall.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈彬
 * date 2020/4/28 9:08 PM
 */
@Controller
public class PassportController {

    @Reference
    UmsMemberService umsMemberService;

    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap map) {
        map.put("ReturnUrl", ReturnUrl);
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UmsMember umsMember, HttpServletRequest request, HttpServletResponse response) {

        // 对用户进行认证
        UmsMember user = umsMemberService.login(umsMember);
        Map<String, Object> map = new HashMap<>();
        String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();// 从request中获取ip
            if (StringUtils.isBlank(ip)) {
                ip = "127.0.0.1";
            }
        }
        if (user != null) {
            // 查到用户,登录成功
            // 制作JWT
            map.put("memberId", user.getId());
            map.put("nickname", user.getNickname());
            // 创建新token并返回
            String token = JwtUtil.encode("pmall", map, ip);
            // 将token传入cookie
            CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);

            return token;
        }
        return "fail";
    }

    @ResponseBody
    @RequestMapping("verify")
    public String verify(String token, String currentIp) {
        // 解析JWT
        Map<String,String> map = new HashMap<>();
        Map<String, Object> decode = JwtUtil.decode(token, "pmall", currentIp);
        if (decode != null) {
            // 解析成功,说明用户成功登录
            map.put("status", "success");
            map.put("memberId", (String) decode.get("memberId"));
            map.put("nickname", (String) decode.get("nickname"));
        } else {
            map.put("status", "fail");
        }

        return JSON.toJSONString(map);
    }
}
