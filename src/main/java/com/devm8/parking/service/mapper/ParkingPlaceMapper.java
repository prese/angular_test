package com.devm8.parking.service.mapper;

import com.devm8.parking.domain.*;
import com.devm8.parking.service.dto.ParkingPlaceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ParkingPlace and its DTO ParkingPlaceDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ParkingPlaceMapper extends EntityMapper<ParkingPlaceDTO, ParkingPlace> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    ParkingPlaceDTO toDto(ParkingPlace parkingPlace);

    @Mapping(source = "userId", target = "user")
    ParkingPlace toEntity(ParkingPlaceDTO parkingPlaceDTO);

    default ParkingPlace fromId(Long id) {
        if (id == null) {
            return null;
        }
        ParkingPlace parkingPlace = new ParkingPlace();
        parkingPlace.setId(id);
        return parkingPlace;
    }
}
