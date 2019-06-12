# 单机模式
是指不使用容器部署，使用java -jar 的方式直接在机器上运行，这种方式跑多实例端口不能相同
本项目需要redis支持，主要做存储

# 运行

 1. 先运行 uuid-eureka-server 项目端口 8761
 2. 运行 uuid-server 
 3. 运行 uuid-snowflake-client 这个需要跑多个
 
 # 测试结果
 
 访问 uuid-snowflake-client项目的路径 http://localhost:8123/generator/id
 
 可查看实例日志如：
 
 ```
    生成ID服务的端口：8121, workId: 1, dateCenterId: 8
    生成ID服务的端口：8123, workId: 1, dateCenterId: 1
```
 