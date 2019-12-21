package com.project.ucakturk.charging.stattion.chargingsession.control.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.project.ucakturk.charging.stattion.chargingsession.control.exception.ChargingSessionValidationException;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionPostRequestDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.enums.StatusEnum;
import com.project.ucakturk.charging.stattion.chargingsession.entity.table.ChargingSession;

class ChargingSessionValidationServiceTest {

    private ChargingSessionValidationService chargingSessionValidationService;

    @BeforeEach
    void setUp() {
        chargingSessionValidationService = new ChargingSessionValidationService();
    }

    @Test
    void validateChargingSession_ShouldSuccessAndDoNothing() {
        //given
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId("ST-1").build();
        //when
        //then
        try {
            chargingSessionValidationService.validateChargingSession(chargingSessionPostRequestDto);
        } catch (ChargingSessionValidationException e) {
            fail();
        }
    }

    @Test
    void validateChargingSession_ShouldThrowException() {
        //given
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(null).build();
        //when
        //then
        try {
            chargingSessionValidationService.validateChargingSession(chargingSessionPostRequestDto);
            fail();
        } catch (ChargingSessionValidationException e) {
            assertNotNull(e);
        }
    }

    @Test
    void validateForStoppingSession_ShouldSuccessAndDoNothing() {
        //given
        UUID id = UUID.randomUUID();
        ChargingSession chargingSession =
            ChargingSession.builder().id(id).stationId("ST-1").startedAt(LocalDateTime.now()).build();
        //when
        //then
        try {
            chargingSessionValidationService.validateForStoppingSession(id, chargingSession);
        } catch (ChargingSessionValidationException e) {
            fail();
        }
    }

    @Test
    void validateForStoppingSession_ShouldThrowException_WhenIdInvalid() {
        //given
        UUID id = UUID.randomUUID();
        //when
        //then
        try {
            chargingSessionValidationService.validateForStoppingSession(id, null);
            fail();
        } catch (ChargingSessionValidationException e) {
            assertNotNull(e);
        }
    }

    @Test
    void validateForStoppingSession_ShouldThrowException_WhenStatusAlreadyFinished() {
        //given
        UUID id = UUID.randomUUID();
        ChargingSession chargingSession = ChargingSession.builder()
            .id(id)
            .stationId("ST-1")
            .startedAt(LocalDateTime.now())
            .stoppedAt(LocalDateTime.now())
            .status(StatusEnum.FINISHED)
            .build();
        //when
        //then
        try {
            chargingSessionValidationService.validateForStoppingSession(id, chargingSession);
            fail();
        } catch (ChargingSessionValidationException e) {
            assertNotNull(e);
        }
    }
}