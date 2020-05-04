package ru.bmstu.cp.rsoi.patient.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.cp.rsoi.patient.domain.Reception;
import ru.bmstu.cp.rsoi.patient.model.reception.ReceptionIn;
import ru.bmstu.cp.rsoi.patient.model.reception.ReceptionOut;
import ru.bmstu.cp.rsoi.patient.service.ReceptionService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/rsoi/patient")
@Api(value = "Patient service")
public class ReceptionController {

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{pid}/reception/last")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get last reception")
    public ReceptionOut getLastReception(@PathVariable String pid) {
        Reception lastReception = receptionService.getLastReception(pid);
        return modelMapper.map(lastReception, ReceptionOut.class);
    }

    @PostMapping("/{pid}/reception")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add reception")
    public String postReception(@PathVariable String pid, @RequestBody @Valid ReceptionIn receptionIn) {
        Reception map = modelMapper.map(receptionIn, Reception.class);
        return receptionService.postReception(pid, map);
    }

    @PutMapping("/{pid}/reception/{rid}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update reception")
    public void putReception(@PathVariable String pid, @RequestBody @Valid ReceptionIn receptionIn, @PathVariable String rid) {
        Reception map = modelMapper.map(receptionIn, Reception.class);
        receptionService.putReception(pid, map, rid);
    }

    @DeleteMapping("/{pid}/reception/{rid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete reception")
    public void deleteReception(@PathVariable String pid, @PathVariable String rid) {
        receptionService.deleteReception(pid, rid);
    }
}
