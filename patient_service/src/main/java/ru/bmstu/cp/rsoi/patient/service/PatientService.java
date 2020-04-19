package ru.bmstu.cp.rsoi.patient.service;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.patient.domain.*;
import ru.bmstu.cp.rsoi.patient.exception.NoSuchPatientException;
import ru.bmstu.cp.rsoi.patient.exception.NoSuchReceptionException;
import ru.bmstu.cp.rsoi.patient.exception.PatientAlreadyExistsException;
import ru.bmstu.cp.rsoi.patient.model.*;
import ru.bmstu.cp.rsoi.patient.repository.PatientRepository;
import ru.bmstu.cp.rsoi.patient.repository.ReceptionRepository;

import java.util.*;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ReceptionRepository receptionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    public PatientOut getPatient(String id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent())
            throw new NoSuchPatientException();

        PatientOut patientOut = modelMapper.map(patientOptional.get(), PatientOut.class);

        List<Reception> all = receptionRepository.findByPatient(
                new ObjectId(id),
                new Sort(Sort.Direction.ASC, "date"));

        List<ReceptionOut> reception = new ArrayList<>();
        all.forEach(r -> {
            ReceptionOut map = modelMapper.map(r, ReceptionOut.class);
            reception.add(map);
        });

        patientOut.setReceptions(reception);

        return patientOut;
    }

    public String postPatient(PatientIn in) {
        String cardId = in.getCardId();
        if (cardId != null && patientRepository.findByCardId(cardId).isPresent())
            throw new PatientAlreadyExistsException(cardId);

        Patient patient = modelMapper.map(in, Patient.class);
        patient.setId(null);
        Patient save = patientRepository.save(patient);
        return save.getId();
    }

    public String putPatient(PatientIn in, String id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent())
            throw new NoSuchPatientException();

        String cardId = in.getCardId();
        if (cardId != null) {
            Optional<Patient> byCardId = patientRepository.findByCardId(cardId);
            if (byCardId.isPresent() && !byCardId.get().getId().equals(id))
                throw new PatientAlreadyExistsException(cardId);
        }

        Patient newPatient = modelMapper.map(in, Patient.class);
        newPatient.setId(patientOptional.get().getId());
        Patient save = patientRepository.save(newPatient);
        return save.getId();
    }


    public void deletePatient(String id) {
        Optional<Patient> byId = patientRepository.findById(id);
        if (!byId.isPresent())
            throw new NoSuchPatientException();

        patientRepository.deleteById(id);
        receptionRepository.deleteReceptionByPatient(new ObjectId(id));
    }

    public String postReception(ReceptionInPost in) {
        Optional<Patient> patientOptional = patientRepository.findById(in.getPatientId());
        if (!patientOptional.isPresent())
            throw new NoSuchPatientException();

        Patient patient = patientOptional.get();
        Reception reception = modelMapper.map(in, Reception.class);

        State state = reception.getState();
        if (state != null) {
            state.setSex(patient.getSex());

            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTimeInMillis(patient.getBirthday());
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTimeInMillis(reception.getDate());

            state.setYears(endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR));
            state.setMonths(endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH));
        }

        reception.setPatient(patient);

        reception.setId(null);
        Reception save = receptionRepository.save(reception);
        return save.getId();
    }

    public String putReception(ReceptionInPut in, String id) {
        Optional<Reception> byId = receptionRepository.findById(id);
        if (!byId.isPresent())
            throw new NoSuchReceptionException();

        Reception reception = byId.get();
        Reception newReception = modelMapper.map(in, Reception.class);

        State newState = newReception.getState();
        State state = reception.getState();
        if (newState != null && state != null) {
            newState.setSex(state.getSex());
            newState.setYears(state.getYears());
            newState.setMonths(state.getMonths());
        }
        newReception.setPatient(reception.getPatient());
        newReception.setId(reception.getId());
        Reception save = receptionRepository.save(newReception);
        return save.getId();
    }

    public void deleteReception(String id) {
        if (!receptionRepository.findById(id).isPresent())
            throw new NoSuchReceptionException();
        receptionRepository.deleteById(id);
    }

    public ListPatientOutShort findPatient(String text) {
        List<Patient> patients = patientRepository.findByCardIdStartsWith(text);
        List<PatientOutShort> res = new ArrayList<>();

        patients.forEach(patient -> res.add(modelMapper.map(patient, PatientOutShort.class)));

        ListPatientOutShort list = new ListPatientOutShort();
        list.setPatients(res);

        return list;
    }
}
