package ru.bmstu.cp.rsoi.patient.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.cp.rsoi.patient.domain.Reception;
import ru.bmstu.cp.rsoi.patient.model.reception.*;
import ru.bmstu.cp.rsoi.patient.service.ReceptionService;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/1.0")
@Api(value = "Patient service")
public class ReceptionController {

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/public/patient/{pid}/reception")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get receptions by id", response = ListReceptionOut.class)
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ListReceptionOut getReceptions(@PathVariable String pid) {
        List<Reception> all = receptionService.findByPatientOrdered(pid);

        List<ReceptionOut> list = new ArrayList<>();
        all.forEach(r -> {
            ReceptionOut map = modelMapper.map(r, ReceptionOut.class);
            list.add(map);
        });

        return new ListReceptionOut(list);
    }

    @GetMapping("/private/reception/search")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get by all parameters", response = ListReceptionWithPatientOut.class)
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ListReceptionWithPatientOut getReceptions(@RequestParam(required = false) Character sex,
                                                     @RequestParam(required = false) Integer years,
                                                     @RequestParam(required = false) Integer months,
                                                     @RequestParam(required = false) String lifeAnamnesis,
                                                     @RequestParam(required = false) String diseaseAnamnesis,
                                                     @RequestParam(required = false) String plaints,
                                                     @RequestParam(required = false) String objectiveInspection,
                                                     @RequestParam(required = false) String examinationsResults,
                                                     @RequestParam(required = false) String specialistsConclusions,
                                                     @RequestParam(required = false) String diagnosisText,
                                                     @RequestParam(required = false) String dateStart,
                                                     @RequestParam(required = false) String dateEnd,
                                                     @RequestParam(required = false) String patientId,
                                                     @RequestParam(required = false) List<String> drugId) throws ParseException {

        List<Reception> all = receptionService.searchReceptions(sex, years, months,
                lifeAnamnesis, diseaseAnamnesis, plaints, objectiveInspection, examinationsResults,
                specialistsConclusions, diagnosisText, dateStart, dateEnd, patientId, drugId);

        List<ReceptionWithPatientOut> list = new ArrayList<>();
        all.forEach(r -> {
            ReceptionWithPatientOut map = modelMapper.map(r, ReceptionWithPatientOut.class);
            list.add(map);
        });

        return new ListReceptionWithPatientOut(list);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_EXPERT", "ROLE_ADMIN"})
    @PostMapping("/protected/patient/{pid}/reception")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add reception")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public String postReception(@PathVariable String pid, @RequestBody @Valid ReceptionIn receptionIn) throws ParseException {
        Reception map = modelMapper.map(receptionIn, Reception.class);
        return receptionService.postReception(pid, map);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_EXPERT", "ROLE_ADMIN"})
    @PutMapping("/protected/patient/{pid}/reception/{rid}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update reception")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void putReception(@PathVariable String pid, @RequestBody @Valid ReceptionIn receptionIn, @PathVariable String rid) throws ParseException {
        Reception map = modelMapper.map(receptionIn, Reception.class);
        receptionService.putReception(pid, map, rid);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    @DeleteMapping("/protected/patient/{pid}/reception/{rid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete reception")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void deleteReception(@PathVariable String pid, @PathVariable String rid) {
        receptionService.deleteReception(pid, rid);
    }
}
