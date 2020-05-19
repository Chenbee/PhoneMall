package com.cb.pmall.user;

import com.cb.pmall.beans.UmsMemberReceiveAddress;
import com.cb.pmall.service.UmsMemberReceiveAddressService;
import com.cb.pmall.user.service.impl.UmsMemberReceiveAddressServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PmallUserServiceApplicationTests {

    @Test
    public void contextLoads() {


        UmsMemberReceiveAddressService addressService = new UmsMemberReceiveAddressServiceImpl();

        //        System.out.println(addressService.getAllAddress("1"));
                System.out.println(addressService.getAddress("1"));

    }

}
