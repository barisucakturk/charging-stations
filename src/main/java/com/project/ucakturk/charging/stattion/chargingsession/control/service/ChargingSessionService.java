package com.project.ucakturk.charging.stattion.chargingsession.control.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.project.ucakturk.charging.stattion.chargingsession.control.exception.ChargingSessionValidationException;
import com.project.ucakturk.charging.stattion.chargingsession.control.mapper.ChargingSessionMapper;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionPostRequestDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionResponseDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionSummaryResponseDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.enums.StatusEnum;
import com.project.ucakturk.charging.stattion.chargingsession.entity.table.ChargingSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChargingSessionService {

    private static final Map<UUID, ChargingSession> CHARGING_SESSION_MAP = new HashMap<>();
    private final ChargingSessionValidationService chargingSessionValidationService;

    public ChargingSessionResponseDto create(ChargingSessionPostRequestDto chargingSessionPostRequestDto)
        throws ChargingSessionValidationException {
        chargingSessionValidationService.validateChargingSession(chargingSessionPostRequestDto);
        ChargingSession chargingSession = buildChargingSession(chargingSessionPostRequestDto);
        CHARGING_SESSION_MAP.put(chargingSession.getId(), chargingSession);
        return ChargingSessionMapper.getInstance().chargingSessionToDto(chargingSession);
    }

    private ChargingSession buildChargingSession(ChargingSessionPostRequestDto chargingSessionPostRequestDto) {
        return ChargingSession.builder()
            .id(UUID.randomUUID())
            .stationId(chargingSessionPostRequestDto.getStationId())
            .startedAt(LocalDateTime.now())
            .status(StatusEnum.IN_PROGRESS)
            .build();
    }

    public ChargingSessionResponseDto stopCharging(String id) throws ChargingSessionValidationException {
        ChargingSession chargingSession = CHARGING_SESSION_MAP.get(UUID.fromString(id));
        chargingSessionValidationService.validateForStoppingSession(UUID.fromString(id), chargingSession);
        chargingSession.setStatus(StatusEnum.FINISHED);
        chargingSession.setStoppedAt(LocalDateTime.now());
        return ChargingSessionMapper.getInstance().chargingSessionToDto(chargingSession);
    }

    public List<ChargingSessionResponseDto> getAllChargingSessions() {
        return ChargingSessionMapper.getInstance().chargingSessionListToDtoList(CHARGING_SESSION_MAP.values());
    }

    public ChargingSessionSummaryResponseDto getSummaryOfSessions() {
        long startedSessionsInLastMinute = CHARGING_SESSION_MAP.values()
            .stream()
            .filter(
                chargingSession -> LocalDateTime.now().minusMinutes(1).compareTo(chargingSession.getStartedAt()) < 0)
            .count();
        long stoppedSessionsInLastMinute = CHARGING_SESSION_MAP.values()
            .stream()
            .filter(chargingSession -> Objects.nonNull(chargingSession.getStoppedAt()))
            .filter(
                chargingSession -> LocalDateTime.now().minusMinutes(1).compareTo(chargingSession.getStoppedAt()) < 0)
            .count();
        return buildSummaryChargingSession(startedSessionsInLastMinute, stoppedSessionsInLastMinute);
    }

    private ChargingSessionSummaryResponseDto buildSummaryChargingSession(long startedSessionsInLastMinute,
        long stoppedSessionsInLastMinute) {
        return ChargingSessionSummaryResponseDto.builder()
            .startedCount(startedSessionsInLastMinute)
            .stoppedCount(stoppedSessionsInLastMinute)
            .totalCount(startedSessionsInLastMinute + stoppedSessionsInLastMinute)
            .build();
    }
}
