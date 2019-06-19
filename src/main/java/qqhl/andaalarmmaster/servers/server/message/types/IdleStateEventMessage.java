package qqhl.andaalarmmaster.servers.server.message.types;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author hulang
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class IdleStateEventMessage extends Message {
    public Date datetime;

    public IdleStateEventMessage() {
        this.type = 0;
    }
}
