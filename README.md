# PhoneMall
    2020.3.27
        手机商城毕业设计预计2020.04完工
        sql文件在pmall-api下


    2020.3.30
        完成service与web的分布式框架的搭建      
        pmall-user-web: 8080
        pmall-user-service: 8070
        pmall-manage-web: 8081
        pmall-manage-service: 8071
        pmall-item-web: 8082
        pmall-search-web: 8083
        pmall-search-service: 8073
        pmall-cart-web: 8084
        pmall-cart-service: 8074
        pmall-passport-web: 8085
        pmall-order-web: 8086
        pmall-order-service: 8076
        pmall-payment: 8087
    2020.3.31
        完成后端管理
       
        
    2020.4.2
      spu保存时绑定销售属性;
      select * from `pms_product_sale_attr`;
      
      sku保存平台属性;
      select * from `pms_sku_attr_value`;
      select * from `pms_base_attr_info`;
      select * from `pms_base_attr_value`;
      
      sku保存销售属性;
      select * from `pms_sku_sale_attr_value`;
      select * from `pms_base_sale_attr`;
      select * from `pms_product_sale_attr_value`;
    
    2020.5.14
    订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单

        
        
        