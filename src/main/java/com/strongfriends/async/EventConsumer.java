package com.strongfriends.async;

import com.alibaba.fastjson.JSON;
import com.strongfriends.util.JedisAdapter;
import com.strongfriends.util.RedisKeyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Spirng的InitializingBean为bean提供了定义初始化方法的方式。InitializingBean是一个接口，仅包含一个方法：afterPropertiesSet()
 * Spring在设置完一个bean所有的信息后，会检查bean是否实现了InitializingBean接口，如果实现就调用bean的afterPropertiesSet方法。
 * 装配bean的信息查看bean是否实现InitializingBean接口调用afterPropertiesSet方法
 * afterPropertiesSet会在init-method前调用
 *
 * 实现ApplicationContextAware接口后，可以获得ApplicationContext中的所有bean。
 */

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {


    // 在config Map中关联EventType与对应处理的EventHandler
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 找出所有实现了EventHandler接口的类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            // 遍历所有实现了EventHandler接口的类
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                // 找到每个Handler类支持的EventType
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                // 找到EventType后要与EventHandler关联
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        // 放入config中,完成关联
                        config.put(type, new ArrayList<EventHandler>());
                    }

                    // 注册每个时间的Handler
                    config.get(type).add(entry.getValue());
                }
            }
        }

        // 初始化线程池
        ThreadPool threadPool = new ThreadPool();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    // 弹出最右的事件，如果list没有事件会阻塞list直到等待超时返回nil
                    List<String> messages = jedisAdapter.brpop(10, key);
                    // [0]:EVENT(即list key) [1]：事件的具体信息
                    for (String message : messages) {
                        // 跳过第一个元素，即redis的list key
                        if (message.equals(key)) {
                            continue;
                        }
                        // JSON反序列回EventModel
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        // 若config中没找到这个事件的处理handler，丢弃该事件
                        if (!config.containsKey(eventModel.getType())) {
                            continue;
                        }
                        // 找到EventModel事件类型，调用对应Handler处理
                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        };
        // 执行
        threadPool.getThreadPool().execute(task);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private class ThreadPool {
        // 线程池核心池的大小
        private final int CORE_SIZE = 8;
        //  线程池的最大线程数
        private final int MAX_SIZE = 12;
        // 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间
        private final long KEEP_ALIVE_TIME = 30;
        // ArrayBlockingQueue的大小
        private final int QUEUE_SIZE = 50000;

        private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(QUEUE_SIZE), new ThreadPoolExecutor.AbortPolicy());

        private ThreadPoolExecutor getThreadPool() {
            return threadPool;
        }
    }

}


