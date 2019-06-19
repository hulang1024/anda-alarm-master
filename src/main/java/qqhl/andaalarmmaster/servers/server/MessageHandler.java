package qqhl.andaalarmmaster.servers.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import qqhl.andaalarmmaster.servers.server.message.types.HostHeartbeatMessage;
import qqhl.andaalarmmaster.servers.server.message.types.HostLoginRequestMessage;
import qqhl.andaalarmmaster.servers.server.message.types.IdleStateEventMessage;
import qqhl.andaalarmmaster.servers.server.message.types.Message;

import java.util.Calendar;
import java.util.Date;


public class MessageHandler extends ChannelInboundHandlerAdapter {
    private AlarmServer alarmServer;
    private final byte[] NORMAL_RESPONSE = new byte[]{(byte)0xad, (byte)0x99, (byte)0x0d, (byte)0x0a};

    public MessageHandler(AlarmServer alarmServer) {
        this.alarmServer = alarmServer;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event =(IdleStateEvent)evt;
        switch (event.state()) {
            case READER_IDLE:
                // 如果登录之后心跳超时
                // 转发这个事件消息
                forwardIdleStateEventMessage(ctx.channel());
                // 主动关闭连接
                ctx.channel().close();//触发channelUnregistered
                break;
            case WRITER_IDLE:
            case ALL_IDLE:
                break;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ChannelAttachment state = new ChannelAttachment();
        state.idle = false;
        alarmServer.channelAttachmentMap.put(ctx.channel(), state);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        ChannelAttachment attr = alarmServer.channelAttachmentMap.get(channel);
        alarmServer.channelAttachmentMap.remove(channel);

        if (attr.hostId != null && alarmServer.hostChannelMap.get(attr.hostId) == channel) {
            forwardIdleStateEventMessage(channel);
            alarmServer.hostChannelMap.remove(attr.hostId);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        Message message = (Message)msg;
        ChannelAttachment attr = alarmServer.channelAttachmentMap.get(channel);
        if (message instanceof HostLoginRequestMessage) {
            // 记录登录主机设备标识
            attr.hostId = ((HostLoginRequestMessage) message).hostId;
            alarmServer.hostChannelMap.put(message.hostId, channel);
        }
        message.hostId = attr.hostId;

        if (message instanceof HostHeartbeatMessage) {
            attr.latestHostHeartbeatTime = System.currentTimeMillis();
            attr.latestHostHeartbeatMessage = (HostHeartbeatMessage) message;
        }

        doResponse(message, channel);
        alarmServer.onMessage(message);
    }

    private void forwardIdleStateEventMessage(Channel channel) throws Exception {
        ChannelAttachment state = alarmServer.channelAttachmentMap.get(channel);
        if (state == null || state.idle || state.hostId == null) {
            return;
        }
        state.idle = true;
        IdleStateEventMessage msg = new IdleStateEventMessage();
        msg.hostId = state.hostId;
        msg.datetime = new Date();
        alarmServer.onMessage(msg);
    }

    /* 应答 */
    private void doResponse(Message message, Channel channel) {
        byte[] reponseBytes;
        if (message instanceof HostLoginRequestMessage) {
            // 告诉主机它登录成功了
            Calendar now = Calendar.getInstance();
            int y = now.get(Calendar.YEAR);
            reponseBytes = new byte[]{
                    0x06, (byte) 0x99,
                    (byte) now.get(Calendar.SECOND),
                    (byte) now.get(Calendar.MINUTE),
                    (byte) now.get(Calendar.HOUR_OF_DAY),
                    (byte) now.get(Calendar.DATE),
                    (byte) (now.get(Calendar.MONTH) + 1),
                    (byte) (y % 100),
                    (byte) (y / 100)
            };
        } else {
            reponseBytes = NORMAL_RESPONSE;
        }
        channel.writeAndFlush(Unpooled.copiedBuffer(reponseBytes));
    }

}
