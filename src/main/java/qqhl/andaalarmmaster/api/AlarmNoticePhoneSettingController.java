package qqhl.andaalarmmaster.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qqhl.andaalarmmaster.api.model.Result;
import qqhl.andaalarmmaster.dao.AlarmNoticePhoneRepo;
import qqhl.andaalarmmaster.entity.AlarmNoticePhone;

import java.util.List;

@Api(description = "电话主机报警通知短信号码")
@RequestMapping("/host/alarm-notice-phone")
@RestController
public class AlarmNoticePhoneSettingController {
    @Autowired
    private AlarmNoticePhoneRepo alarmNoticePhoneRepo;

    @ApiOperation(value="获取主机的报警通知短信号码列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name="hostId", value="电话ID", required=true, paramType="path"),
    })
    @GetMapping("/list")
    public List<AlarmNoticePhone> find(String hostId) {
        return alarmNoticePhoneRepo.findByPkHostId(hostId);
    }

    @ApiOperation(value="增加一个号码")
    @ApiImplicitParams({
        @ApiImplicitParam(name="hostId", value="电话ID", required=true, paramType="path"),
        @ApiImplicitParam(name="phone", value="号码", required=true, paramType="path"),
        @ApiImplicitParam(name="memo", value="备注", paramType="path"),
    })
    @PostMapping("/add")
    public Result add(String hostId, String phone, String memo) {
        AlarmNoticePhone record = new AlarmNoticePhone();
        record.setPk(new AlarmNoticePhone.PK(hostId, phone));
        record.setMemo(memo);
        return Result.from( alarmNoticePhoneRepo.save(record) != null);
    }

    @ApiOperation(value="修改一个号码")
    @ApiImplicitParams({
        @ApiImplicitParam(name="hostId", value="电话ID", required=true, paramType="form"),
        @ApiImplicitParam(name="phone", value="号码", required=true, paramType="form"),
        @ApiImplicitParam(name="memo", value="备注", paramType="form"),
    })
    @PostMapping("/update")
    public Result update(String hostId, String phone, String memo) {
        AlarmNoticePhone record = new AlarmNoticePhone();
        record.setPk(new AlarmNoticePhone.PK(hostId, phone));
        record.setMemo(memo);
        return Result.from( alarmNoticePhoneRepo.save(record) != null);
    }

    @ApiOperation(value="删除一个号码")
    @ApiImplicitParams({
        @ApiImplicitParam(name="hostId", value="电话ID", required=true, paramType="form"),
        @ApiImplicitParam(name="phone", value="号码", required=true, paramType="path"),
    })
    @PostMapping("/delete")
    public Result delete(AlarmNoticePhone.PK id) {
        alarmNoticePhoneRepo.deleteById(id);
        return Result.SUCCESS;
    }
}
