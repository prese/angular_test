package com.devm8.parking.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ParkingPlace.
 */
@Entity
@Table(name = "parking_place")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParkingPlace implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_in")
    private ZonedDateTime dateIn;

    @Column(name = "date_out")
    private ZonedDateTime dateOut;

    @ManyToOne
    @JsonIgnoreProperties("parkingPlaces")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateIn() {
        return dateIn;
    }

    public ParkingPlace dateIn(ZonedDateTime dateIn) {
        this.dateIn = dateIn;
        return this;
    }

    public void setDateIn(ZonedDateTime dateIn) {
        this.dateIn = dateIn;
    }

    public ZonedDateTime getDateOut() {
        return dateOut;
    }

    public ParkingPlace dateOut(ZonedDateTime dateOut) {
        this.dateOut = dateOut;
        return this;
    }

    public void setDateOut(ZonedDateTime dateOut) {
        this.dateOut = dateOut;
    }

    public User getUser() {
        return user;
    }

    public ParkingPlace user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParkingPlace parkingPlace = (ParkingPlace) o;
        if (parkingPlace.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), parkingPlace.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ParkingPlace{" +
            "id=" + getId() +
            ", dateIn='" + getDateIn() + "'" +
            ", dateOut='" + getDateOut() + "'" +
            "}";
    }
}
