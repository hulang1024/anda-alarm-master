package qqhl.andaalarmmaster.service;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qqhl.andaalarmmaster.AndaAlarmMasterApplication;
import qqhl.andaalarmmaster.dao.AlarmNoticePhoneRepo;
import qqhl.andaalarmmaster.servers.server.message.types.HostEventMessage;
import qqhl.andaalarmmaster.servers.server.message.types.IdleStateEventMessage;
import qqhl.andaalarmmaster.servers.server.message.types.Message;
import qqhl.andaalarmmaster.entity.Device;
import qqhl.andaalarmmaster.dao.DeviceRepo;
import qqhl.andaalarmmaster.entity.Host;
import qqhl.andaalarmmaster.dao.HostRepo;
import qqhl.andaalarmmaster.entity.DeviceAlarm;
import qqhl.andaalarmmaster.dao.devicealarm.DeviceAlarmEventTypeDict;
import qqhl.andaalarmmaster.dao.devicealarm.DeviceAlarmRepo;
import qqhl.andaalarmmaster.entity.DeviceUserBinding;
import qqhl.andaalarmmaster.dao.DeviceUserBindingRepo;
import qqhl.andaalarmmaster.dao.UserDao;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageHandleService {
    @Autowired
    private HostRepo hostRepo;
    @Autowired
    private DeviceUserBindingRepo deviceUserBindingRepo;
    @Autowired
    private DeviceRepo deviceRepo;
    @Autowired
    private DeviceAlarmRepo deviceAlarmRepo;
    @Autowired
    private AlarmNoticePhoneRepo alarmNoticePhoneRepo;
    @Autowired
    private UserDao userDao;

    public void handleMessage(Message message) {
        AndaAlarmMasterApplication.webSocketServer.sendMessageToClients(message);

        if (!(message instanceof HostEventMessage || message instanceof IdleStateEventMessage)) {
            return;
        }
        boolean isSaveRecord = false; //是否保存数据库记录
        boolean isNoticable = false;  //是否做紧急通知
        int eventType = 0;
        String eventDetails = null;
        Date eventTime = null;
        if (message instanceof HostEventMessage) {
            HostEventMessage msg = (HostEventMessage) message;
            if (ArrayUtils.contains(new int[]{4369, 4384, 4450, 4423, 4404, 4866, 4996}, msg.getEventType())) {
                isSaveRecord = true;
                isNoticable = true;
                eventTime = msg.getDatetime();
                // 做一个报警类型转换
                switch (msg.getEventType()) {
                    case 4384:
                        eventType = 100;
                        break;
                    case 4450:
                        eventType = 102;
                        break;
                    case 4369:
                        eventType = 103;
                        break;
                    case 4404:
                        eventType = 106;
                        break;
                    case 4423:
                        eventType = 105;
                        break;
                    case 4866://电池电压低
                        eventType = 900;
                        break;
                    case 4996://传感器电池电压低
                        eventType = 900;
                        eventDetails = "{\"defenceArea\": " + msg.getDefenceArea() + "}";
                        break;
                }
            }
        } else if (message instanceof IdleStateEventMessage) {
            isSaveRecord = true;
            isNoticable = true;
            eventType = 1000;
            eventTime = new Date();
        }

        // 该设备绑定的用户
        UserInfo user = null;
        DeviceAlarm deviceAlarm = new DeviceAlarm();
        deviceAlarm.setDeviceId(message.getHostId());
        deviceAlarm.setDeviceType("telephone");
        deviceAlarm.setEventType(eventType);
        deviceAlarm.setEventTime(eventTime);
        deviceAlarm.setEventDetails(eventDetails);

        if (isSaveRecord) {
            Optional<DeviceUserBinding> deviceUserBinding = deviceUserBindingRepo.findById(new Device.PK(message.getHostId(), "telephone"));
            if (deviceUserBinding.isPresent()) {
                user = userDao.findById(deviceUserBinding.get().getUserId());
            }

            if (user != null) {
                deviceAlarm.setOrgId(user.getOrgId());
                deviceAlarm.setUserId(user.getId());
            } else {
                Optional<Device> device = deviceRepo.findById(new Device.PK(message.getHostId(), "telephone"));
                deviceAlarm.setOrgId(device.isPresent() ? device.get().getOrgId() : null);
            }
            deviceAlarm.setHandleState(0);
            deviceAlarm.setCreateTime(new Date());
            deviceAlarmRepo.save(deviceAlarm);
        }

        // 紧急报警事件
        if (user != null && isNoticable) {
            // 根据消息中的主机ID获取主机设备实体
            Host host = hostRepo.findById(message.getHostId()).orElse(Host.Null);
            if (host == Host.Null) {
                return;
            }
            doNotice(deviceAlarm, user, host);
        }
    }

    private void doNotice(DeviceAlarm deviceAlarm, UserInfo user, Host host) {
        // 获取报警通知手机号
        List<String> phones = alarmNoticePhoneRepo
            .findByPkHostId(deviceAlarm.getDeviceId()).stream().map(hu -> hu.getPhone()).collect(Collectors.toList());
        if (phones.isEmpty()) {
            return;
        }

        // 发送短信
        try {
            Map<String, String> params = new HashMap<>();
            params.put("time", new SimpleDateFormat("yyyyMMddHHmmss").format(deviceAlarm.getEventTime()));
            params.put("eventName", DeviceAlarmEventTypeDict.value(deviceAlarm.getEventType()));
            params.put("name", StringUtils.defaultIfEmpty(user.getRealName(), "未知"));
            params.put("phone", StringUtils.defaultIfEmpty(host.getTelNumber(), ""));
            params.put("address", StringUtils.defaultIfEmpty(user.getAddress(), "未知"));
            SendSms.send(phones.stream().collect(Collectors.toSet()), "SMS_157355424", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
