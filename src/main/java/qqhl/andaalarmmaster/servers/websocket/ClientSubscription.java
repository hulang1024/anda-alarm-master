package qqhl.andaalarmmaster.servers.websocket;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ClientSubscription {
    private String channel;
    private String hostId;
    private int[] messageTypes;
    private int[] subTypes;

    public void setMessageTypes(String csv) {
        String[] ss = csv.split(",");
        fillInts(messageTypes = new int[ss.length], ss);
    }

    public void setSubTypes(String csv) {
        if (StringUtils.isNotEmpty(csv)) {
            String[] ss = csv.split(",");
            fillInts(subTypes = new int[ss.length], ss);
        }
    }

    private void fillInts(int[] arr, String[] ss) {
        int i = 0;
        for (String s : ss)
            arr[i++] = Integer.parseInt(s);
    }
}
