package qqhl.andaalarmmaster.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="主机状态信息")
@Data
public class HostStateInfo {
    @ApiModelProperty(value="电话ID")
    private String hostId;

    @ApiModelProperty(value="在线状态")
    private boolean online;
    @ApiModelProperty(value="设备状态，1=布防中，2=撤防中")
    private Integer defenceState;
    @ApiModelProperty(value="GSM信号强度，如果为0，说明没有使用GSM网络，而使用WiFi网络")
    private Integer signalIntensity;
    @ApiModelProperty(value="供电电压，单位为V")
    private Float supplyVoltage;
    @ApiModelProperty(value="输出电压，单位为V")
    private Float outputVoltage;
    @ApiModelProperty(value="电池电压，单位为V")
    private Float batteryVoltage;
    @ApiModelProperty(value="状态信息最后更新时间戳（13位，毫秒）；状态信息都是电话主动每隔40秒往服务器（即心跳）发送的信息，本字段值是最近一次心跳时间")
    private String lastUpdateTime;
}
