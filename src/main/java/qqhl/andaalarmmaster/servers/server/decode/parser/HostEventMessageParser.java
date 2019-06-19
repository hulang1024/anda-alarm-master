package qqhl.andaalarmmaster.servers.server.decode.parser;

import io.netty.buffer.ByteBuf;
import qqhl.andaalarmmaster.servers.server.message.types.HostEventMessage;
import qqhl.andaalarmmaster.servers.server.message.types.Message;

import java.util.Calendar;

/**
 * @author hulang
 */
public class HostEventMessageParser extends AbstractMessageParser {
    public Message parse(ByteBuf byteBuf) {
        HostEventMessage msg = new HostEventMessage();
        msg.state = byteBuf.readUnsignedByte();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, byteBuf.readUnsignedByte());
        cal.set(Calendar.MINUTE, byteBuf.readUnsignedByte());
        cal.set(Calendar.HOUR_OF_DAY, byteBuf.readUnsignedByte());
        cal.set(Calendar.DATE, byteBuf.readUnsignedByte());
        cal.set(Calendar.MONTH, byteBuf.readUnsignedByte() - 1);
        cal.set(Calendar.YEAR, byteBuf.readUnsignedByte() + (byteBuf.readUnsignedByte() * 100));
        msg.datetime = cal.getTime();

        msg.eventType = byteBuf.readUnsignedShort();//big-endian
        msg.operator = byteBuf.readUnsignedByte();
        msg.defenceArea = byteBuf.readUnsignedShortLE();
        return msg;
    }
}
