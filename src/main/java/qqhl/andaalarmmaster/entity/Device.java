package qqhl.andaalarmmaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name="ided_device")
@Entity
@Data
public class Device {
    @JsonIgnore
    @EmbeddedId
    private Device.PK pk;
    private Integer orgId;
    private Date addTime;

    @SuppressWarnings("serial")
    @Data
    public static class PK implements Serializable {
        private String deviceId;
        private String deviceType;

        public PK() {}

        public PK(String deviceId, String deviceType) {
            this.deviceId = deviceId;
            this.deviceType = deviceType;
        }
    }
}
