package com.simple.sharding.strategy;

import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

import java.util.Properties;
import java.util.UUID;

public class UUIDShardingKeyGenerator implements ShardingKeyGenerator {
    @Override
    public Comparable<?> generateKey() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public String getType() {
        return "UUID";
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
