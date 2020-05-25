package ru.bmstu.cp.rsoi.patient;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import ru.bmstu.cp.rsoi.patient.domain.*;
import ru.bmstu.cp.rsoi.patient.repository.PatientRepository;
import ru.bmstu.cp.rsoi.patient.repository.ReceptionRepository;
import ru.bmstu.cp.rsoi.patient.web.controller.PatientController;
import ru.bmstu.cp.rsoi.patient.web.controller.ReceptionController;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@WithMockUser(username = "olga", roles = {"ADMIN"})
@AutoConfigureMessageVerifier
@TestPropertySource("classpath:test.properties")
public abstract class BaseTestClass {

    @Autowired
    private PatientController patientController;

    @Autowired
    private ReceptionController receptionController;

    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Before
    public void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder
                = MockMvcBuilders.standaloneSetup(patientController, receptionController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);

        Patient patient = new Patient();
        patient.setId("5ec844100a975a009f2b9195");
        patient.setSex('m');
        patient.setCardId("0000");
        patient.setBirthday("2020-05-25T16:43:14.144Z");

        Patient patient2 = new Patient();
        patient2.setId("5ec815d30a975a009f2b9191");
        patient2.setSex('m');
        patient2.setCardId("9999");
        patient2.setBirthday("2020-05-25T16:43:14.144Z");

        patientRepository.save(patient);
        patientRepository.save(patient2);

        Reception reception = new Reception();
        reception.setPatient(patient);
        reception.setId("0");
        reception.setDate("2020-05-25T16:43:14.144Z");

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setText("string");
        reception.setDiagnosis(diagnosis);

        State state = new State();
        state.setSex('m');
        state.setMonths(0);
        state.setYears(0);
        state.setSpecialistsConclusions("string");
        state.setPlaints("string");
        state.setObjectiveInspection("string");
        state.setExaminationsResults("string");
        state.setDiseaseAnamnesis("string");
        state.setLifeAnamnesis("string");
        reception.setState(state);

        Drug drug = new Drug();
        drug.setId("0");
        drug.setManufacturer("string");
        drug.setReleaseFormVSDosage("string");
        drug.setTradeName("string");
        reception.setDrugs(Collections.singletonList(drug));

        receptionRepository.save(reception);
    }

    @After
    public void clear() {
        receptionRepository.deleteAll();
        patientRepository.deleteAll();
    }
}
