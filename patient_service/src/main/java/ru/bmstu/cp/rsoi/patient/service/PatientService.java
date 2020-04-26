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
    private ReceptionService receptionService;

    @Autowired
    private ModelMapper modelMapper;

    public PatientOut getPatient(String id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent())
            throw new NoSuchPatientException();

        PatientOut patientOut = modelMapper.map(patientOptional.get(), PatientOut.class);

        List<Reception> all = receptionService.findByPatient(id);

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
        String cardId = in.getCardId();
        if (cardId != null) {
            Optional<Patient> byCardId = patientRepository.findByCardId(cardId);
            if (byCardId.isPresent() && !byCardId.get().getId().equals(id))
                throw new PatientAlreadyExistsException(cardId);
        }

        Patient newPatient = modelMapper.map(in, Patient.class);
        newPatient.setId(id);
        Patient save = patientRepository.save(newPatient);

        receptionService.updateReceptions(save);

        return save.getId();
    }

    public void deletePatient(String id) {
        patientRepository.deleteById(id);
        receptionService.deleteReceptionByPatient(id);
    }

    public ListPatientOutShort findPatient(String text) {
        List<Patient> patients = patientRepository.findByCardIdStartsWith(text);
        List<PatientOutShort> res = new ArrayList<>();

        patients.forEach(patient -> res.add(modelMapper.map(patient, PatientOutShort.class)));

        ListPatientOutShort list = new ListPatientOutShort();
        list.setPatients(res);

        return list;
    }

    public Optional<Patient> findById(String patientId) {
        return patientRepository.findById(patientId);
    }
}
