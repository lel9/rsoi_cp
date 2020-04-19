package ru.bmstu.cp.rsoi.patient.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.cp.rsoi.patient.model.*;
import ru.bmstu.cp.rsoi.patient.service.PatientService;

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

    @PostMapping("/protected/patient/")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add patient")
    public String postPatient(@RequestBody PatientIn patient) {
        return patientService.postPatient(patient);
    }

    @PutMapping("/protected/patient/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update patient")
    public String putPatient(@RequestBody PatientIn patient, @PathVariable String id) {
        return patientService.putPatient(patient, id);
    }

    @DeleteMapping("/protected/patient/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete patient")
    public void deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
    }

    @PostMapping("/protected/patient/reception")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add reception")
    public String postReception(@RequestBody ReceptionInPost receptionInPost) {
        return patientService.postReception(receptionInPost);
    }

    @PutMapping("/protected/patient/reception/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update reception")
    public String postReception(@RequestBody ReceptionInPut receptionInPut, @PathVariable String id) {
        return patientService.putReception(receptionInPut, id);
    }

    @DeleteMapping("/protected/patient/reception/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete reception")
    public void deleteReception(@PathVariable String id) {
        patientService.deleteReception(id);
    }
}
