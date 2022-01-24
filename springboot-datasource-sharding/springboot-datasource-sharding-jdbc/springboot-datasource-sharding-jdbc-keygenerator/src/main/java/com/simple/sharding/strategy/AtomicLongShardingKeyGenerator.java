package com.simple.sharding.strategy;

import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongShardingKeyGenerator implements ShardingKeyGenerator {

    private AtomicLong atomicLong = new AtomicLong(0);
    private Properties properties = new Properties();

    @Override
    public Comparable<?> generateKey() {
        return atomicLong.incrementAndGet();
    }

    @Override
    public String getType() {
        return "AtomicLong";
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
