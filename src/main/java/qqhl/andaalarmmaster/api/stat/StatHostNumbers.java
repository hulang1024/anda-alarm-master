package qqhl.andaalarmmaster.api.stat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "主机数量")
@Data
public class StatHostNumbers {
    @ApiModelProperty(value="注册主机总数")
    int total;
    @ApiModelProperty(value="在线主机数量")
    int online;
}
