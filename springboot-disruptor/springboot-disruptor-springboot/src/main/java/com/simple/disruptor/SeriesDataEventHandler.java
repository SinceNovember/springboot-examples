package com.simple.disruptor;

import com.lmax.disruptor.WorkHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeriesDataEventHandler implements WorkHandler<SeriesDataEvent> {
    private Logger logger = LoggerFactory.getLogger(SeriesDataEventHandler.class);

    @Override
    public void onEvent(SeriesDataEvent event) throws Exception {
        if (event.getValue() == null || StringUtils.isEmpty(event.getValue().getDeviceInfoStr())) {

            logger.warn("receiver series data is empty!");
        }
        logger.info(Thread.currentThread().getName());
        logger.error("hello word!");
    }
}
