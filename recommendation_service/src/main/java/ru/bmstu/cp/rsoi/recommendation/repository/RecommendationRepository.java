package ru.bmstu.cp.rsoi.recommendation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.bmstu.cp.rsoi.recommendation.domain.Recommendation;


public interface RecommendationRepository extends MongoRepository<Recommendation, String> {

    Page<Recommendation> findByDrugId(String id, Pageable pageable);

}
