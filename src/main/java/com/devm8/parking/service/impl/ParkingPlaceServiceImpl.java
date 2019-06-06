package com.devm8.parking.service.impl;

import com.devm8.parking.service.ParkingPlaceService;
import com.devm8.parking.domain.ParkingPlace;
import com.devm8.parking.repository.ParkingPlaceRepository;
import com.devm8.parking.service.dto.ParkingPlaceDTO;
import com.devm8.parking.service.mapper.ParkingPlaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ParkingPlace.
 */
@Service
@Transactional
public class ParkingPlaceServiceImpl implements ParkingPlaceService {

    private final Logger log = LoggerFactory.getLogger(ParkingPlaceServiceImpl.class);

    private final ParkingPlaceRepository parkingPlaceRepository;

    private final ParkingPlaceMapper parkingPlaceMapper;

    public ParkingPlaceServiceImpl(ParkingPlaceRepository parkingPlaceRepository, ParkingPlaceMapper parkingPlaceMapper) {
        this.parkingPlaceRepository = parkingPlaceRepository;
        this.parkingPlaceMapper = parkingPlaceMapper;
    }

    /**
     * Save a parkingPlace.
     *
     * @param parkingPlaceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ParkingPlaceDTO save(ParkingPlaceDTO parkingPlaceDTO) {
        log.debug("Request to save ParkingPlace : {}", parkingPlaceDTO);
        ParkingPlace parkingPlace = parkingPlaceMapper.toEntity(parkingPlaceDTO);
        parkingPlace = parkingPlaceRepository.save(parkingPlace);
        return parkingPlaceMapper.toDto(parkingPlace);
    }

    /**
     * Get all the parkingPlaces.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ParkingPlaceDTO> findAll() {
        log.debug("Request to get all ParkingPlaces");
        return parkingPlaceRepository.findAll().stream()
            .map(parkingPlaceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one parkingPlace by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ParkingPlaceDTO> findOne(Long id) {
        log.debug("Request to get ParkingPlace : {}", id);
        return parkingPlaceRepository.findById(id)
            .map(parkingPlaceMapper::toDto);
    }

    /**
     * Delete the parkingPlace by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ParkingPlace : {}", id);
        parkingPlaceRepository.deleteById(id);
    }
}
