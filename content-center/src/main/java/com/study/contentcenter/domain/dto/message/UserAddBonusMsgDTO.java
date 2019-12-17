package com.study.contentcenter.domain.dto.message;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddBonusMsgDTO {

    private Integer userId;
    private Integer bonus;
}
