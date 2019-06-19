package qqhl.andaalarmmaster.servers.server.message.types;

import lombok.Data;

/**
 * 抽象消息
 * @author hulang
 */
@Data
public abstract class Message {
    /* 主机设备ID */
    public String hostId;

    public Integer type;
}
