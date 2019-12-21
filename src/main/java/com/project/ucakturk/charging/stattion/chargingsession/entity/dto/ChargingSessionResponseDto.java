package com.project.ucakturk.charging.stattion.chargingsession.entity.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.project.ucakturk.charging.stattion.chargingsession.entity.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChargingSessionResponseDto {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String stationId;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private StatusEnum status;
}
