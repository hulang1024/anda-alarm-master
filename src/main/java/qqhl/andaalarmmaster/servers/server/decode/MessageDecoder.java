package qqhl.andaalarmmaster.servers.server.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import qqhl.andaalarmmaster.servers.server.decode.parser.MessageParser;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in == null || in.readableBytes() < 9) {
            return;
        }

        in.markReaderIndex();
        boolean foundHeader = false;
        // 定位到消息头
        while (!foundHeader && in.isReadable()) {
            // 读消息头字节
            if (in.readUnsignedByte() == 0xad && in.readUnsignedByte() == 0xcc) {
                foundHeader = true;
                break;
            }
        }
        if (!foundHeader) {
            return;
        }

        int length = in.readUnsignedShortLE(); //消息主体字节数
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        out.add(MessageParser.parse(in));
    }
}
