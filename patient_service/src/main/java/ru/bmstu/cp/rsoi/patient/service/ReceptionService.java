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
import java.text.SimpleDateFormat;
import java.util.*;
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

        Calendar startCalendar = parseDate(patient.getBirthday());
        Calendar endCalendar = parseDate(reception.getDate());

        if (startCalendar != null && endCalendar != null) {
            if (endCalendar.before(startCalendar))
                throw new InvalidReceptionDateException();

            int diffYears = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonths = diffYears * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

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
                                            List<String> drugId) throws ParseException {

        List<Reception> allByDiagnosisText = (diagnosisText != null && !diagnosisText.isEmpty()) ?
                                                    receptionRepository.findAllByDiagnosisText(diagnosisText) :
                                                    receptionRepository.findAll();
        Calendar start = parseDate(dateStart);
        Calendar end = parseDate(dateEnd);
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
                    try {
                        Calendar date = parseDate(r.getDate());
                        return date != null;
                    } catch (ParseException e) {
                        return false;
                    }})
                .filter(r -> {
                    if (start != null) {
                        try {
                            return !parseDate(r.getDate()).after(start);
                        } catch (ParseException e) {
                            return false;
                        }
                    } else
                        return true; })
                .filter(r -> {
                    if (end != null) {
                        try {
                            return !end.before(parseDate(r.getDate()));
                        } catch (ParseException e) {
                            return false;
                        }
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

    private Calendar parseDate(String date) throws ParseException {
        if (date == null || date.isEmpty())
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX");
        Date parsed = sdf.parse(date);
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(parsed);
        return startCalendar;
    }
}
