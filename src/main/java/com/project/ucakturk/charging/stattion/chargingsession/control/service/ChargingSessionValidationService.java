package com.project.ucakturk.charging.stattion.chargingsession.control.service;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.project.ucakturk.charging.stattion.chargingsession.control.exception.ChargingSessionValidationException;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionPostRequestDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.enums.StatusEnum;
import com.project.ucakturk.charging.stattion.chargingsession.entity.table.ChargingSession;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChargingSessionValidationService {

    void validateChargingSession(ChargingSessionPostRequestDto chargingSessionPostRequestDto)
        throws ChargingSessionValidationException {
        if (Objects.isNull(chargingSessionPostRequestDto.getStationId())) {
            throw new ChargingSessionValidationException("Station Id cannot be null!");
        }
    }

    void validateForStoppingSession(UUID id, ChargingSession chargingSession)
        throws ChargingSessionValidationException {
        if (Objects.isNull(chargingSession)) {
            throw new ChargingSessionValidationException("Session not found with id: ".concat(id.toString()));
        }
        if (StatusEnum.FINISHED.equals(chargingSession.getStatus())) {
            throw new ChargingSessionValidationException(
                "Session already stopped. Station Id:".concat(chargingSession.getStationId()));
        }
    }
}
