package com.bx.implatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "用户设备操作DTO")
public class WebrtcGroupDeviceDTO {

    @NotNull(message = "群聊id不可为空")
    @Schema(description = "群聊id")
    private Long groupId;

    @Schema(description = "是否开启摄像头")
    private Boolean isCamera;

    @Schema(description = "是否开启麦克风")
    private Boolean isMicroPhone;

}
