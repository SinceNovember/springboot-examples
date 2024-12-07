package com.simple.disruptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"com.simple.disruptor"})
public class DisruptorConfig {

    /**
     * smsParamEventHandler1
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataWorkHandler smsParamEventHandler1() {
        return new SeriesDataWorkHandler();
    }

    /**
     * smsParamEventHandler2
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataWorkHandler smsParamEventHandler2() {
        return new SeriesDataWorkHandler();
    }

    /**
     * smsParamEventHandler3
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataWorkHandler smsParamEventHandler3() {
        return new SeriesDataWorkHandler();
    }


    /**
     * smsParamEventHandler4
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataWorkHandler smsParamEventHandler4() {
        return new SeriesDataWorkHandler();
    }

    /**
     * smsParamEventHandler5
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataWorkHandler smsParamEventHandler5() {
        return new SeriesDataWorkHandler();
    }


    /**
     * smsParamEventHandler5
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataWorkHandler smsParamEventHandler6() {
        return new SeriesDataWorkHandler();
    }
}
