package qqhl.andaalarmmaster.servers.server.message.types;

import lombok.EqualsAndHashCode;

/**
 * @author hulang
 */
@EqualsAndHashCode(callSuper = false)
public class CommandResult extends Message {

    public int cmdType;
    /**
     * 1=成功
     * 2=失败
     * 3=已经
     */
    public int resultCode;
}
