package qqhl.andaalarmmaster.servers.websocket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import qqhl.andaalarmmaster.servers.server.message.types.HostEventMessage;
import qqhl.andaalarmmaster.servers.server.message.types.Message;
import qqhl.andaalarmmaster.service.ChannelQuery;
import qqhl.andaalarmmaster.servers.server.HostCommand;
import qqhl.andaalarmmaster.servers.websocket.listeners.HostCommandListener;
import qqhl.andaalarmmaster.servers.websocket.listeners.MessageSubscribeListener;
import qqhl.andaalarmmaster.servers.websocket.listeners.StateQueryListener;

/**
 * web socket server
 * @author hulang
 */
@Component
public class WebSocketServer {
    private SocketIOServer socketIOServer;
    private boolean isRunning;
    @Autowired
    private ChannelQuery channelQuery;
    @Value("${websocketServer.port}")
    private int port;
    @Value("${websocketServer.host}")
    private String host;

    private Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    public void start() {
        if (isRunning)
            return;
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        socketIOServer = new SocketIOServer(config);
        socketIOServer.addEventListener("stateQuery", String.class, new StateQueryListener(this));
        socketIOServer.addEventListener("hostCommand", HostCommand.class, new HostCommandListener(this));
        socketIOServer.addEventListener("messageSubscribe", ClientSubscription.class, new MessageSubscribeListener(this));
        socketIOServer.start();
        isRunning = true;
        logger.info("Websocket Server Started on port " + port);
    }

    public void stop() {
        if (!isRunning)
            return;
        socketIOServer.stop();
        isRunning = false;
    }

    public void sendMessageToClients(Message message) {
        String channel = null;
        for (SocketIOClient client : socketIOServer.getAllClients()) {
            boolean send = false;
            ClientSubscription subscription = client.get("subscription");
            if (subscription != null) {
                if (StringUtils.isNotEmpty(subscription.getHostId())) {
                    if (message.getHostId().equals(subscription.getHostId())) {
                        send = true;
                    }
                } else if (StringUtils.isNotEmpty(subscription.getChannel())) {
                    if (subscription.getChannel().equals("0")) {
                        send = true;
                    } else {
                        if (channel == null)
                            channel = channelQuery.getChannelByHostId(message.getHostId());
                        if (channel != null && channel.equals(subscription.getChannel())) {
                            send = true;
                        }
                    }
                }

                if (send) {
                    send = false;
                    if (ArrayUtils.contains(subscription.getMessageTypes(), message.type)) {
                        if (message instanceof HostEventMessage) {
                            if (ArrayUtils.contains(subscription.getSubTypes(), ((HostEventMessage) message).eventType)) {
                                send = true;
                            }
                        } else {
                            send = true;
                        }
                    }
                }

            }

            if (send) {
                client.sendEvent("message", message);
            }
        }
    }
}

