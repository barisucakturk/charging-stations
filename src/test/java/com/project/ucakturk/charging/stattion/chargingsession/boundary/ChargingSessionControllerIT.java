package com.project.ucakturk.charging.stattion.chargingsession.boundary;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.project.ucakturk.charging.stattion.chargingsession.ChargingStationApplication;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionPostRequestDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionResponseDto;

import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChargingStationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChargingSessionControllerIT {

    @LocalServerPort
    private int port;

    private String chargingSessionBaseUrl = "/chargingSessions";
    private String chargingSessionPutUrl = "/chargingSessions/{id}";
    private String chargingSessionSummaryUrl = "/chargingSessions/summary";

    @Test
    void create_shouldSuccess() {
        String stationId = "ST-1";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();

        given().accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(chargingSessionPostRequestDto)
            .port(port)
            .post(chargingSessionBaseUrl)
            .then()
            .statusCode(HttpStatus.OK.value())
            .and()
            .body("stationId", Matchers.equalTo(stationId))
            .log()
            .ifValidationFails();
    }

    @Test
    void create_shouldReturnBadRequest_whenStationIdNull() {
        String stationId = null;
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();

        given().accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(chargingSessionPostRequestDto)
            .port(port)
            .post(chargingSessionBaseUrl)
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void stopCharging_ShouldSuccess() {
        //given
        String stationId = "ST-1";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();

        ChargingSessionResponseDto createdSession = given().accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(chargingSessionPostRequestDto)
            .port(port)
            .post(chargingSessionBaseUrl)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(ChargingSessionResponseDto.class);

        //then
        given().accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .pathParam("id", createdSession.getId())
            .port(port)
            .put(chargingSessionPutUrl)
            .then()
            .statusCode(HttpStatus.OK.value())
            .and()
            .body("stationId", Matchers.equalTo(stationId))
            .log()
            .ifValidationFails();
    }

    @Test
    void stopCharging_ShouldReturnBadRequest() {
        given().accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .pathParam("id", UUID.randomUUID().toString())
            .port(port)
            .put(chargingSessionPutUrl)
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getAllSessions() {
        //given
        String stationId = "ST-3";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();

        given().accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(chargingSessionPostRequestDto)
            .port(port)
            .post(chargingSessionBaseUrl)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(ChargingSessionResponseDto.class);

        //then
        List<ChargingSessionResponseDto> result = given().accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)
            .get(chargingSessionBaseUrl)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .jsonPath()
            .getList(".", ChargingSessionResponseDto.class);

        assertNotNull(result.get(0).getStationId());
    }

    @Test
    void getSummaryOfSessions() {
        //given
        String stationId = "ST-3";
        ChargingSessionPostRequestDto chargingSessionPostRequestDto =
            ChargingSessionPostRequestDto.builder().stationId(stationId).build();

        given().accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(chargingSessionPostRequestDto)
            .port(port)
            .post(chargingSessionBaseUrl)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(ChargingSessionResponseDto.class);

        given().accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)
            .get(chargingSessionSummaryUrl)
            .then()
            .statusCode(HttpStatus.OK.value());
    }
}