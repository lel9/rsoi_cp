package ru.bmstu.cp.rsoi.patient.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.patient.domain.Patient;
import ru.bmstu.cp.rsoi.patient.exception.NoSuchPatientException;
import ru.bmstu.cp.rsoi.patient.exception.PatientAlreadyExistsException;
import ru.bmstu.cp.rsoi.patient.model.OperationOut;
import ru.bmstu.cp.rsoi.patient.repository.PatientRepository;

import java.util.Optional;

import static ru.bmstu.cp.rsoi.patient.model.OperationOut.getPatientOperation;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Patient getPatient(String id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent())
            throw new NoSuchPatientException();

        try {
            String routingKey = "operation";
            rabbitTemplate.convertAndSend("operationExchange", routingKey, getPatientOperation(id, "R"));
        } catch (Exception ex) {
            // todo логгирование
        }
        return patientOptional.get();
    }

    public String postPatient(Patient in) {
        String cardId = in.getCardId();
        if (cardId != null && patientRepository.findByCardId(cardId).isPresent())
            throw new PatientAlreadyExistsException(cardId);

        in.setId(null);
        Patient save = patientRepository.save(in);
        String id = save.getId();

        try {
            String routingKey = "operation";
            rabbitTemplate.convertAndSend("operationExchange", routingKey, getPatientOperation(id, "C"));
        } catch (Exception ex) {
            // todo логгирование
        }

        return id;
    }

    public void putPatient(final Patient in, final String id) {
        String cardId = in.getCardId();
        if (cardId != null) {
            Optional<Patient> byCardId = patientRepository.findByCardId(cardId);
            if (byCardId.isPresent() && !byCardId.get().getId().equals(id))
                throw new PatientAlreadyExistsException(cardId);
        }

        in.setId(id);
        Patient save = patientRepository.save(in);

        receptionService.updateReceptions(save);

        try {
            String routingKey = "operation";
            rabbitTemplate.convertAndSend("operationExchange", routingKey, getPatientOperation(id, "U"));
        } catch (Exception ex) {
            // todo логгирование
        }
    }

    public void deletePatient(String id) {
        patientRepository.deleteById(id);
        receptionService.deleteReceptionByPatient(id);

        try {
            String routingKey = "operation";
            rabbitTemplate.convertAndSend("operationExchange", routingKey, getPatientOperation(id, "D"));
        } catch (Exception ex) {
            // todo логгирование
        }
    }

    public Page<Patient> findPatients(String text, int page, int size) {
        return patientRepository.findByCardIdStartsWith(text, PageRequest.of(page, size));
    }

    public Optional<Patient> findById(String patientId) {
        return patientRepository.findById(patientId);
    }
}
