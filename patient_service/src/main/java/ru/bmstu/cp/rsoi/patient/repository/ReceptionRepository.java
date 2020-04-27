package ru.bmstu.cp.rsoi.patient.repository;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.bmstu.cp.rsoi.patient.domain.Reception;

import java.util.List;

public interface ReceptionRepository extends MongoRepository<Reception, String> {
    @Query(value="{'patient.$id': ?0}")
    List<Reception> findByPatient(String id, Sort sort);

    @Query(value="{'patient.$id': ?0}")
    List<Reception> findByPatient(String id);

    @Query(value = "{'patient.$id': ?0}}", delete = true)
    void deleteReceptionByPatient(String id);

}
