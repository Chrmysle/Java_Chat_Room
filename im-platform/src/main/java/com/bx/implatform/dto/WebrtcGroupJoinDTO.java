package com.bx.implatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "进入群视频通话DTO")
public class WebrtcGroupJoinDTO {

    @NotNull(message = "群聊id不可为空")
    @Schema(description = "群聊id")
    private Long groupId;

}
