package com.bx.implatform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户加入群通话失败VO")
public class WebrtcGroupFailedVO {

    @Schema(description = "失败用户列表")
    private List<Long> userIds;

    @Schema(description = "失败原因")
    private String reason;
}
