package qqhl.andaalarmmaster.servers.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<ByteBuf> {
    private static final byte[] MESSAGE_BEG = new byte[]{(byte) 0xAD, (byte) 0xCC};
    private static final byte[] MESSAGE_END = new byte[]{0x0D, 0x0A, 0x0D, 0x0A};

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (msg.readableBytes() != 4) {
            out.writeBytes(MESSAGE_BEG);
            out.writeShortLE(msg.readableBytes());
            out.writeBytes(msg);
            out.writeBytes(MESSAGE_END);
        } else {
            out.writeBytes(msg);
        }
    }
}
