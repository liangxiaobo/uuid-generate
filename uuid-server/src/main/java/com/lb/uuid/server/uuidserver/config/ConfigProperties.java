package com.lb.uuid.server.uuidserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * @program: lb-uuid-parent
 * @description:
 * @author: liangbo
 * @create: 2019-06-04 11:28
 **/
@Configuration
public class ConfigProperties {

    @Value("${uuid.client-application-name}")
    private String clientApplicationName;

    /**
     * machineCount 默认值2
     */
    @Value("${uuid.machine-count:2}")
    private Integer machineCount;

    /**
     * 同步检测已使用池中的数据和eureka注册表中的实例数默认30秒
     */
    @Value("${uuid.task-execution-interval:30}")
    private Long taskExecutionInterval;

    public String getClientApplicationName() {
        return clientApplicationName;
    }

    public Integer getMachineCount() {
        return machineCount;
    }

    public Long getTaskExecutionInterval() {
        return taskExecutionInterval;
    }

    @Override
    public String toString() {
        return "ConfigProperties{" +
                "clientApplicationName='" + clientApplicationName + '\'' +
                ", machineCount=" + machineCount +
                ", taskExecutionInterval=" + taskExecutionInterval +
                '}';
    }
}
