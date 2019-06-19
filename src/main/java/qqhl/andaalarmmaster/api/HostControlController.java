package qqhl.andaalarmmaster.api;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import qqhl.andaalarmmaster.AndaAlarmMasterApplication;
import qqhl.andaalarmmaster.api.model.HostStateInfo;
import qqhl.andaalarmmaster.api.model.Result;
import qqhl.andaalarmmaster.servers.server.HostCommand;

import java.util.Timer;
import java.util.TimerTask;

@Api(description = "电话主机状态控制相关")
@RequestMapping("/host")
@RestController
public class HostControlController {

    @ApiOperation(value="获取某个主机的状态信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name="hostId", value="电话ID", required=true, paramType="path"),
    })
    @GetMapping("/{hostId}/state")
    public HostStateInfo state(@PathVariable String hostId) {
        return AndaAlarmMasterApplication.alarmServer.queryHostStateInfo(hostId);
    }

    @ApiOperation(value="设置某个主机的布防/撤防状态")
    @ApiImplicitParams({
        @ApiImplicitParam(name="hostId", value="电话ID", required=true, paramType="path"),
        @ApiImplicitParam(name="state", value="1=布防、2=撤防", required=true, dataType = "int"),
    })
    @PostMapping("/{hostId}/defence-state")
    public Result setDefenceState(@PathVariable String hostId, int state) {
        HostCommand command = new HostCommand();
        command.hostId = hostId;
        command.cmdType = state;
        Result ret = Result.from(AndaAlarmMasterApplication.alarmServer.sendCommand(command));
        ret.setMessage(ret.getCode() == 0 ? "指令发送成功" : (ret.getCode() == 1 ? "设备不在线" : "未知错误"));
        return ret;
    }

    @ApiOperation(value="开关某个主机的警铃")
    @ApiImplicitParams({
        @ApiImplicitParam(name="hostId", value="电话ID", required=true, paramType="path"),
        @ApiImplicitParam(name="on", value="0=关，1=开", required=true, dataType = "int"),
        @ApiImplicitParam(name="offDelay", value="当开时，定时自动关，单位毫秒；-1表示不自动关", defaultValue="500", dataType = "long"),
    })
    @PostMapping("/{hostId}/bell")
    public Result turnBell(@PathVariable String hostId, int on, Long offDelay) {
        HostCommand command = new HostCommand();
        command.hostId = hostId;
        command.cmdType = on > 0 ? 0x06 : 0x07;

        int retCode = AndaAlarmMasterApplication.alarmServer.sendCommand(command);

        if (on > 0) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    HostCommand command = new HostCommand();
                    command.hostId = hostId;
                    command.cmdType = 0x07;
                    AndaAlarmMasterApplication.alarmServer.sendCommand(command);
                }
            }, offDelay != null ? offDelay : 500);
        }
        Result ret = Result.from(retCode);
        ret.setMessage(ret.getCode() == 0 ? "指令发送成功" : (ret.getCode() == 1 ? "设备不在线" : "未知错误"));
        return ret;
    }
}
