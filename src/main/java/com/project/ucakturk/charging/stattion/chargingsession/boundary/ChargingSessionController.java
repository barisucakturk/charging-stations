package com.project.ucakturk.charging.stattion.chargingsession.boundary;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ucakturk.charging.stattion.chargingsession.control.exception.ChargingSessionValidationException;
import com.project.ucakturk.charging.stattion.chargingsession.control.service.ChargingSessionService;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionPostRequestDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionResponseDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionSummaryResponseDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ErrorDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/chargingSessions")
@RequiredArgsConstructor
public class ChargingSessionController {

    private final ChargingSessionService chargingSessionService;

    @PostMapping
    public ResponseEntity create(@RequestBody ChargingSessionPostRequestDto chargingSessionPostRequestDto) {
        try {
            log.info("Submitting new charging session {} is started", chargingSessionPostRequestDto.getStationId());
            ChargingSessionResponseDto chargingSessionResponseDto =
                chargingSessionService.create(chargingSessionPostRequestDto);
            log.info("Submitting new charging session {} is finished", chargingSessionResponseDto.getStationId());
            return ResponseEntity.ok().body(chargingSessionResponseDto);
        } catch (ChargingSessionValidationException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(e.getMessage()));
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity stopCharging(@PathVariable("id") String id) {
        try {
            log.info("Updating charging session is started");
            ChargingSessionResponseDto chargingSessionResponseDto = chargingSessionService.stopCharging(id);
            log.info("Updating charging session is finished");
            return ResponseEntity.ok().body(chargingSessionResponseDto);
        } catch (ChargingSessionValidationException e) {
            log.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity getAllSessions() {
        try {
            log.info("Getting charging session is started");
            List<ChargingSessionResponseDto> chargingSessionResponseDto =
                chargingSessionService.getAllChargingSessions();
            log.info("Getting charging session is finished");
            return ResponseEntity.ok().body(chargingSessionResponseDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(e.getMessage()));
        }
    }

    @GetMapping(value = "/summary")
    public ResponseEntity getSummaryOfSessions() {
        try {
            log.info("Getting summary of charging session is started");
            ChargingSessionSummaryResponseDto summaryResponseDto = chargingSessionService.getSummaryOfSessions();
            log.info("Getting summary of charging session is finished");
            return ResponseEntity.ok().body(summaryResponseDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(e.getMessage()));
        }
    }
}
