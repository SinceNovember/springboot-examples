package com.xxl.job.admin.custom.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {

    @XxlJob("testJob")
    public void testJob() {
        System.out.println("testJob");
    }
}
