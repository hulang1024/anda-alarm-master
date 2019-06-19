package qqhl.andaalarmmaster.servers.server;

import qqhl.andaalarmmaster.servers.server.message.types.HostHeartbeatMessage;

public class ChannelAttachment {
    public String hostId;
    public long latestHostHeartbeatTime;
    public HostHeartbeatMessage latestHostHeartbeatMessage;
    public boolean idle = false;
}
