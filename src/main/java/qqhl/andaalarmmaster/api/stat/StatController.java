package qqhl.andaalarmmaster.api.stat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qqhl.andaalarmmaster.AndaAlarmMasterApplication;
import qqhl.andaalarmmaster.dao.HostRepo;

@Api(description = "统计")
@RequestMapping("/stat")
@RestController
public class StatController {
    @Autowired
    private HostRepo hostRepo;

    @ApiOperation(value="统计主机数量")
    @GetMapping("/host/numbers")
    public StatHostNumbers hostNumbers() {
        StatHostNumbers nums = new StatHostNumbers();
        nums.online = AndaAlarmMasterApplication.alarmServer.hostChannelMap.keySet().size();
        nums.total = (int)hostRepo.count();
        return nums;
    }
}
