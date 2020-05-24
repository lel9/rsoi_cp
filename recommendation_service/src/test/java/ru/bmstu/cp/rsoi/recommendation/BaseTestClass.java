package ru.bmstu.cp.rsoi.recommendation;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import ru.bmstu.cp.rsoi.recommendation.domain.Profile;
import ru.bmstu.cp.rsoi.recommendation.domain.Recommendation;
import ru.bmstu.cp.rsoi.recommendation.repository.RecommendationRepository;
import ru.bmstu.cp.rsoi.recommendation.web.controller.RecommendationController;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
//@WithMockUser(username = "olga", roles = {"ADMIN"})
@AutoConfigureMessageVerifier
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "stubs:profile_service:+:stubs:8090")
@TestPropertySource("classpath:test.properties")
public abstract class BaseTestClass {

    @Autowired
    private RecommendationController recommendationController;

    @Autowired
    private RecommendationRepository repository;

    @Before
    public void setup() {

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        GrantedAuthority role_admin = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(role_admin);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("olga");
        Mockito.when(securityContext.getAuthentication().getAuthorities()).thenReturn((List) grantedAuthorities);
        SecurityContextHolder.setContext(securityContext);

        StandaloneMockMvcBuilder standaloneMockMvcBuilder
                = MockMvcBuilders.standaloneSetup(recommendationController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);

        Recommendation recommendation = new Recommendation();
        recommendation.setId("0");
        recommendation.setDrugId("0");
        recommendation.setText("text");
        recommendation.setDate(0L);
        Profile profile = new Profile();
        profile.setId("olga");
        profile.setDisplayName("string");
        recommendation.setAuthor(profile);
        repository.save(recommendation);
    }

    @After
    public void clear() {
        repository.deleteAll();
    }

}
