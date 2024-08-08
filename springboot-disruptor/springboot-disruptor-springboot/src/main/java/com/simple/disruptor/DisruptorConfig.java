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
    public SeriesDataEventHandler smsParamEventHandler1() {
        return new SeriesDataEventHandler();
    }

    /**
     * smsParamEventHandler2
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataEventHandler smsParamEventHandler2() {
        return new SeriesDataEventHandler();
    }

    /**
     * smsParamEventHandler3
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataEventHandler smsParamEventHandler3() {
        return new SeriesDataEventHandler();
    }


    /**
     * smsParamEventHandler4
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataEventHandler smsParamEventHandler4() {
        return new SeriesDataEventHandler();
    }

    /**
     * smsParamEventHandler5
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataEventHandler smsParamEventHandler5() {
        return new SeriesDataEventHandler();
    }


    /**
     * smsParamEventHandler5
     *
     * @return SeriesDataEventHandler
     */
    @Bean
    public SeriesDataEventHandler smsParamEventHandler6() {
        return new SeriesDataEventHandler();
    }
}
