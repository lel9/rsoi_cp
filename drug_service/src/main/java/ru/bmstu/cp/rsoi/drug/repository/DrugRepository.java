package ru.bmstu.cp.rsoi.drug.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.bmstu.cp.rsoi.drug.domain.Drug;

import java.util.List;
import java.util.Optional;

public interface DrugRepository extends MongoRepository<Drug, String> {
    Optional<Drug> findByTradeName(String name);

    List<Drug> findByActiveSubstance(String activeSubstance);

    Page<Drug> findByTradeNameStartsWith(String text, Pageable pageable);
}
