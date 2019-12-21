package com.project.ucakturk.charging.stattion.chargingsession.control.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.project.ucakturk.charging.stattion.chargingsession.entity.dto.ChargingSessionResponseDto;
import com.project.ucakturk.charging.stattion.chargingsession.entity.table.ChargingSession;

@Mapper
public interface ChargingSessionMapper {

    static ChargingSessionMapper getInstance() {
        return Mappers.getMapper(ChargingSessionMapper.class);
    }

    ChargingSessionResponseDto chargingSessionToDto(ChargingSession chargingSession);

}
