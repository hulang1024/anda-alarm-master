package qqhl.andaalarmmaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import qqhl.andaalarmmaster.servers.server.AlarmServer;
import qqhl.andaalarmmaster.servers.websocket.WebSocketServer;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class AndaAlarmMasterApplication {
    @Autowired
    public static AlarmServer alarmServer;
    public static WebSocketServer webSocketServer;

    @PostConstruct
    void start() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AndaAlarmMasterApplication.class, args);
        alarmServer = context.getBean(AlarmServer.class);
        webSocketServer = context.getBean(WebSocketServer.class);
        // 先启动websocket
        webSocketServer.start();
        // 后启动socket，因为它是堵塞的
        alarmServer.start();
    }
}
