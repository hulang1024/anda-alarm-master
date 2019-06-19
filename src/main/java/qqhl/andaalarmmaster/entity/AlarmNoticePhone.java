package qqhl.andaalarmmaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@ApiModel(value="报警通知短信号码")
@Table(name="anda_tel_alarm_notice_phone")
@Entity
@Data
public class AlarmNoticePhone {
    @JsonIgnore
    @EmbeddedId
    private AlarmNoticePhone.PK pk;
    @ApiModelProperty(value="备注")
    private String memo;

    @ApiModel
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class PK implements Serializable {
        @ApiModelProperty(value="电话ID")
        private String hostId;
        @ApiModelProperty(value="号码")
        private String phone;
    }

    public String getHostId() { return pk.hostId; }
    public String getPhone() { return pk.phone; }
}
