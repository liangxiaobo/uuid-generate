package com.lb.uuid.server.uuidserver;

import com.lb.uuid.common.uuidcommon.model.UuidDataItemModel;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.sun.javafx.tk.Toolkit;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UuidServerApplicationTests {

    @Autowired
    EurekaClient eurekaClient;



    @Test
    public void contextLoads() {
        System.out.println("UuidServerApplicationTests.contextLoads ======== ");
//        eurekaClient.getApplications().getRegisteredApplications().forEach(p -> {
//            System.out.println("===== " + p.getName());
//        });

        System.out.println(eurekaClient);
        System.out.println(eurekaClient.getApplications().getRegisteredApplications().size());

        Application application = eurekaClient.getApplication("test-demo");

        System.out.println("eurekaClient.getEurekaClientConfig().getRegistryFetchIntervalSeconds() ============== " + eurekaClient.getEurekaClientConfig().getRegistryFetchIntervalSeconds());

        if (application != null) {
            for (InstanceInfo instanceInfo : application.getInstances()) {
                System.out.println("----- : "+ instanceInfo.getInstanceId());
            }
        }



//        for (Application application : eurekaClient.getApplications().getRegisteredApplications()) {
//            System.out.println("===== " + application.getName() + ", " );
//
//            for (InstanceInfo instanceInfo : application.getInstances()) {
//                System.out.println("----- : "+ instanceInfo.getInstanceId());
//            }
//        }


    }

    @Test
    public  void testNum() {

        int count = 10;

        HashMap<Integer, UuidDataItemModel> map = new HashMap<>();

        int indexCount = 1;

        for (int i = 1; i <= 31; i++) {
            for (int j = 1; j <= 31; j++) {
                if (count < indexCount) {
                    break;
                }else {
                   UuidDataItemModel _dataItemModel = new UuidDataItemModel(i, j);
                   map.put(indexCount, _dataItemModel);
                    indexCount++;
                }


            }
        }

        System.out.println(map);

    }

    @Test
    public void Task01() {

        try {
            scheduleWithFixedDelay();
//            ScheduledFuture scheduledExecutorService = scheduleAtFixedRate();
//            Thread.sleep(10000);
//            scheduleCaller();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void scheduleWithFixedDelay() throws ExecutionException, InterruptedException {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(10);

       Runnable aa =  new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ScheduledFuture<?> result = executorService.scheduleWithFixedDelay(aa, 1000, 2000, TimeUnit.MILLISECONDS);

//        result.get();

        System.out.println("over");

        ((ScheduledThreadPoolExecutor) executorService).remove(aa);
    }

    static ScheduledFuture scheduleAtFixedRate() throws InterruptedException, ExecutionException {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(10);

        ScheduledFuture<?> result = executorService.scheduleAtFixedRate(new Runnable() {
            public void run() {

                System.out.println(System.currentTimeMillis());
                try {
                    Thread.sleep(3000);

//                    executorService.shutdown();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, 1000, 2000, TimeUnit.MILLISECONDS);

        // 由于是定时任务，一直不会返回
        System.out.println(" ========== " + result.get());
//        result.cancel(true);
        System.out.println("aaa");
        return result;
    }

    // 延迟1s后开始执行，只执行一次，有返回值
    static void scheduleCaller() throws InterruptedException, ExecutionException {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(10);

        ScheduledFuture<String> result = executorService.schedule(new Callable<String>() {

            @Override
            public String call() throws Exception {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return "gh";
            }

        }, 1000, TimeUnit.MILLISECONDS);

        // 阻塞，直到任务执行完成
        System.out.print(result.get());

    }

}
