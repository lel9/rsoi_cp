package ru.bmstu.cp.rsoi.patient.service;

import org.bson.types.ObjectId;
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

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.bmstu.cp.rsoi.patient.model.OperationOut.getPatientOperation;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger log = Logger.getLogger(PatientService.class.getName());

    public Patient getPatient(String id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent())
            throw new NoSuchPatientException();

        try {
            String routingKey = "operation";
            OperationOut operation = getPatientOperation(id, "R");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
        return patientOptional.get();
    }

    public String postPatient(Patient in) {
        String cardId = in.getCardId();
        if (cardId != null && patientRepository.findByCardId(cardId).isPresent())
            throw new PatientAlreadyExistsException(cardId);

        String id = ObjectId.get().toString();
        in.setId(id);
        if (in.getCardId() == null || in.getCardId().isEmpty())
            in.setCardId(id);
        patientRepository.save(in);

        try {
            String routingKey = "operation";
            OperationOut operation = getPatientOperation(id, "C");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
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
        if (in.getCardId() == null || in.getCardId().isEmpty())
            in.setCardId(id);
        Patient save = patientRepository.save(in);

        receptionService.updateReceptions(save);

        try {
            String routingKey = "operation";
            OperationOut operation = getPatientOperation(id, "U");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
    }

    public void deletePatient(String id) {
        patientRepository.deleteById(id);
        receptionService.deleteReceptionByPatient(id);

        try {
            String routingKey = "operation";
            OperationOut operation = getPatientOperation(id, "D");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
    }

    public Page<Patient> findPatients(String text, int page, int size) {
        return patientRepository.findByCardIdStartsWith(text, PageRequest.of(page, size));
    }

    public Optional<Patient> findById(String patientId) {
        return patientRepository.findById(patientId);
    }

    public Iterable<Patient> findByIds(List<String> ids) {
        return patientRepository.findAllById(ids);
    }
}
