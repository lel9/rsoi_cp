package ru.bmstu.cp.rsoi.patient.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.cp.rsoi.patient.model.*;
import ru.bmstu.cp.rsoi.patient.service.PatientService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/rsoi")
@Api(value = "Patient service")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/protected/patient/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get patient by id", response = PatientOut.class)
    public PatientOut getDrug(@PathVariable String id) {
        return patientService.getPatient(id);
    }

    @GetMapping("/protected/patient/find/bycard")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find patient by card id", response = ListPatientOutShort.class)
    public ListPatientOutShort findPatient(@RequestParam String text) {
        return patientService.findPatient(text);
    }

    @PostMapping("/protected/patient")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add patient")
    public String postPatient(@RequestBody @Valid PatientIn patient) {
        return patientService.postPatient(patient);
    }

    @PutMapping("/protected/patient/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update patient")
    public String putPatient(@RequestBody @Valid PatientIn patient, @PathVariable String id) {
        return patientService.putPatient(patient, id);
    }

    @DeleteMapping("/protected/patient/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete patient")
    public void deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
    }

}
