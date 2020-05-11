package ru.bmstu.cp.rsoi.patient.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.patient.domain.Patient;
import ru.bmstu.cp.rsoi.patient.domain.Reception;
import ru.bmstu.cp.rsoi.patient.domain.State;
import ru.bmstu.cp.rsoi.patient.exception.InvalidReceptionDateException;
import ru.bmstu.cp.rsoi.patient.exception.NoSuchPatientException;
import ru.bmstu.cp.rsoi.patient.repository.ReceptionRepository;

import java.util.*;

@Service
public class ReceptionService {
    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private PatientService patientService;

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

        receptionRepository.deleteReceptionByPatient(objectId);
    }

    public String postReception(String patientId, Reception in) {
        return saveReception(patientId, in, null);
    }

    public void putReception(String patientId, Reception in, String id) {
        saveReception(patientId, in, id);
    }

    public void deleteReception(String pid, String rid) {
        receptionRepository.deleteById(rid);
    }

    private String saveReception(String patientId, Reception reception, String id) {
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

    private State getStateAccordingToPatient(final Reception reception, final Patient patient) {
        State state = reception.getState();
        if (state == null) {
            state = new State();
        }

        state.setSex(patient.getSex());

        if (patient.getBirthday() != null && reception.getDate() != null) {
            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTimeInMillis(patient.getBirthday());
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTimeInMillis(reception.getDate());

            int diffYears = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonths = diffYears * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

            if (diffMonths < 0)
                throw new InvalidReceptionDateException();

            state.setYears(diffMonths / 12);
            state.setMonths(diffMonths % 12);
        }

        return state;
    }

    public void updateReceptions(Patient patient) {
        List<Reception> receptions = this.findByPatient(patient.getId());
        for (Reception reception: receptions) {
            reception.setState(getStateAccordingToPatient(reception, patient));
            receptionRepository.save(reception);
        }
    }

    public Reception getLastReception(String pid) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(pid);
        } catch (Exception ignored) {
            return null;
        }

        List<Reception> receptions = receptionRepository.findByPatient(objectId, new Sort(Sort.Direction.DESC, "date"));
        if (receptions != null && !receptions.isEmpty())
            return receptions.get(0);
        return null;

    }
}
