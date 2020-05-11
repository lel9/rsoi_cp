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
import ru.bmstu.cp.rsoi.patient.model.reception.ReceptionIn;
import ru.bmstu.cp.rsoi.patient.model.reception.ReceptionOut;
import ru.bmstu.cp.rsoi.patient.model.reception.Receptions;
import ru.bmstu.cp.rsoi.patient.service.ReceptionService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/1.0/rsoi/patient")
@Api(value = "Patient service")
public class ReceptionController {

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private ModelMapper modelMapper;

    @Secured({"ROLE_USER", "ROLE_OPERATOR", "ROLE_EXPERT", "ROLE_ADMIN", "ROLE_INTERNAL_CLIENT"})
    @GetMapping("/{pid}/reception")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get receptions by id", response = Receptions.class)
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public Receptions getReceptions(@PathVariable String pid) {
        List<Reception> all = receptionService.findByPatientOrdered(pid);

        List<ReceptionOut> list = new ArrayList<>();
        all.forEach(r -> {
            ReceptionOut map = modelMapper.map(r, ReceptionOut.class);
            list.add(map);
        });

        return new Receptions(list);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_EXPERT", "ROLE_ADMIN"})
    @GetMapping("/{pid}/reception/last")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get last reception")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ReceptionOut getLastReception(@PathVariable String pid) {
        Reception lastReception = receptionService.getLastReception(pid);
        return modelMapper.map(lastReception, ReceptionOut.class);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_EXPERT", "ROLE_ADMIN"})
    @PostMapping("/{pid}/reception")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add reception")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public String postReception(@PathVariable String pid, @RequestBody @Valid ReceptionIn receptionIn) {
        Reception map = modelMapper.map(receptionIn, Reception.class);
        return receptionService.postReception(pid, map);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_EXPERT", "ROLE_ADMIN"})
    @PutMapping("/{pid}/reception/{rid}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update reception")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void putReception(@PathVariable String pid, @RequestBody @Valid ReceptionIn receptionIn, @PathVariable String rid) {
        Reception map = modelMapper.map(receptionIn, Reception.class);
        receptionService.putReception(pid, map, rid);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    @DeleteMapping("/{pid}/reception/{rid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete reception")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void deleteReception(@PathVariable String pid, @PathVariable String rid) {
        receptionService.deleteReception(pid, rid);
    }
}
