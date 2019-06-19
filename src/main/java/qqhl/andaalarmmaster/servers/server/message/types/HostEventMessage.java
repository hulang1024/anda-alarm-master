package qqhl.andaalarmmaster.servers.server.message.types;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/*
 * 设备事件消息
 * @author hulang
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class HostEventMessage extends Message {
    /* 设备状态 */
    public int state;
    /* 日期时间，精确到秒 */
    public Date datetime;
    /* 事件类型 */
    public int eventType;
    /* 操作员 */
    public int operator;
    /* 防区（次序编号） */
    public int defenceArea;
}
