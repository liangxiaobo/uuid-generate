package com.lb.uuid.server.uuidserver;

import com.lb.uuid.common.uuidcommon.config.ConfigUtils;
import com.lb.uuid.common.uuidcommon.model.UuidDataItemModel;
import com.lb.uuid.server.uuidserver.config.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: lb-uuid-parent
 * @description:
 * @author: liangbo
 * @create: 2019-06-04 12:32
 **/
@Component
public class UuidRunner implements ApplicationRunner {

    private final static Logger logger = LoggerFactory.getLogger(UuidRunner.class);

    @Autowired
    ConfigProperties configProperties;

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("UuidRunner.run ============= ");
        logger.info("configproperties ==== {}", configProperties.toString());

        // 先从redis中读取
        Map<Integer, UuidDataItemModel> map = redisTemplate.opsForHash().entries(ConfigUtils.UUID_MAP_KEY);

        if (map.size() == 0) {
            // 初始化
            map = initDeployData();

            redisTemplate.opsForHash().putAll(ConfigUtils.UUID_MAP_KEY, map);

            Set<Integer> keys = redisTemplate.opsForHash().keys(ConfigUtils.UUID_MAP_KEY);

            logger.info("keys ===== {}", keys);

        } else {
            logger.info("map 上一次存的 === {} ", redisTemplate.opsForHash().entries(ConfigUtils.UUID_MAP_KEY));
        }
    }

    /**
     * 项目初始化时根据配置文件中的
     * ${uuid.machine-count} 机器数量来生成对应数据的 {workId: 1,dataCenterId: 1}
     * @return
     */
    private Map<Integer, UuidDataItemModel> initDeployData() {
        HashMap<Integer, UuidDataItemModel> map = new HashMap<>();

        int index = 1;

        for (int i = 1; i <= ConfigUtils.WORK_ID_MAX; i++) {
            for (int j = 1; j <= ConfigUtils.DATA_CENTER_ID_MAX; j++) {
                if (configProperties.getMachineCount() < index) {
                    break;
                }else {
                    UuidDataItemModel _dataItemModel = new UuidDataItemModel(i, j);
                    map.put(index, _dataItemModel);
                    index++;
                }
            }
        }

        logger.info("生成在的UuidDataItem集合 {}", map);
        return map;
    }

}
