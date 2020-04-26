package ru.bmstu.cp.rsoi.patient.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.cp.rsoi.patient.model.ReceptionIn;
import ru.bmstu.cp.rsoi.patient.service.ReceptionService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0/rsoi")
@Api(value = "Patient service")
public class ReceptionController {

    @Autowired
    private ReceptionService receptionService;

    @PostMapping("/protected/patient/{pid}/reception")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add reception")
    public String postReception(@PathVariable String pid, @RequestBody @Valid ReceptionIn receptionIn) {
        return receptionService.postReception(pid, receptionIn);
    }

    @PutMapping("/protected/patient/{pid}/reception/{rid}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update reception")
    public String postReception(@PathVariable String pid, @RequestBody @Valid ReceptionIn receptionIn, @PathVariable String rid) {
        return receptionService.putReception(pid, receptionIn, rid);
    }

    @DeleteMapping("/protected/patient/{pid}/reception/{rid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete reception")
    public void deleteReception(@PathVariable String pid, @PathVariable String rid) {
        receptionService.deleteReception(pid, rid);
    }
}
