package qqhl.andaalarmmaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
public class DeviceUserBinding {
    public static final DeviceUserBinding Null = new DeviceUserBinding();
    @JsonIgnore
    @EmbeddedId
    private Device.PK pk;
    private String userId;
    private Date addTime;

    public String getDeviceId() {
        return pk == null ? null : pk.getDeviceId();
    }

    public String getDeviceType() {
        return pk == null ? null : pk.getDeviceType();
    }
}
