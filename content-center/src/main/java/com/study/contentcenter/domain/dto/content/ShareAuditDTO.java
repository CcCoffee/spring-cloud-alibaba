package com.study.contentcenter.domain.dto.content;

import com.study.contentcenter.domain.enums.ShareAuditStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareAuditDTO {

    private ShareAuditStatus shareAuditStatus;
    private String reason;
}
