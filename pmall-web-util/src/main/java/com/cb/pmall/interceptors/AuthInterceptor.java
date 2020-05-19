package com.cb.pmall.interceptors;

import com.alibaba.fastjson.JSON;
import com.cb.pmall.annotations.LoginRequired;
import com.cb.pmall.utils.CookieUtil;
import com.cb.pmall.utils.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否有注解@LoginRequired
        // 有则拦截
        HandlerMethod hm = (HandlerMethod) handler;
        LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);
        if (methodAnnotation == null) {
            // 无注解,直接放行
            return true;
        }
        // 是否必须登录
        boolean necessary = methodAnnotation.loginSuccess();
        if (necessary) {
            // 需要进行登录验证
            // token获取
            String token = "";
            // 旧token获取
            String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
            if (StringUtils.isNotBlank(oldToken)) {
                token = oldToken;
            }
            // 新token获取
            String newToken = request.getParameter("newToken");
            if (StringUtils.isNotBlank(newToken)) {
                token = newToken;
            }

            // 获取URL,验证失败打回时作为ReturnURL
            StringBuffer requestURL = request.getRequestURL();

            // 获取IP
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if (StringUtils.isBlank(ip)) {
                ip = request.getRemoteAddr();// 从request中获取ip
                if (StringUtils.isBlank(ip)) {
                    ip = "127.0.0.1";
                }
            }
            // token不为空,进行验证
            if (StringUtils.isNotBlank(token)) {
                String successJson = HttpclientUtil.doGet("http://localhost:8085/verify?token=" + token + "&currentIp=" + ip);
                Map<String, Object> map = JSON.parseObject(successJson, Map.class);
                String status = (String) map.get("status");
                if (status.equals("success")) {
                    // token验证成功
                    // 将token存入cookie
                    CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
                    // 将属性存入request
                    request.setAttribute("memberId", map.get("memberId"));
                    request.setAttribute("nickname", map.get("nickname"));
                    return true;
                }
            }
            // token验证失败,打回
            response.sendRedirect("http://localhost:8085/index?ReturnUrl=" + requestURL);
            return false;

    }

    {
        // 无需验证,直接放行
        return true;
    }


}
}
