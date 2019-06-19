package qqhl.andaalarmmaster.servers.server.message.types;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备请求登录消息，表示已与本服务器建立连接成功
 * @author hulang
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class HostLoginRequestMessage extends Message {
    /* 设备类型 */
    public int hostDeviceType;
    /**
     * 设备状态
     * 0=撤防
     * 1=布防
    */
    public int state;
}
