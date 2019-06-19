package qqhl.andaalarmmaster.dao.devicealarm;

import java.util.HashMap;
import java.util.Map;

public class DeviceAlarmEventTypeDict {
    static Map<Integer, String> map = new HashMap(){{
        put(100, "紧急呼叫");
        put(102, "燃气泄漏");
        put(103, "烟雾报警");
        put(105, "无人活动");
        put(106, "门磁报警");
        put(400, "跌倒报警");
        put(600, "电子围栏");
        put(700, "离床报警");
        put(800, "心率异常");
        put(801, "呼吸异常");
        put(802, "血压异常");
        put(1000, "设备离线");
        put(900, "设备电压低");
    }};

    public static String value(Integer key) {
        return map.get(key);
    }
}
