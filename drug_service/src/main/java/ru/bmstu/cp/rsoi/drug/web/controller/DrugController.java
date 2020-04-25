package ru.bmstu.cp.rsoi.drug.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.cp.rsoi.drug.model.DrugIn;
import ru.bmstu.cp.rsoi.drug.model.DrugOut;
import ru.bmstu.cp.rsoi.drug.model.ListDrugOutShort;
import ru.bmstu.cp.rsoi.drug.service.DrugService;

@RestController
@RequestMapping("/api/1.0/rsoi")
@Api(value = "Drug service")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @GetMapping("/protected/drug/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get drug by id", response = DrugOut.class)
    public DrugOut getDrug(@PathVariable String id) {
        return drugService.getDrug(id);
    }

    @GetMapping("/protected/drug/find/byname")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Find drug by trade name", response = ListDrugOutShort.class)
    public ListDrugOutShort findDrug(@RequestParam String text) {
        return drugService.findDrug(text);
    }

    @PostMapping("/protected/drug/")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add drug")
    public String postDrug(@RequestBody DrugIn drug) {
        return drugService.postDrug(drug);
    }

    @PutMapping("/protected/drug/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update drug")
    public void putDrug(@PathVariable String id, @RequestBody DrugIn drug) {
        drugService.putDrug(drug, id);
    }

}
