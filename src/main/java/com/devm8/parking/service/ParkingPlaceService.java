package com.devm8.parking.service;

import com.devm8.parking.service.dto.ParkingPlaceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing ParkingPlace.
 */
public interface ParkingPlaceService {

    /**
     * Save a parkingPlace.
     *
     * @param parkingPlaceDTO the entity to save
     * @return the persisted entity
     */
    ParkingPlaceDTO save(ParkingPlaceDTO parkingPlaceDTO);

    /**
     * Get all the parkingPlaces.
     *
     * @return the list of entities
     */
    List<ParkingPlaceDTO> findAll();


    /**
     * Get the "id" parkingPlace.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ParkingPlaceDTO> findOne(Long id);

    /**
     * Delete the "id" parkingPlace.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
