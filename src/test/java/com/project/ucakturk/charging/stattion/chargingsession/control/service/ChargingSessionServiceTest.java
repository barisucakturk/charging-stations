package com.project.ucakturk.charging.stattion.chargingsession.control.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.ucakturk.charging.stattion.chargingsession.control.exception.ChargingSessionValidationException;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionPostRequestDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionResponseDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionSummaryResponseDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.enums.StatusEnum;

class ChargingSessionServiceTest {

    private ChargingSessionService chargingSessionService;

    @Mock
    private ChargingSessionValidationService chargingSessionValidationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        chargingSessionService = new ChargingSessionService(chargingSessionValidationService);
    }

    @Test
    void create_shouldCreateSession() throws ChargingSessionValidationException {
        //given
        String stationId = "ST-1";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();
        //when
        doNothing().when(chargingSessionValidationService).validateChargingSession(chargingSessionPostRequestDto);
        //then
        try {
            ChargingSessionResponseDto result = chargingSessionService.create(chargingSessionPostRequestDto);
            assertEquals(stationId, result.getStationId());
            assertEquals(StatusEnum.IN_PROGRESS, result.getStatus());
            assertNotNull(result.getId());
            assertNotNull(result.getStartedAt());
            assertNull(result.getStoppedAt());
        } catch (ChargingSessionValidationException e) {
            fail();
        }
    }

    @Test
    void create_shouldNotCreateSession_whenValidationFail() throws ChargingSessionValidationException {
        //given
        String stationId = "ST-1";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();
        //when
        doThrow(ChargingSessionValidationException.class).when(chargingSessionValidationService)
            .validateChargingSession(chargingSessionPostRequestDto);
        //then
        try {
            chargingSessionService.create(chargingSessionPostRequestDto);
            fail();
        } catch (ChargingSessionValidationException e) {
            assertNotNull(e);
        }
    }

    @Test
    void stopCharging_shouldUpdateSession() throws ChargingSessionValidationException {
        //given
        String stationId = "ST-1";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();
        String id = chargingSessionService.create(chargingSessionPostRequestDto).getId().toString();
        //when
        doNothing().when(chargingSessionValidationService).validateForStoppingSession(any(), any());
        //then
        try {
            ChargingSessionResponseDto result = chargingSessionService.stopCharging(id);
            assertEquals(stationId, result.getStationId());
            assertEquals(StatusEnum.FINISHED, result.getStatus());
            assertNotNull(result.getId());
            assertNotNull(result.getStartedAt());
            assertNotNull(result.getStoppedAt());
        } catch (ChargingSessionValidationException e) {
            fail();
        }
    }

    @Test
    void stopCharging_shouldNotUpdateSession_WhenValidationFails() throws ChargingSessionValidationException {
        //given
        String stationId = "ST-1";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();
        String id = chargingSessionService.create(chargingSessionPostRequestDto).getId().toString();
        //when
        doThrow(ChargingSessionValidationException.class).when(chargingSessionValidationService)
            .validateForStoppingSession(any(), any());
        //then
        try {
            chargingSessionService.stopCharging(id);
            fail();
        } catch (ChargingSessionValidationException e) {
            assertNotNull(e);
        }
    }

    @Test
    void getAllChargingSessions_ShouldReturnAllSessions() throws ChargingSessionValidationException {
        //given
        String stationId = "ST-3";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();
        chargingSessionService.create(chargingSessionPostRequestDto);
        String stationId2 = "ST-4";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto2 =
            ChargingSessionPostRequestDto.builder().stationId(stationId2).build();
        chargingSessionService.create(chargingSessionPostRequestDto2);
        //when
        //then
        List<ChargingSessionResponseDto> resultList = chargingSessionService.getAllChargingSessions();
        assertEquals(StatusEnum.IN_PROGRESS, resultList.get(0).getStatus());
        assertNotNull(resultList.get(0).getId());
        assertNotNull(resultList.get(0).getStartedAt());
        assertNull(resultList.get(0).getStoppedAt());
        assertEquals(StatusEnum.IN_PROGRESS, resultList.get(1).getStatus());
        assertNotNull(resultList.get(1).getId());
        assertNotNull(resultList.get(1).getStartedAt());
        assertNull(resultList.get(1).getStoppedAt());
    }

    @Test
    void getSummaryOfSessions_ShouldReturnLastMinutesUpdatedSessions() throws ChargingSessionValidationException {
        //given
        String stationId = "ST-5";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();
        chargingSessionService.create(chargingSessionPostRequestDto);
        String stationId2 = "ST-6";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto2 =
            ChargingSessionPostRequestDto.builder().stationId(stationId2).build();
        chargingSessionService.create(chargingSessionPostRequestDto2);
        //when
        //then
        ChargingSessionSummaryResponseDto result = chargingSessionService.getSummaryOfSessions();
        assertEquals(2, result.getTotalCount());
        assertEquals(2, result.getStartedCount());
        assertEquals(0, result.getStoppedCount());
    }
}