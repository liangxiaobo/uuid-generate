package com.lb.uuid.server.uuidserver;

import com.lb.uuid.server.uuidserver.config.ConfigProperties;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: lb-uuid-parent
 * @description:
 * @author: liangbo
 * @create: 2019-06-04 15:57
 **/
@RestController
public class UuidController {
    @Autowired
    EurekaClient eurekaClient;
    @Autowired
    ConfigProperties configProperties;

    @RequestMapping("/info")
    public void info() {
//        for (InstanceInfo instanceInfo : eurekaClient.getApplication(configProperties.getClientApplicationName()).getInstances()) {
//
//        }

        System.out.println("=============================");
//        System.out.println(eurekaClient.getApplication(configProperties.getClientApplicationName()).getInstances());
        for (InstanceInfo instanceInfo : eurekaClient.getApplication(configProperties.getClientApplicationName()).getInstances()) {
            System.out.println(instanceInfo.getInstanceId() + ": " + instanceInfo.getStatus());
        }
        System.out.println("=============================");

    }
}
