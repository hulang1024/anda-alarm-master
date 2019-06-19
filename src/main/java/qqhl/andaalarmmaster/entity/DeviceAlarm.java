package qqhl.andaalarmmaster.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class DeviceAlarm {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long id;
    private String deviceId;
    private String deviceType;
    private Integer eventType;
    private Date eventTime;
    private String eventDetails;
    private String userId;
    private Integer orgId;
    private Integer handleState;
    private Date createTime;
}
