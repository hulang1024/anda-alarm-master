package qqhl.andaalarmmaster.servers.server.message.types;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hulang
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class HostHeartbeatMessage extends Message {
    /* 设备状态 */
    public int state;
    /* 信号强度 */
    public int signalIntensity;
    /* 供电电压 0.01V */
    public int supplyVoltage;
    /* 输出电压 0.01V */
    public int outputVoltage;
    /* 电池电压 0.01V */
    public int batteryVoltage;
}
