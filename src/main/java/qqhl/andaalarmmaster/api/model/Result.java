package qqhl.andaalarmmaster.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="结果", description="表示一个结果，通常是对于带有副作用的操作结果，或表示失败时的结果")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result {
    @ApiModelProperty(value="结果代码，通常0表示成功，1表示失败，大于1的值表示具体失败原因")
    private int code;
    @ApiModelProperty(value="结果详情描述")
    private String message;

    public static final Result SUCCESS = new Result(0, "成功");
    public static final Result FAILED = new Result(1, "失败");

    public static Result from(int code) {
        return code == 0 ? SUCCESS : new Result(code, "失败");
    }

    public static Result from(boolean b) {
        return b ? SUCCESS : FAILED;
    }
}
