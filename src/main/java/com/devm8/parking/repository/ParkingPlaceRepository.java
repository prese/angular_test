package com.devm8.parking.repository;

import com.devm8.parking.domain.ParkingPlace;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ParkingPlace entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParkingPlaceRepository extends JpaRepository<ParkingPlace, Long> {

    @Query("select parking_place from ParkingPlace parking_place where parking_place.user.login = ?#{principal.username}")
    List<ParkingPlace> findByUserIsCurrentUser();

}
