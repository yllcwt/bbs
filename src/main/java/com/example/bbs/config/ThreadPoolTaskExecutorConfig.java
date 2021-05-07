package com.example.bbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * spring线程池配置
 * <p>
 * 直接注入使用即可
 * <code> @Autowired
 * private ThreadPoolTaskExecutor threadPoolTaskExecutor;
 * </code>
 *
 * @author quifar
 **/
@EnableAsync
@Configuration
public class ThreadPoolTaskExecutorConfig {

    /**
     * 线程池维护线程的最少数量
     */
    private final static int CORE_POOL_SIZE = 8;

    /**
     * 线程池维护线程的最大数量(超过最大值，workQueue将拒绝执行任务)
     */
    private final static int MAX_POOL_SIZE = 16;

    /**
     * 线程池空闲线程存活的时间
     */
    private final static int KEEP_ALIVE_SECONDS = 60;

    /**
     * 线程池被阻塞线程队列容量
     */
    private final static int BLOCKING_QUEUE_CAPACITY = Integer.MAX_VALUE;

    /**
     * 常用线程池
     *
     * @return
     */
    @Bean("threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("common-");
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        executor.setQueueCapacity(BLOCKING_QUEUE_CAPACITY);
        return executor;
    }

    /**
     * 渠道门店商品同步线程池
     * 由于该同步任务重，需要用线程池隔离，避免影响其他任务
     *
     * @return
     */
    @Bean("channelDeptGoodsThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor channelDeptGoodsThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("CDG Task-");
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(200);
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        executor.setQueueCapacity(BLOCKING_QUEUE_CAPACITY);
        return executor;
    }
}
