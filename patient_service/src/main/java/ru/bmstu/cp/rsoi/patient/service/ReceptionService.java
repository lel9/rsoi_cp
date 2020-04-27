package ru.bmstu.cp.rsoi.patient.service;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.patient.domain.Patient;
import ru.bmstu.cp.rsoi.patient.domain.Reception;
import ru.bmstu.cp.rsoi.patient.domain.State;
import ru.bmstu.cp.rsoi.patient.exception.NoSuchPatientException;
import ru.bmstu.cp.rsoi.patient.model.ReceptionIn;
import ru.bmstu.cp.rsoi.patient.repository.PatientRepository;
import ru.bmstu.cp.rsoi.patient.repository.ReceptionRepository;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Service
public class ReceptionService {
    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private PatientService patientService;

    public List<Reception> findByPatient(String id) {
        return receptionRepository.findByPatient(id,
                new Sort(Sort.Direction.ASC, "date"));
    }

    public void deleteReceptionByPatient(String id) {
        receptionRepository.deleteReceptionByPatient(id);
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

            state.setYears(endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR));
            state.setMonths(endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH));
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
}
