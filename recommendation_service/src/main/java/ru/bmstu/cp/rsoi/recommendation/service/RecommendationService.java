package ru.bmstu.cp.rsoi.recommendation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.bmstu.cp.rsoi.recommendation.domain.Profile;
import ru.bmstu.cp.rsoi.recommendation.domain.Recommendation;
import ru.bmstu.cp.rsoi.recommendation.model.RecommendationIn;
import ru.bmstu.cp.rsoi.recommendation.repository.RecommendationRepository;

import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value( "${service.profile.host}" )
    private String profileServiceHost;

    @Value( "${service.profile.port}" )
    private Integer profileServicePort;

    public Page<Recommendation> getRecommendations(String drugId, int page, int size) {
        return recommendationRepository.findByDrugId(drugId, PageRequest.of(page, size));
    }

    public Long getCountByDrugId(String drugId) {
        return recommendationRepository.countAllByDrugId(drugId);
    }

    public String postRecommendation(RecommendationIn recommendationIn) throws URISyntaxException {
        Recommendation recommendation = new Recommendation();
        recommendation.setDate(System.currentTimeMillis());
        recommendation.setDrugId(recommendationIn.getDrugId());
        recommendation.setText(recommendationIn.getText());
        recommendation.setAuthor(getProfile());
        Recommendation save = recommendationRepository.save(recommendation);
        return save.getId();
    }

    public String putRecommendation(String id, RecommendationIn recommendationIn) throws URISyntaxException {
        Optional<Recommendation> oldRecommendation = recommendationRepository.findById(id);
        if (!oldRecommendation.isPresent()) {
            Recommendation recommendation = new Recommendation();
            recommendation.setId(id);
            recommendation.setDate(System.currentTimeMillis());
            recommendation.setDrugId(recommendationIn.getDrugId());
            recommendation.setText(recommendationIn.getText());
            recommendation.setAuthor(getProfile());
            recommendationRepository.save(recommendation);
        } else {
            Recommendation recommendation = oldRecommendation.get();
            recommendation.setDrugId(recommendationIn.getDrugId());
            recommendation.setText(recommendationIn.getText());
            recommendationRepository.save(recommendation);
        }
        return id;
    }

    public void deleteRecommendation(String id) {
        recommendationRepository.deleteById(id);
    }

    private Profile getProfile() throws URISyntaxException {
        return new Profile("stub", "stub"); // todo
//        String name = "user2";
//        try {
//            URI thirdPartyApi = new URI("http", null, profileServiceHost, profileServicePort, "/api/1.0/rsoi/private/profile/" + name, null, null);
//            ResponseEntity<Profile> exchange = restTemplate.exchange(thirdPartyApi, HttpMethod.GET, null, Profile.class);
//            return exchange.getBody();
//        } catch (HttpStatusCodeException ex) {
//            return new Profile(name, null);
//        }
    }
}
