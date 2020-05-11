package ru.bmstu.cp.rsoi.patient.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmstu.cp.rsoi.patient.domain.Patient;
import ru.bmstu.cp.rsoi.patient.domain.Reception;
import ru.bmstu.cp.rsoi.patient.model.patient.PagePatientOut;
import ru.bmstu.cp.rsoi.patient.model.patient.PatientIn;
import ru.bmstu.cp.rsoi.patient.model.patient.PatientOut;
import ru.bmstu.cp.rsoi.patient.model.patient.PatientWithReceptionsOut;
import ru.bmstu.cp.rsoi.patient.model.reception.ReceptionOut;
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
@RequestMapping("/api/1.0/rsoi/patient")
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

    @Secured({"ROLE_OPERATOR", "ROLE_EXPERT", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get patient by id", response = PatientWithReceptionsOut.class)
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public PatientWithReceptionsOut getPatient(@PathVariable String id) {
        Patient patient = patientService.getPatient(id);
        List<Reception> all = receptionService.findByPatient(id);

        List<ReceptionOut> reception = new ArrayList<>();
        all.forEach(r -> {
            ReceptionOut map = modelMapper.map(r, ReceptionOut.class);
            reception.add(map);
        });

        PatientWithReceptionsOut patientOut = modelMapper.map(patient, PatientWithReceptionsOut.class);
        patientOut.setReceptions(reception);

        return patientOut;
    }

    @Secured({"ROLE_OPERATOR", "ROLE_EXPERT", "ROLE_ADMIN"})
    @GetMapping(path = "/byCardId", params = { "page", "size" })
    @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public PagePatientOut findPatient(@RequestParam(defaultValue = "", required = false) String cardId,
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

        int totalPages = resultPage.getTotalPages();
        long totalElements = resultPage.getTotalElements();
        int number = resultPage.getNumber();
        int pageSize = resultPage.getSize();
        List<PatientOut> results = resultPage.getContent()
                .stream()
                .map(patient -> modelMapper.map(patient, PatientOut.class))
                .collect(Collectors.toList());

        return new PagePatientOut(totalPages, totalElements, page, pageSize, results);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add patient")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public String postPatient(@RequestBody @Valid PatientIn patient) {
        Patient map = modelMapper.map(patient, Patient.class);
        return patientService.postPatient(map);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update patient")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public String putPatient(@RequestBody @Valid PatientIn patient, @PathVariable String id) {
        Patient map = modelMapper.map(patient, Patient.class);
        return patientService.putPatient(map, id);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete patient")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
    }

}
