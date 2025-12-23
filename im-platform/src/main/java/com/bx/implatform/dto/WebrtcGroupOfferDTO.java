package com.bx.implatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "回复用户连接请求DTO")
public class WebrtcGroupOfferDTO {

    @NotNull(message = "群聊id不可为空")
    @Schema(description = "群聊id")
    private Long groupId;

    @NotNull(message = "用户id不可为空")
    @Schema(description = "用户id,代表回复谁的连接请求")
    private Long userId;

    @NotEmpty(message = "offer不可为空")
    @Schema(description = "用户offer信息")
    private String offer;

}
