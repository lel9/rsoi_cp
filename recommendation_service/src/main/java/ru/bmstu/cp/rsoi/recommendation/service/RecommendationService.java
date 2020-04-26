package ru.bmstu.cp.rsoi.recommendation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.recommendation.domain.Profile;
import ru.bmstu.cp.rsoi.recommendation.domain.Recommendation;
import ru.bmstu.cp.rsoi.recommendation.exception.NoSuchRecommendationException;
import ru.bmstu.cp.rsoi.recommendation.model.RecommendationIn;
import ru.bmstu.cp.rsoi.recommendation.repository.RecommendationRepository;

import java.util.Optional;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    public Page<Recommendation> getRecommendations(String drugId, int page, int size) {
        return recommendationRepository.findByDrugId(drugId, PageRequest.of(page, size));
    }

    public String postRecommendation(RecommendationIn recommendationIn) {
        Recommendation recommendation = new Recommendation();
        recommendation.setDate(System.currentTimeMillis());
        recommendation.setDrugId(recommendationIn.getDrugId());
        recommendation.setText(recommendationIn.getText());
        recommendation.setAuthor(new Profile());
        Recommendation save = recommendationRepository.save(recommendation);
        return save.getId();
    }

    public String putRecommendation(String id, RecommendationIn recommendationIn) {
        Optional<Recommendation> oldRecommendation = recommendationRepository.findById(id);
        if (!oldRecommendation.isPresent()) {
            Recommendation recommendation = new Recommendation();
            recommendation.setId(id);
            recommendation.setDate(System.currentTimeMillis());
            recommendation.setDrugId(recommendationIn.getDrugId());
            recommendation.setText(recommendationIn.getText());
            recommendation.setAuthor(new Profile());
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
        if (!recommendationRepository.findById(id).isPresent())
            throw new NoSuchRecommendationException();

        recommendationRepository.deleteById(id);
    }
}
