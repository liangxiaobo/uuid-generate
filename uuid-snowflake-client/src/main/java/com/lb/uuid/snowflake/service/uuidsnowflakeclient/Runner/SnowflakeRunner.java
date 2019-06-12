package com.lb.uuid.snowflake.service.uuidsnowflakeclient.Runner;

import com.lb.uuid.common.uuidcommon.config.ConfigUtils;
import com.lb.uuid.common.uuidcommon.model.UuidDataItemModel;
import com.lb.uuid.snowflake.service.uuidsnowflakeclient.service.SnowflakeDataService;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @program: lb-uuid-parent
 * @description: SnowflakeRunner要做的事
 *                  1. 启动（或重启）时先尝试取出上一次关联的数据，如果有就拿出来使用
 *                  2. 启动（第一次）时从未使用池子里取出集合中的第一个，从未使用池子中删除，放到已使用池子中
 *                  3. 获取实例ID并关联到已使用池数据的key
 * @author: liangbo
 * @create: 2019-06-05 10:02
 **/
@Component
public class SnowflakeRunner implements ApplicationRunner {
    private final static Logger logger = LoggerFactory.getLogger(SnowflakeRunner.class);

    @Autowired
    EurekaClient eurekaClient;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SnowflakeDataService snowflakeDataService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("TestRunner.run =================== ");
        System.out.println(eurekaClient.getApplicationInfoManager().getEurekaInstanceConfig().getInstanceId());
        System.out.println("TestRunner.run =================== ");

        while (true) {
            if (redisTemplate.opsForHash().size(ConfigUtils.UUID_MAP_KEY) > 0) {
                break;
            }

            logger.info("未使用池为空，可能ID-Server服务未启动，等待2秒继续");

            Thread.sleep(2000);
        }

        logger.info("未使用DataPoolMap {} ", redisTemplate.opsForHash().size(ConfigUtils.UUID_MAP_KEY));


        String instanceId = eurekaClient.getApplicationInfoManager().getEurekaInstanceConfig().getInstanceId();

        if (redisTemplate.opsForHash().hasKey(ConfigUtils.APP_INSTANCE_ID_USED_DATA, instanceId)) {
            Integer exist_obj_value_key = (Integer) redisTemplate.opsForHash().get(ConfigUtils.APP_INSTANCE_ID_USED_DATA, instanceId);

            // 从已使用池子里取出来使用
            UuidDataItemModel exist_obj = (UuidDataItemModel)redisTemplate.opsForHash().get(ConfigUtils.UUID_USED_DATA_POOL, exist_obj_value_key);

            // 将从缓存中取到的workId和dataCenterId设置给snowflake对象
            snowflakeDataService.setWorkerId(Long.valueOf(exist_obj.getWorkId()));
            snowflakeDataService.setDataCenterId(Long.valueOf(exist_obj.getDataCenterId()));

            logger.info("重启后，如果关联池子里有就直接用: {}", exist_obj);

        }else {
            Set<Integer> keys = redisTemplate.opsForHash().keys(ConfigUtils.UUID_MAP_KEY);

            if (keys.iterator().hasNext()) {
                Integer key = keys.iterator().next();
                UuidDataItemModel uuidDataItemModel = (UuidDataItemModel)redisTemplate.opsForHash().get(ConfigUtils.UUID_MAP_KEY, key);

                logger.info("1. 应用 {} 抽到的 key {} value 为 {}", instanceId, key, uuidDataItemModel);

                logger.info("2. 将抽到的数据到放已经使用的池子里");

                redisTemplate.opsForHash().put(ConfigUtils.UUID_USED_DATA_POOL, key, uuidDataItemModel);
                redisTemplate.opsForHash().delete(ConfigUtils.UUID_MAP_KEY, key);

                logger.info("3. 查询已使用池子数据 {}", redisTemplate.opsForHash().entries(ConfigUtils.UUID_USED_DATA_POOL));

                redisTemplate.opsForHash().put(ConfigUtils.APP_INSTANCE_ID_USED_DATA, instanceId, key);

                logger.info("4. 查询应用和数据种子关联的池子 {}", redisTemplate.opsForHash().entries(ConfigUtils.APP_INSTANCE_ID_USED_DATA));

                // 将抽到的workId和dataCenterId设置给snowflake对象
                snowflakeDataService.setWorkerId(Long.valueOf(uuidDataItemModel.getWorkId()));
                snowflakeDataService.setDataCenterId(Long.valueOf(uuidDataItemModel.getDataCenterId()));
            }
        }

        logger.info("============== 设置完成 workId={}, dataCenterId={}", snowflakeDataService.getWorkerId(), snowflakeDataService.getDataCenterId());
    }

}
