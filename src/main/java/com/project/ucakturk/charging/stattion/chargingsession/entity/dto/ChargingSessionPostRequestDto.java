package com.project.ucakturk.charging.stattion.chargingsession.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChargingSessionPostRequestDto {

    private String stationId;
}
