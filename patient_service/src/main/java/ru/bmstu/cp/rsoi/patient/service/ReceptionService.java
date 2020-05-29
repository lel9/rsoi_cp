package ru.bmstu.cp.rsoi.patient.service;

import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.patient.domain.Patient;
import ru.bmstu.cp.rsoi.patient.domain.Reception;
import ru.bmstu.cp.rsoi.patient.domain.State;
import ru.bmstu.cp.rsoi.patient.exception.InvalidReceptionDateException;
import ru.bmstu.cp.rsoi.patient.exception.NoSuchPatientException;
import ru.bmstu.cp.rsoi.patient.model.OperationOut;
import ru.bmstu.cp.rsoi.patient.repository.ReceptionRepository;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ru.bmstu.cp.rsoi.patient.model.OperationOut.getReceptionOperation;

@Service
public class ReceptionService {
    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger log = Logger.getLogger(ReceptionService.class.getName());

    public List<Reception> findByPatient(String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (Exception e) {
            return Collections.emptyList();
        }

        return receptionRepository.findByPatient(objectId);
    }

    public List<Reception> findByPatientOrdered(String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (Exception e) {
            return Collections.emptyList();
        }

        return receptionRepository.findByPatient(objectId,
                new Sort(Sort.Direction.ASC, "date"));
    }

    public void deleteReceptionByPatient(String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (Exception ignored) {
            return;
        }

        List<Reception> receptions = receptionRepository.deleteReceptionByPatient(objectId);
        receptions.forEach(reception -> {
            try {
                String routingKey = "operation";
                OperationOut operation = getReceptionOperation(reception.getId(), id, "D");
                rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
                log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
            } catch (Exception ex) {
                log.log(Level.SEVERE, ex.getMessage());
            }
        });
    }

    public String postReception(String patientId, Reception in) throws ParseException {
        String id = saveReception(patientId, in, null);
        try {
            String routingKey = "operation";
            OperationOut operation = getReceptionOperation(id, patientId, "C");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
        return id;
    }

    public void putReception(String patientId, Reception in, String id) throws ParseException {
        id = saveReception(patientId, in, id);
        try {
            String routingKey = "operation";
            OperationOut operation = getReceptionOperation(id, patientId, "U");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
    }

    public void deleteReception(String pid, String rid) {
        receptionRepository.deleteById(rid);
        try {
            String routingKey = "operation";
            OperationOut operation = getReceptionOperation(rid, pid, "D");
            rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
            log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage());
        }
    }

    private String saveReception(String patientId, Reception reception, String id) throws ParseException {
        Optional<Patient> patientOptional = patientService.findById(patientId);
        if (!patientOptional.isPresent())
            throw new NoSuchPatientException();

        Patient patient = patientOptional.get();
        reception.setState(getStateAccordingToPatient(reception, patient));
        reception.setPatient(patient);
        reception.setId(id);
        Reception save = receptionRepository.save(reception);
        return save.getId();
    }

    private State getStateAccordingToPatient(final Reception reception, final Patient patient) throws ParseException {
        State state = reception.getState();
        if (state == null) {
            state = new State();
        }

        state.setSex(patient.getSex());

        OffsetDateTime startCalendar = parseDate(patient.getBirthday());
        OffsetDateTime endCalendar = parseDate(reception.getDate());

        if (startCalendar != null && endCalendar != null) {
            if (endCalendar.isBefore(startCalendar))
                throw new InvalidReceptionDateException();

            int diffYears = endCalendar.getYear() - startCalendar.getYear();
            int diffMonths = diffYears * 12 + endCalendar.getMonthValue() - startCalendar.getMonthValue();

            state.setYears(diffMonths / 12);
            state.setMonths(diffMonths % 12);
        }

        return state;
    }

    public void updateReceptions(Patient patient) throws ParseException {
        List<Reception> receptions = this.findByPatient(patient.getId());
        for (Reception reception: receptions) {
            reception.setState(getStateAccordingToPatient(reception, patient));
            receptionRepository.save(reception);

            try {
                String routingKey = "operation";
                OperationOut operation = getReceptionOperation(reception.getId(), patient.getId(), "U");
                rabbitTemplate.convertAndSend("operationExchange", routingKey, operation);
                log.log(Level.INFO, "Operation was sent to RabbitMQ: " + operation);
            } catch (Exception ex) {
                log.log(Level.SEVERE, ex.getMessage());
            }
        }
    }

    public List<Reception> searchReceptions(Character sex,
                                            Integer years,
                                            Integer months,
                                            String lifeAnamnesis,
                                            String diseaseAnamnesis,
                                            String plaints,
                                            String objectiveInspection,
                                            String examinationsResults,
                                            String specialistsConclusions,
                                            String diagnosisText,
                                            String dateStart,
                                            String dateEnd,
                                            String patientId,
                                            List<String> drugId) {

        List<Reception> allByDiagnosisText = (diagnosisText != null && !diagnosisText.isEmpty()) ?
                                                    receptionRepository.findAllByDiagnosisText(diagnosisText) :
                                                    receptionRepository.findAll();
        OffsetDateTime start = parseDate(dateStart);
        OffsetDateTime end = parseDate(dateEnd);
        return allByDiagnosisText
                .stream()
                .filter(r -> {
                    if (patientId != null) {
                        if (r.getPatient() != null) {
                            return r.getPatient().getId().equals(patientId);
                        } else
                            return false;
                    } else
                        return true;})
                .filter(r -> {
                    OffsetDateTime date = parseDate(r.getDate());
                    return date != null;
                })
                .filter(r -> {
                    if (start != null) {
                        OffsetDateTime date = parseDate(r.getDate());
                        return start.isBefore(date) || start.isEqual(date);
                    } else
                        return true; })
                .filter(r -> {
                    if (end != null) {
                        OffsetDateTime date = parseDate(r.getDate());
                        return date.isBefore(end) || end.isEqual(date);
                    } else
                        return true; })
                .filter(r -> {
                    if (drugId != null && !drugId.isEmpty()) {
                        for (String di : drugId) {
                            if (r.getDrugs().stream().noneMatch(d -> d.getId().equals(di))) {
                                return false;
                            }
                        }
                        return true;
                    } else
                        return true;
                })
                .collect(Collectors.toList());

    }

    private OffsetDateTime parseDate(String date) {
        if (date == null || date.isEmpty())
            return null;
        return OffsetDateTime.parse(date);
    }
}
