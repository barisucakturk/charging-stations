package com.project.ucakturk.charging.stattion.chargingsession.control.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.project.ucakturk.charging.stattion.chargingsession.control.exception.ChargingSessionValidationException;
import com.project.ucakturk.charging.stattion.chargingsession.control.mapper.ChargingSessionMapper;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionPostRequestDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionResponseDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.enums.StatusEnum;
import com.project.ucakturk.charging.stattion.chargingsession.entity.table.ChargingSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChargingSessionService {

    private static final Map<UUID, ChargingSession> CHARGING_SESSION_MAP = new HashMap<>();

    public ChargingSessionResponseDto create(ChargingSessionPostRequestDto chargingSessionPostRequestDto)
        throws ChargingSessionValidationException {
        validateChargingSession(chargingSessionPostRequestDto);
        ChargingSession chargingSession = buildChargingSession(chargingSessionPostRequestDto);
        CHARGING_SESSION_MAP.put(chargingSession.getId(), chargingSession);
        return ChargingSessionMapper.getInstance().chargingSessionToDto(chargingSession);
    }

    private void validateChargingSession(ChargingSessionPostRequestDto chargingSessionPostRequestDto)
        throws ChargingSessionValidationException {
        if (Objects.isNull(chargingSessionPostRequestDto.getStationId())) {
            throw new ChargingSessionValidationException("Station Id cannot be null!");
        }
    }

    private ChargingSession buildChargingSession(ChargingSessionPostRequestDto chargingSessionPostRequestDto) {
        return ChargingSession.builder()
            .id(UUID.randomUUID())
            .stationId(chargingSessionPostRequestDto.getStationId())
            .startedAt(LocalDateTime.now())
            .status(StatusEnum.IN_PROGRESS)
            .build();
    }
}
