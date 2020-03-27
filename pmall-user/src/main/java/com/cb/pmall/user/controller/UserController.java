package com.cb.pmall.user.controller;

import com.cb.pmall.user.Service.UserService;
import com.cb.pmall.user.beans.UmsMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){
        List<UmsMember> userMembers =  userService.getAllUser();
        return userMembers;
    }
}
