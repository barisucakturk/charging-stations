package com.project.ucakturk.charging.stattion.chargingsession.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChargingSessionSummaryResponseDto {
    private static final long serialVersionUID = 1L;
    private long totalCount;
    private long startedCount;
    private long stoppedCount;
}
