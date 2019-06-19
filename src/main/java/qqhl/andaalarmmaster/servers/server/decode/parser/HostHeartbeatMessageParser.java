package qqhl.andaalarmmaster.servers.server.decode.parser;

import io.netty.buffer.ByteBuf;
import qqhl.andaalarmmaster.servers.server.message.types.Message;
import qqhl.andaalarmmaster.servers.server.message.types.HostHeartbeatMessage;

/**
 * @author hulang
 */
public class HostHeartbeatMessageParser extends AbstractMessageParser {
    public Message parse(ByteBuf byteBuf) {
        HostHeartbeatMessage msg = new HostHeartbeatMessage();
        msg.state = byteBuf.readUnsignedByte();
        msg.signalIntensity = byteBuf.readUnsignedByte();
        msg.supplyVoltage = byteBuf.readUnsignedShortLE();
        msg.outputVoltage = byteBuf.readUnsignedShortLE();
        msg.batteryVoltage = byteBuf.readUnsignedShortLE();
        byteBuf.readUnsignedShortLE();
        return msg;
    }
}
