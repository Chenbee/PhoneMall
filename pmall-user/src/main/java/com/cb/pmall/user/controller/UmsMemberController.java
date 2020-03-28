package com.cb.pmall.user.controller;

import com.cb.pmall.user.Service.UmsMemberReceiveAddressService;
import com.cb.pmall.user.Service.UmsMemberService;
import com.cb.pmall.user.beans.UmsMember;
import com.cb.pmall.user.beans.UmsMemberReceiveAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UmsMemberController {
    @Autowired
    UmsMemberService umsMemberService;

    @Autowired
    UmsMemberReceiveAddressService umsMemberReceiveAddressService;

    /**    对用户操作    **/

    /**
     * 查找所有用户
     * @return
     */
    @RequestMapping("/getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){
        List<UmsMember> userMembers =  umsMemberService.getAllUser();
        return userMembers;
    }

    /**
     * 根据id查找用户
     * @return
     */
    @RequestMapping("/getUser")
    @ResponseBody
    public UmsMember getUser(Integer id){
        UmsMember userMember =  umsMemberService.getUser(id);
        return userMember;
    }

    /**
     * 根据id删除用户
     * @return
     */
    @RequestMapping("/deleteUser")
    public void deleteUser(Integer id){
        umsMemberService.deleteUser(id);
    }

    /**
     * 修改用户
     */
    @RequestMapping("/modifyUser")
    public void modifyUser(UmsMember umsMember){
        umsMemberService.modifyUser(umsMember);
    }

    /**
     * 添加用户
     */
    @RequestMapping("/addUser")
    public void addUser(UmsMember umsMember){
        umsMemberService.addUser(umsMember);
    }



    /**    对用户地址操作    **/

    /**
     * 查找单用户所有地址
     * @return
     */
    @RequestMapping("/getAllAddress")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getAllAddress(Integer memberId){
        List<UmsMemberReceiveAddress> msMemberReceiveAddresss =  umsMemberReceiveAddressService.getAllAddress(memberId);
        return msMemberReceiveAddresss;
    }

    /**
     * 根据id查找地址
     * @return
     */
    @RequestMapping("/getAddress")
    @ResponseBody
    public UmsMemberReceiveAddress getAddress(Integer id){
        UmsMemberReceiveAddress umsMemberReceiveAddress =  umsMemberReceiveAddressService.getAddress(id);
        return umsMemberReceiveAddress;
    }

    /**
     * 根据id删除地址
     * @return
     */
    @RequestMapping("/deleteAddress")
    public void deleteAddress(Integer id){
        umsMemberReceiveAddressService.deleteAddress(id);
    }

    /**
     * 修改地址
     */
    @RequestMapping("/modifyAddress")
    public void modifyAddress(UmsMemberReceiveAddress umsMemberReceiveAddress){
        umsMemberReceiveAddressService.modifyAddress(umsMemberReceiveAddress);
    }

    /**
     * 添加地址
     */
    @RequestMapping("/addAddress")
    public void addUser(UmsMemberReceiveAddress umsMemberReceiveAddress){
        umsMemberReceiveAddressService.addAddress(umsMemberReceiveAddress);
    }
}
