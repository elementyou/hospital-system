package com.hospital.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "叫号返回")
public class CallNumberVO {
    @Schema(description = "排队号码")
    private Integer queueNumber;
}
