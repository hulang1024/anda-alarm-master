package qqhl.andaalarmmaster.servers.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import qqhl.andaalarmmaster.api.model.HostStateInfo;
import qqhl.andaalarmmaster.servers.server.message.types.Message;
import qqhl.andaalarmmaster.servers.server.decode.MessageDecoder;
import qqhl.andaalarmmaster.service.MessageHandleService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class AlarmServer {
    public boolean isRunning = false;
    @Autowired
    private MessageHandleService messageHandleService;
    @Value("${alarmServer.port}")
    private int port;
    @Value("${alarmServer.heartbeatTimeout}")
    private int heartbeatTimeout;

    Map<Channel, ChannelAttachment> channelAttachmentMap = new HashMap<>();
    public Map<String, Channel> hostChannelMap = new HashMap<>();

    public void start() {
        isRunning = true;

        ServerBootstrap boot = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            boot.option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.ALLOCATOR.SO_BACKLOG, 1024)
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                            // 实现心跳检查
                            .addLast(new IdleStateHandler(heartbeatTimeout, heartbeatTimeout, heartbeatTimeout, TimeUnit.MILLISECONDS))
                            // 解码消息
                            .addLast(new MessageDecoder())
                            // 处理连接和消息
                            .addLast(new MessageHandler(AlarmServer.this))
                            // 编码消息
                            .addLast(new MessageEncoder());
                        }
                    });
            ChannelFuture future = boot.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public void onMessage(Message message) throws Exception {
        messageHandleService.handleMessage(message);
    }

    public HostStateInfo queryHostStateInfo(String hostId) {
        HostStateInfo hostStateInfo = new HostStateInfo();
        Channel channel =  hostChannelMap.get(hostId);
        hostStateInfo.setHostId(hostId);
        hostStateInfo.setOnline(channel != null);
        if (channel != null) {
            ChannelAttachment attr = channelAttachmentMap.get(channel);
            if (attr != null) {
                if (attr.latestHostHeartbeatMessage != null) {
                    hostStateInfo.setDefenceState(attr.latestHostHeartbeatMessage.state == 1 ? 1 : 2);
                    hostStateInfo.setSignalIntensity(attr.latestHostHeartbeatMessage.signalIntensity);
                    hostStateInfo.setSupplyVoltage(attr.latestHostHeartbeatMessage.supplyVoltage * 0.01f);
                    hostStateInfo.setOutputVoltage(attr.latestHostHeartbeatMessage.outputVoltage * 0.01f);
                    hostStateInfo.setBatteryVoltage(attr.latestHostHeartbeatMessage.batteryVoltage * 0.01f);
                    hostStateInfo.setLastUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(attr.latestHostHeartbeatTime));
                }
            }
        }

        return hostStateInfo;
    }

    public Map<String, Object> queryHostState(String hostId) {
        Map<String, Object> ret = new HashMap<>();
        Channel channel =  hostChannelMap.get(hostId);
        ret.put("online", channel != null ? 1 : 0);
        if (channel != null) {
            ChannelAttachment attr = channelAttachmentMap.get(channel);
            if (attr != null) {
                ret.put("latestHeartbeatMessage", attr.latestHostHeartbeatMessage);
                ret.put("latestHeartbeatTime", attr.latestHostHeartbeatTime);
            }
        }

        return ret;
    }

    public int sendCommand(HostCommand hostCommand) {
        Channel channel = hostChannelMap.get(hostCommand.hostId);
        if (! (channel != null && channel.isOpen())) {
            return 1;
        }
        try {
            channel.writeAndFlush(Unpooled.copiedBuffer(new byte[]{0X11, (byte) hostCommand.cmdType}));
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }
}
