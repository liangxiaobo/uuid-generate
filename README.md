# 集群模式
 以容器方式部署在docker swarm 或 k8s环境中
 
 <strong>当前分支内容是k8s部署</strong>

## 项目打包成docker镜像

  **先修改根目录的pom.xml中的 <docker.registry.url></docker.registry.url> 改为自己的私库地址**

 * 首先在项目根目录
 
 ```bash
    mvn clean install -Dmaven.test.skip=true
 ```
 
 * 分别进入三个子项目中执行打包命令如果uuid-eureka-server
 
 ```bash
 cd uuid-eureka-server/
 mvn package docker:build -Dmaven.test.skip=true
 ```
 
 打包成功后将镜像上传到私有库或公共库中，我这里是个人的私有库
 
 ```bash
    docker push 172.16.10.192:5000/bobo/uuid-eureka-server
    docker push 172.16.10.192:5000/bobo/uuid-server
    docker push 172.16.10.192:5000/bobo/uuid-snowflake-client
 ```
 
 ## 在docker swarm 中部署
 
 * 执行命令
 ```bash
 docker stack deploy -c docker-swarm-compose.yml --with-registry-auth id-app
 ```
 
 * service
 
 ```bash
 [root@manager uuid-generate]# docker service ls
 ID                  NAME                           MODE                REPLICAS            IMAGE                                                  PORTS
 gjjf0oaou5zg        id-app_uuid-eureka-server      replicated          1/1                 172.16.10.192:5000/bobo/uuid-eureka-server:latest      *:8761->8761/tcp
 wmwusdo737pm        id-app_uuid-server             replicated          1/1                 172.16.10.192:5000/bobo/uuid-server:latest             *:8082->8082/tcp
 y97ceng663iz        id-app_uuid-snowflake-client   replicated          2/2                 172.16.10.192:5000/bobo/uuid-snowflake-client:latest   *:8123->8123/tcp
 ```
 
 * 访问
 
 http://ip:8123/generator/id
 
 * 查看两个实例的日志
 
 ```bash
 id-app_uuid-snowflake-client.2.0tkgecz302jw@work2    | 2019-06-12 17:16:50.522  INFO 1 --- [nio-8123-exec-2] o.s.web.servlet.DispatcherServlet        : Completed initialization in 12 ms
 id-app_uuid-snowflake-client.2.0tkgecz302jw@work2    | 2019-06-12 17:16:50.559  INFO 1 --- [nio-8123-exec-2] c.l.u.s.s.u.c.SnowflakeController        : 生成ID服务的端口：8123, workId: 1, dateCenterId: 3
 id-app_uuid-snowflake-client.2.0tkgecz302jw@work2    | 单例模式 ===== 进入
 id-app_uuid-snowflake-client.2.0tkgecz302jw@work2    | 2019-06-12 17:19:16.838  INFO 1 --- [nio-8123-exec-4] c.l.u.s.s.u.c.SnowflakeController        : 生成ID服务的端口：8123, workId: 1, dateCenterId: 3
 id-app_uuid-snowflake-client.2.0tkgecz302jw@work2    | 2019-06-12 17:19:17.056  INFO 1 --- [nio-8123-exec-6] c.l.u.s.s.u.c.SnowflakeController        : 生成ID服务的端口：8123, workId: 1, dateCenterId: 3
 id-app_uuid-snowflake-client.2.0tkgecz302jw@work2    | 2019-06-12 17:19:17.238  INFO 1 --- [nio-8123-exec-8] c.l.u.s.s.u.c.SnowflakeController        : 生成ID服务的端口：8123, workId: 1, dateCenterId: 3
 id-app_uuid-snowflake-client.1.gxkc7k0xuyph@manager    | 2019-06-12 17:21:23.632  INFO 1 --- [nio-8123-exec-7] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
 id-app_uuid-snowflake-client.1.gxkc7k0xuyph@manager    | 2019-06-12 17:21:23.632  INFO 1 --- [nio-8123-exec-7] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
 id-app_uuid-snowflake-client.1.gxkc7k0xuyph@manager    | 2019-06-12 17:21:23.640  INFO 1 --- [nio-8123-exec-7] o.s.web.servlet.DispatcherServlet        : Completed initialization in 8 ms
 id-app_uuid-snowflake-client.1.gxkc7k0xuyph@manager    | 2019-06-12 17:21:23.673  INFO 1 --- [nio-8123-exec-7] c.l.u.s.s.u.c.SnowflakeController        : 生成ID服务的端口：8123, workId: 1, dateCenterId: 9
 id-app_uuid-snowflake-client.1.gxkc7k0xuyph@manager    | 单例模式 ===== 进入
 id-app_uuid-snowflake-client.1.gxkc7k0xuyph@manager    | 2019-06-12 17:22:35.978  INFO 1 --- [nio-8123-exec-6] c.l.u.s.s.u.c.SnowflakeController        : 生成ID服务的端口：8123, workId: 1, dateCenterId: 9
 id-app_uuid-snowflake-client.1.gxkc7k0xuyph@manager    | 2019-06-12 17:22:36.057  INFO 1 --- [nio-8123-exec-8] c.l.u.s.s.u.c.SnowflakeController        : 生成ID服务的端口：8123, workId: 1, dateCenterId: 9
 id-app_uuid-snowflake-client.1.gxkc7k0xuyph@manager    | 2019-06-12 17:22:36.143  INFO 1 --- [io-8123-exec-10] c.l.u.s.s.u.c.SnowflakeController        : 生成ID服务的端口：8123, workId: 1, dateCenterId: 9
 id-app_uuid-snowflake-client.1.gxkc7k0xuyph@manager    | 2019-06-12 17:22:36.225  INFO 1 --- [nio-8123-exec-2] c.l.u.s.s.u.c.SnowflakeController        : 生成ID服务的端口：8123, workId: 1, dateCenterId: 9
 
 ```
 
 ## k8s部署
 
 ### 需要注意
 
    1. 本实例在k8s中 uuid-eureka-server和uuid-server 只部署一个实例
    2. 使用的docker私有库，请参考[k8s 从私有仓库拉取镜像](https://www.jianshu.com/p/3f24bbee72ad)
    
 ### 重新打包上传docker image
 
 ```bash
    cd uuid-eureka-server/
    mvn package docker:build -Dmaven.test.skip=true
    
    cd uuid-server/
    mvn package docker:build -Dmaven.test.skip=true
    
    cd uuid-snowflake-client/
    mvn package docker:build -Dmaven.test.skip=true
  ```
  
 ### 重新 docker push image 
    
 ### 执行
 
 1. 先发布Eureka-Server, 执行根目录的 eureka-k8s.yaml
 
 ```bash
 kubectl create -f eureka-k8s.yaml
 ```
 
 2. 执行发布 uuid-server和uuid-snowflake-client 执行根目录的 uuid-k8s.yaml
 ```bash
 kubectl create -f uuid-k8s.yaml 
 ```
 
 ### 结果
 
 查看pod
 ```bash
[root@master work]# kubectl get pod -o wide
NAME                                    READY   STATUS    RESTARTS   AGE   IP            NODE    NOMINATED NODE   READINESS GATES
nginx-65f88748fd-s9727                  1/1     Running   1          27h   10.244.1.4    node1   <none>           <none>
uuid-eureka-server-0                    1/1     Running   0          52m   10.244.1.37   node1   <none>           <none>
uuid-server-68c4b867c-7qt8p             1/1     Running   0          50m   10.244.1.38   node1   <none>           <none>
uuid-snowflake-client-866764b58-ltdc6   1/1     Running   0          50m   10.244.1.39   node1   <none>           <none>
uuid-snowflake-client-866764b58-nh6gl   1/1     Running   0          50m   10.244.2.25   node2   <none>           <none>

 ```
 
 查看service
 
 ```bash
 [root@master work]# kubectl get svc
 NAME                    TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE
 kubernetes              ClusterIP   10.1.0.1       <none>        443/TCP          29h
 nginx                   NodePort    10.1.193.136   <none>        80:30057/TCP     27h
 uuid-eureka-server      NodePort    10.1.8.102     <none>        8761:32297/TCP   56m
 uuid-snowflake-client   NodePort    10.1.103.35    <none>        8123:31968/TCP   54m
 ```
 其中访问 eureka-server地址 http://IP:32297 访问id生成服务 http://IP:31968/generator/id
 
 * uuid-server的日志
 ```bash
 INFO 1 --- [pool-7-thread-1] c.l.u.s.u.UuidServerApplication          : 应用关联的种子 2
 INFO 1 --- [pool-7-thread-1] c.l.u.s.u.UuidServerApplication          : 服务应用注册数量： 2
 ```
 
> 应用关联的种子 和 服务应用注册数量 相等时，表示正常，否则表示有异常
    
 
 # 注意
 
 > uuid-server项目主要工作是初始化 workid和dataCenterId到redis中，并监控redis中的关联池与实际Eureka-server中注册服务的变化，
 监控的任务 ```task-execution-interval: 30```默认30秒执行一次，由于在生产环境中，uuid-snowflake-client注册的数量不确定，所以需要设置足够大的时长，10分钟以上，
 30秒为测试环境。
 
 
 