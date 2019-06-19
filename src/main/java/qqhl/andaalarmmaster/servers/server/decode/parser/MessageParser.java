package qqhl.andaalarmmaster.servers.server.decode.parser;

import io.netty.buffer.ByteBuf;
import qqhl.andaalarmmaster.servers.server.message.types.Message;

import java.io.IOException;

/**
 * 消息解析器
 * @author hulang
 */
public class MessageParser {
    /* 解析消息字节 */
    public static Message parse(ByteBuf byteBuf) throws IOException {
        int type = byteBuf.readUnsignedByte();
        AbstractMessageParser parser = factory(type);

        Message message = parser.parse(byteBuf);
        message.type = type;
        return message;
    }

    private static AbstractMessageParser factory(int type) {
        switch (type) {
            case 0x01:
                return new HostLoginRequestMessageParser();
            case 0x02:
                return new HostEventMessageParser();
            case 0X07:
                return new HostHeartbeatMessageParser();
            case 0x0a:
                return new CommandResultParser();
        }
        return null;
    }
}
