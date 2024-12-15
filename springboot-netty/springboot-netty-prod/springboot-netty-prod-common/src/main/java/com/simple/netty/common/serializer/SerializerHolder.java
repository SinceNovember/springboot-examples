package com.simple.netty.common.serializer;

import com.simple.netty.common.serializer.protostuff.ProtoStuffSerializer;

public enum SerializerHolder {

    // 单例实例
    INSTANCE;

    // 序列化器实例
    private final Serializer serializer;

    // 构造方法（枚举的构造方法默认是私有的）
    SerializerHolder() {
        // 初始化序列化器
        this.serializer = new ProtoStuffSerializer();
    }

    // 获取序列化器实例的方法
    public Serializer getSerializer() {
        return serializer;
    }

    // 静态方法获取序列化器（可选，为了保持原有的调用方式）
    public static Serializer serializerImpl() {
        return INSTANCE.getSerializer();
    }
}
