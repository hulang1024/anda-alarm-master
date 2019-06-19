package qqhl.andaalarmmaster.servers.server.decode.parser;

import io.netty.buffer.ByteBuf;
import qqhl.andaalarmmaster.servers.server.message.types.Message;
import qqhl.andaalarmmaster.servers.server.message.types.CommandResult;

/**
 * @author hulang
 */
public class CommandResultParser extends AbstractMessageParser {
    public Message parse(ByteBuf byteBuf) {
        CommandResult msg = new CommandResult();
        byteBuf.skipBytes(1);
        msg.cmdType = byteBuf.readUnsignedByte();
        msg.resultCode = byteBuf.readUnsignedByte();
        return msg;
    }
}
