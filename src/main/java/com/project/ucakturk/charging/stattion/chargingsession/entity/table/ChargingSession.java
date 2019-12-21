package com.project.ucakturk.charging.stattion.chargingsession.entity.table;

import java.time.LocalDateTime;
import java.util.UUID;

import com.project.ucakturk.charging.stattion.chargingsession.entity.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = { "stationId" })
@ToString(of = { "stationId" })
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargingSession {

    private UUID id;
    private String stationId;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private StatusEnum status;
}
