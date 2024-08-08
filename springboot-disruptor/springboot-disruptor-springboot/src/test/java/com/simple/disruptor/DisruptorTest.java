package com.simple.disruptor;

import com.simple.disruptor.SeriesData;
import com.simple.disruptor.SeriesDataEventQueueHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DisruptorTest {

    @Resource
    private SeriesDataEventQueueHelper seriesDataEventQueueHelper;
    @Test
    public void demo(){
        seriesDataEventQueueHelper.publishEvent(new SeriesData("hello word"));
        seriesDataEventQueueHelper.publishEvent(new SeriesData("hello word"));
        seriesDataEventQueueHelper.publishEvent(new SeriesData("hello word"));
        seriesDataEventQueueHelper.publishEvent(new SeriesData("hello word"));
        seriesDataEventQueueHelper.publishEvent(new SeriesData("hello word"));
        seriesDataEventQueueHelper.publishEvent(new SeriesData("hello word"));

    }
}
