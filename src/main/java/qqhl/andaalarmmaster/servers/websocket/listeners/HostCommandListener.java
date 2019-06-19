package qqhl.andaalarmmaster.servers.websocket.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import qqhl.andaalarmmaster.AndaAlarmMasterApplication;
import qqhl.andaalarmmaster.servers.server.HostCommand;
import qqhl.andaalarmmaster.servers.websocket.WebSocketServer;

public class HostCommandListener implements DataListener<HostCommand> {
    private WebSocketServer server;

    public HostCommandListener(WebSocketServer server) {
        this.server = server;
    }

    @Override
    public void onData(SocketIOClient client, HostCommand hostCommand, AckRequest ackRequest) throws Exception {
        int ret = AndaAlarmMasterApplication.alarmServer.sendCommand(hostCommand);
        client.sendEvent("commandReqRet", ret);
    }
}
