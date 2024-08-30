package com.yolo.chef.orderStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusService {
    @Autowired
    OrderStatusRepository orderStatusRepository;
    public String getStatusByOrderId(Integer orderStatusId)
    {
       OrderStatus status= orderStatusRepository.findById(orderStatusId).orElseGet(null);
       if(status!=null)
       {
           return status.getValue();
       }
       return  "";
    }
}
