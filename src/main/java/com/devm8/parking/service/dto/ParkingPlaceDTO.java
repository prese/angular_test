package com.devm8.parking.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ParkingPlace entity.
 */
public class ParkingPlaceDTO implements Serializable {

    private Long id;

    private ZonedDateTime dateIn;

    private ZonedDateTime dateOut;


    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateIn() {
        return dateIn;
    }

    public void setDateIn(ZonedDateTime dateIn) {
        this.dateIn = dateIn;
    }

    public ZonedDateTime getDateOut() {
        return dateOut;
    }

    public void setDateOut(ZonedDateTime dateOut) {
        this.dateOut = dateOut;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParkingPlaceDTO parkingPlaceDTO = (ParkingPlaceDTO) o;
        if (parkingPlaceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), parkingPlaceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ParkingPlaceDTO{" +
            "id=" + getId() +
            ", dateIn='" + getDateIn() + "'" +
            ", dateOut='" + getDateOut() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}
