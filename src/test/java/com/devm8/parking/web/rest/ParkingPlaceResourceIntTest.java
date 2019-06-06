package com.devm8.parking.web.rest;

import com.devm8.parking.ParkingApp;

import com.devm8.parking.domain.ParkingPlace;
import com.devm8.parking.repository.ParkingPlaceRepository;
import com.devm8.parking.service.ParkingPlaceService;
import com.devm8.parking.service.dto.ParkingPlaceDTO;
import com.devm8.parking.service.mapper.ParkingPlaceMapper;
import com.devm8.parking.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static com.devm8.parking.web.rest.TestUtil.sameInstant;
import static com.devm8.parking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParkingPlaceResource REST controller.
 *
 * @see ParkingPlaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ParkingApp.class)
public class ParkingPlaceResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_IN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_IN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_OUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_OUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ParkingPlaceRepository parkingPlaceRepository;

    @Autowired
    private ParkingPlaceMapper parkingPlaceMapper;

    @Autowired
    private ParkingPlaceService parkingPlaceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restParkingPlaceMockMvc;

    private ParkingPlace parkingPlace;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParkingPlaceResource parkingPlaceResource = new ParkingPlaceResource(parkingPlaceService);
        this.restParkingPlaceMockMvc = MockMvcBuilders.standaloneSetup(parkingPlaceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParkingPlace createEntity(EntityManager em) {
        ParkingPlace parkingPlace = new ParkingPlace()
            .dateIn(DEFAULT_DATE_IN)
            .dateOut(DEFAULT_DATE_OUT);
        return parkingPlace;
    }

    @Before
    public void initTest() {
        parkingPlace = createEntity(em);
    }

    @Test
    @Transactional
    public void createParkingPlace() throws Exception {
        int databaseSizeBeforeCreate = parkingPlaceRepository.findAll().size();

        // Create the ParkingPlace
        ParkingPlaceDTO parkingPlaceDTO = parkingPlaceMapper.toDto(parkingPlace);
        restParkingPlaceMockMvc.perform(post("/api/parking-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parkingPlaceDTO)))
            .andExpect(status().isCreated());

        // Validate the ParkingPlace in the database
        List<ParkingPlace> parkingPlaceList = parkingPlaceRepository.findAll();
        assertThat(parkingPlaceList).hasSize(databaseSizeBeforeCreate + 1);
        ParkingPlace testParkingPlace = parkingPlaceList.get(parkingPlaceList.size() - 1);
        assertThat(testParkingPlace.getDateIn()).isEqualTo(DEFAULT_DATE_IN);
        assertThat(testParkingPlace.getDateOut()).isEqualTo(DEFAULT_DATE_OUT);
    }

    @Test
    @Transactional
    public void createParkingPlaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parkingPlaceRepository.findAll().size();

        // Create the ParkingPlace with an existing ID
        parkingPlace.setId(1L);
        ParkingPlaceDTO parkingPlaceDTO = parkingPlaceMapper.toDto(parkingPlace);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParkingPlaceMockMvc.perform(post("/api/parking-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parkingPlaceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParkingPlace in the database
        List<ParkingPlace> parkingPlaceList = parkingPlaceRepository.findAll();
        assertThat(parkingPlaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllParkingPlaces() throws Exception {
        // Initialize the database
        parkingPlaceRepository.saveAndFlush(parkingPlace);

        // Get all the parkingPlaceList
        restParkingPlaceMockMvc.perform(get("/api/parking-places?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parkingPlace.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateIn").value(hasItem(sameInstant(DEFAULT_DATE_IN))))
            .andExpect(jsonPath("$.[*].dateOut").value(hasItem(sameInstant(DEFAULT_DATE_OUT))));
    }
    
    @Test
    @Transactional
    public void getParkingPlace() throws Exception {
        // Initialize the database
        parkingPlaceRepository.saveAndFlush(parkingPlace);

        // Get the parkingPlace
        restParkingPlaceMockMvc.perform(get("/api/parking-places/{id}", parkingPlace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parkingPlace.getId().intValue()))
            .andExpect(jsonPath("$.dateIn").value(sameInstant(DEFAULT_DATE_IN)))
            .andExpect(jsonPath("$.dateOut").value(sameInstant(DEFAULT_DATE_OUT)));
    }

    @Test
    @Transactional
    public void getNonExistingParkingPlace() throws Exception {
        // Get the parkingPlace
        restParkingPlaceMockMvc.perform(get("/api/parking-places/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParkingPlace() throws Exception {
        // Initialize the database
        parkingPlaceRepository.saveAndFlush(parkingPlace);

        int databaseSizeBeforeUpdate = parkingPlaceRepository.findAll().size();

        // Update the parkingPlace
        ParkingPlace updatedParkingPlace = parkingPlaceRepository.findById(parkingPlace.getId()).get();
        // Disconnect from session so that the updates on updatedParkingPlace are not directly saved in db
        em.detach(updatedParkingPlace);
        updatedParkingPlace
            .dateIn(UPDATED_DATE_IN)
            .dateOut(UPDATED_DATE_OUT);
        ParkingPlaceDTO parkingPlaceDTO = parkingPlaceMapper.toDto(updatedParkingPlace);

        restParkingPlaceMockMvc.perform(put("/api/parking-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parkingPlaceDTO)))
            .andExpect(status().isOk());

        // Validate the ParkingPlace in the database
        List<ParkingPlace> parkingPlaceList = parkingPlaceRepository.findAll();
        assertThat(parkingPlaceList).hasSize(databaseSizeBeforeUpdate);
        ParkingPlace testParkingPlace = parkingPlaceList.get(parkingPlaceList.size() - 1);
        assertThat(testParkingPlace.getDateIn()).isEqualTo(UPDATED_DATE_IN);
        assertThat(testParkingPlace.getDateOut()).isEqualTo(UPDATED_DATE_OUT);
    }

    @Test
    @Transactional
    public void updateNonExistingParkingPlace() throws Exception {
        int databaseSizeBeforeUpdate = parkingPlaceRepository.findAll().size();

        // Create the ParkingPlace
        ParkingPlaceDTO parkingPlaceDTO = parkingPlaceMapper.toDto(parkingPlace);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkingPlaceMockMvc.perform(put("/api/parking-places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parkingPlaceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParkingPlace in the database
        List<ParkingPlace> parkingPlaceList = parkingPlaceRepository.findAll();
        assertThat(parkingPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParkingPlace() throws Exception {
        // Initialize the database
        parkingPlaceRepository.saveAndFlush(parkingPlace);

        int databaseSizeBeforeDelete = parkingPlaceRepository.findAll().size();

        // Delete the parkingPlace
        restParkingPlaceMockMvc.perform(delete("/api/parking-places/{id}", parkingPlace.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ParkingPlace> parkingPlaceList = parkingPlaceRepository.findAll();
        assertThat(parkingPlaceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParkingPlace.class);
        ParkingPlace parkingPlace1 = new ParkingPlace();
        parkingPlace1.setId(1L);
        ParkingPlace parkingPlace2 = new ParkingPlace();
        parkingPlace2.setId(parkingPlace1.getId());
        assertThat(parkingPlace1).isEqualTo(parkingPlace2);
        parkingPlace2.setId(2L);
        assertThat(parkingPlace1).isNotEqualTo(parkingPlace2);
        parkingPlace1.setId(null);
        assertThat(parkingPlace1).isNotEqualTo(parkingPlace2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParkingPlaceDTO.class);
        ParkingPlaceDTO parkingPlaceDTO1 = new ParkingPlaceDTO();
        parkingPlaceDTO1.setId(1L);
        ParkingPlaceDTO parkingPlaceDTO2 = new ParkingPlaceDTO();
        assertThat(parkingPlaceDTO1).isNotEqualTo(parkingPlaceDTO2);
        parkingPlaceDTO2.setId(parkingPlaceDTO1.getId());
        assertThat(parkingPlaceDTO1).isEqualTo(parkingPlaceDTO2);
        parkingPlaceDTO2.setId(2L);
        assertThat(parkingPlaceDTO1).isNotEqualTo(parkingPlaceDTO2);
        parkingPlaceDTO1.setId(null);
        assertThat(parkingPlaceDTO1).isNotEqualTo(parkingPlaceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(parkingPlaceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(parkingPlaceMapper.fromId(null)).isNull();
    }
}
