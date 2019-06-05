package com.lb.uuid.common.uuidcommon.config;

/**
 * @program: lb-uuid-parent
 * @description:
 * @author: liangbo
 * @create: 2019-06-04 14:52
 **/
public class ConfigUtils {
    /**
     * redis中存储 生成的UuidDataItem集合的Map key
     */
    public final static String UUID_MAP_KEY = "uuidMapKey";

    /**
     * workId最大值 31
     */
    public final static int WORK_ID_MAX = 31;

    /**
     * dataCenterId最大值 31
     */
    public final static int DATA_CENTER_ID_MAX = 31;

    /**
     * 使用过的
     */
    public final static String UUID_USED_DATA_POOL = "uuidUsedDataPoolKey";

    /**
     * 将应用和抽取的数据种子关联
     */
    public final static String APP_INSTANCE_ID_USED_DATA = "appInstanceIdUsedDataKey";
}
