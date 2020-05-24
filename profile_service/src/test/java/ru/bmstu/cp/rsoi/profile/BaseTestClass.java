package ru.bmstu.cp.rsoi.profile;

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
import ru.bmstu.cp.rsoi.profile.domain.Profile;
import ru.bmstu.cp.rsoi.profile.repository.ProfileRepository;
import ru.bmstu.cp.rsoi.profile.web.controller.ProfileController;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@WithMockUser(username = "olga", roles = {"ADMIN"})
@AutoConfigureMessageVerifier
@TestPropertySource("classpath:test.properties")
public abstract class BaseTestClass {

    @Autowired
    private ProfileController profileController;

    @Autowired
    private ProfileRepository profileRepository;

    @Before
    public void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder
                = MockMvcBuilders.standaloneSetup(profileController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);

        Profile profile = new Profile();
        profile.setId("olga");
        profile.setDisplayName("string");
        profile.setOrganization("string");
        profile.setProfession("string");

        profileRepository.save(profile);
    }

    @After
    public void clear() {
        profileRepository.deleteAll();
    }

}
