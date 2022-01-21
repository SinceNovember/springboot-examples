package com.simple.canal.sync.rocketmq.consumer;

import com.alibaba.otter.canal.protocol.FlatMessage;

import java.util.Collection;

public interface CanalSynService<T> {

    void process(FlatMessage flatMessage);

    void ddl(FlatMessage flatMessage);

    void insert(Collection<T> list);

    void update(Collection<T> list);

    void delete(Collection<T> list);



}
