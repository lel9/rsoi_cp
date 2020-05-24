package ru.bmstu.cp.rsoi.drug;

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
import ru.bmstu.cp.rsoi.drug.domain.Drug;
import ru.bmstu.cp.rsoi.drug.repository.DrugRepository;
import ru.bmstu.cp.rsoi.drug.web.controller.DrugController;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@WithMockUser(username = "olga", roles = {"ADMIN"})
@AutoConfigureMessageVerifier
@TestPropertySource("classpath:test.properties")
public abstract class BaseTestClass {

    @Autowired
    private DrugController drugController;

    @Autowired
    private DrugRepository repository;

    @Before
    public void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder
                = MockMvcBuilders.standaloneSetup(drugController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);

        Drug drug = createDrug("0", "string");
        Drug drug1 = createDrug("1", "test");

        repository.save(drug);
        repository.save(drug1);
    }

    @After
    public void clear() {
        repository.deleteAll();
    }

    protected Drug createDrug(String id, String tradeName) {
        Drug drug = new Drug();
        drug.setId(id);
        drug.setAtx("string");
        drug.setActiveSubstance("string");
        drug.setCertificateOwner("string");
        drug.setComposition("string");
        drug.setContraindications("string");
        drug.setDescription("string");
        drug.setDirectionForUse("string");
        drug.setForm("string");
        drug.setGroup("string");
        drug.setIndications("string");
        drug.setInteraction("string");
        drug.setManufacturer("string");
        drug.setOverdose("string");
        drug.setPharmacodynamics("string");
        drug.setPharmacokinetics("string");
        drug.setPregnancyAndLactation("string");
        drug.setReleaseFormVSDosage("string");
        drug.setSideEffects("string");
        drug.setSpecialInstruction("string");
        drug.setStorageLife("string");
        drug.setStorageСonditions("string");
        drug.setTradeName(tradeName);
        drug.setTransportationСonditions("string");
        drug.setVacationFromPharmacies("string");
        drug.setVehicleImpact("string");
        drug.setWithCaution("string");
        return drug;
    }
}
