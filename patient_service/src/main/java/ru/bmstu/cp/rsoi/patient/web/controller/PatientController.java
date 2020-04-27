package ru.bmstu.cp.rsoi.patient.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmstu.cp.rsoi.patient.domain.Patient;
import ru.bmstu.cp.rsoi.patient.domain.Reception;
import ru.bmstu.cp.rsoi.patient.model.*;
import ru.bmstu.cp.rsoi.patient.service.PatientService;
import ru.bmstu.cp.rsoi.patient.service.ReceptionService;
import ru.bmstu.cp.rsoi.patient.web.event.PaginatedResultsRetrievedEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/1.0/rsoi")
@Api(value = "Patient service")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/protected/patient/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get patient by id", response = PatientOut.class)
    public PatientOut getDrug(@PathVariable String id) {
        Patient patient = patientService.getPatient(id);
        List<Reception> all = receptionService.findByPatient(id);

        List<ReceptionOut> reception = new ArrayList<>();
        all.forEach(r -> {
            ReceptionOut map = modelMapper.map(r, ReceptionOut.class);
            reception.add(map);
        });

        PatientOut patientOut = modelMapper.map(patient, PatientOut.class);
        patientOut.setReceptions(reception);

        return patientOut;
    }

    @GetMapping(path = "/protected/patient/byCardId", params = { "page", "size" })
    @ResponseStatus(HttpStatus.OK)
    public List<PatientOutShort> findPatient(@RequestParam(defaultValue = "", required = false) String cardId,
                                             @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                             @RequestParam(value = "size", defaultValue = "15", required = false) int size,
                                             UriComponentsBuilder uriBuilder,
                                             HttpServletResponse response,
                                             HttpServletRequest request) {

        Page<Patient> resultPage = patientService.findPatients(cardId, page, size);

        uriBuilder.path(request.getRequestURI());
        uriBuilder.queryParam("cardId", cardId);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                Patient.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return resultPage.getContent()
                .stream()
                .map(patient -> modelMapper.map(patient, PatientOutShort.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/protected/patient")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add patient")
    public String postPatient(@RequestBody @Valid PatientIn patient) {
        Patient map = modelMapper.map(patient, Patient.class);
        return patientService.postPatient(map);
    }

    @PutMapping("/protected/patient/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update patient")
    public String putPatient(@RequestBody @Valid PatientIn patient, @PathVariable String id) {
        Patient map = modelMapper.map(patient, Patient.class);
        return patientService.putPatient(map, id);
    }

    @DeleteMapping("/protected/patient/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete patient")
    public void deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
    }

}
