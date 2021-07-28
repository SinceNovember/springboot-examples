package com.xxl.job;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 继承 XXL-JOB IJobHandler 抽象类，通过实现 #execute(String param) 方法，从而实现定时任务的逻辑。
 * 在方法上，添加 @JobHandler 注解，设置 JobHandler 的名字。后续，我们在调度中心的控制台中，新增任务时，需要使用到这个名字。
 * execute(String param) 方法的返回结果，为 ReturnT 类型。当返回值符合 “ReturnT.code == ReturnT.SUCCESS_CODE” 时表示任务执行成功，否则表示任务执行失败，而且可以通过 “ReturnT.msg” 回调错误信息给调度中心；从而，在任务逻辑中可以方便的控制任务执行结果。
 */
@Component
public class DemoJob extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger counts = new AtomicInteger();

    @Override
    @XxlJob(value = "demoJob")
    public ReturnT<String> execute(String param) throws Exception {
        // 打印日志
        logger.info("[execute][定时第 ({}) 次执行]", counts.incrementAndGet());
        // 返回执行成功
        return ReturnT.SUCCESS;
    }

}