package ru.bmstu.cp.rsoi.patient.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.bmstu.cp.rsoi.patient.domain.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends MongoRepository<Patient, String> {
    Optional<Patient> findByCardId(String cardId);

    Page<Patient> findByCardIdStartsWith(String text, Pageable pageable);
}
