package com.lb.uuid.server.uuidserver;

import com.lb.uuid.common.uuidcommon.config.ConfigUtils;
import com.lb.uuid.common.uuidcommon.model.UuidDataItemModel;
import com.lb.uuid.server.uuidserver.config.ConfigProperties;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.*;

@SpringBootApplication
@EnableEurekaClient
@EnableAutoConfiguration
public class UuidServerApplication {

    private final static Logger logger = LoggerFactory.getLogger(UuidServerApplication.class);


    public static void main(String[] args) {
        System.out.println("app start ================= ");

        ConfigurableApplicationContext context = SpringApplication.run(UuidServerApplication.class, args);
        ConfigProperties configProperties =  context.getBean(ConfigProperties.class);
        EurekaClient eurekaClient = context.getBean(EurekaClient.class);
        RedisTemplate redisTemplate = (RedisTemplate) context.getBean("redisTemplate");

        System.out.println("app end ================= ");

        try {
            scheduleAtFixedRate(eurekaClient, configProperties, redisTemplate);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * 延时5秒执行任务，任务执行间隔 {configProperties.getTaskExecutionInterval()}
     * 此任务，主要工作：
     *              1. 取出已使用池和应用实例ID关联的池子数据
     *              2. 通过eurekaClient获取应用实例数
     *              3. 对比两个数值是否相等，正常情况下，应该相等，否则的话表示有实例down了
     *              4. 将down了的实例关联的，数据种子，放回未使用池子里
     * @param eurekaClient
     * @param configProperties
     * @param redisTemplate
     * @throws InterruptedException
     * @throws ExecutionException
     */
    static void scheduleAtFixedRate(EurekaClient eurekaClient, ConfigProperties configProperties, RedisTemplate redisTemplate) throws InterruptedException, ExecutionException {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        ScheduledFuture<?> result = executorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                logger.info("ScheduledExecutorService 执行一次 {}", System.currentTimeMillis());

                Long app_instance_used_count = redisTemplate.opsForHash().size(ConfigUtils.APP_INSTANCE_ID_USED_DATA);

                logger.info("应用关联的种子 {}", app_instance_used_count);


                if (null != eurekaClient.getApplication(configProperties.getClientApplicationName())) {
                    logger.info("服务应用注册数量： {}", eurekaClient.getApplication(configProperties.getClientApplicationName()).getInstances().size());
                    int appCount = eurekaClient.getApplication(configProperties.getClientApplicationName()).getInstances().size();

                    logger.info("已注册的应用实例列表：{}", eurekaClient.getApplication(configProperties.getClientApplicationName()).getInstances());

                    if (app_instance_used_count != appCount) {
                        Set<String> oldMap = filterUsedApp(eurekaClient.getApplication(configProperties.getClientApplicationName()).getInstances(),
                                redisTemplate.opsForHash().entries(ConfigUtils.APP_INSTANCE_ID_USED_DATA));

                        logger.info("oldMap === {}", oldMap);

                        Iterator<String> iterator = oldMap.iterator();

                        while (iterator.hasNext()) {
                            String nextItem = iterator.next();
                            Integer value_key = (Integer) redisTemplate.opsForHash().entries(ConfigUtils.APP_INSTANCE_ID_USED_DATA).get(nextItem);
                            redisTemplate.opsForHash().delete(ConfigUtils.APP_INSTANCE_ID_USED_DATA, nextItem);

                            UuidDataItemModel uuidDataItemModel = (UuidDataItemModel) redisTemplate.opsForHash().get(ConfigUtils.UUID_USED_DATA_POOL, value_key);
                            redisTemplate.opsForHash().delete(ConfigUtils.UUID_USED_DATA_POOL, value_key);

                            redisTemplate.opsForHash().put(ConfigUtils.UUID_MAP_KEY, value_key, uuidDataItemModel);
                        }

                        logger.info("最终 未使用池 == {}", redisTemplate.opsForHash().entries(ConfigUtils.UUID_MAP_KEY));
                    }

                }else {
                    logger.info("{} 暂无注册实例", configProperties.getClientApplicationName());
                }

            }
        }, 5, configProperties.getTaskExecutionInterval(), TimeUnit.SECONDS);

        result.get();
    }

    // 过滤不在注册列表中的实例
    static Set<String> filterUsedApp(List<InstanceInfo> currentInstance, Map<String, Integer> usedAppMap) {
        Set<String> oldMap =new HashSet<>();

        logger.info("filer.usedAppMap == {}", usedAppMap);

        Iterator<String> iterator = usedAppMap.keySet().iterator();

        while (iterator.hasNext()) {
            String item = iterator.next();
            boolean resultNext = false;

            for (InstanceInfo instanceInfo : currentInstance) {
                logger.info("比较 === {} == {} = {}", instanceInfo.getInstanceId(), item ,(instanceInfo.getInstanceId().equals(item)));

                if (instanceInfo.getInstanceId().equals(item)) {
                    resultNext = true;
                    break;
                }
            }

            if (!resultNext) {
                oldMap.add(item);
            }
        }

        return oldMap;
    }

}
