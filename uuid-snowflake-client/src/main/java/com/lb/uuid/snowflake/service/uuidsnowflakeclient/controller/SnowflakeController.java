package com.lb.uuid.snowflake.service.uuidsnowflakeclient.controller;

import com.lb.uuid.snowflake.service.uuidsnowflakeclient.service.SnowflakeDataService;
import com.lb.uuid.snowflake.service.uuidsnowflakeclient.service.SnowflakeIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SnowflakeController {
    private static final Logger logger = LoggerFactory.getLogger(SnowflakeController.class);

    @Autowired
    SnowflakeDataService snowflakeDataService;


    @Value("${server.port}")
    private String port;

    /**
     * SnowflakeIdGenerator 是个单例模式
     * @return
     */
    @RequestMapping("/generator/id/")
    public Long generatorIdOfLongType(){
        logger.info("生成ID服务的端口：{}, workId: {}, dateCenterId: {}", port, snowflakeDataService.getWorkerId(), snowflakeDataService.getDataCenterId());
        return SnowflakeIdGenerator.getInstance(snowflakeDataService.getWorkerId(), snowflakeDataService.getDataCenterId()).nextId();
    }


}
