package com.simple.disruptor;

public class SeriesData {
    private String deviceInfoStr;

    public SeriesData() {
    }

    public SeriesData(String deviceInfoStr) {
        this.deviceInfoStr = deviceInfoStr;
    }

    public String getDeviceInfoStr() {
        return deviceInfoStr;
    }

    public void setDeviceInfoStr(String deviceInfoStr) {
        this.deviceInfoStr = deviceInfoStr;
    }

    @Override
    public String toString() {
        return "SeriesData{" +
                "deviceInfoStr='" + deviceInfoStr + '\'' +
                '}';
    }
}
