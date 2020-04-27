package ru.bmstu.cp.rsoi.drug.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmstu.cp.rsoi.drug.domain.Drug;
import ru.bmstu.cp.rsoi.drug.model.DrugIn;
import ru.bmstu.cp.rsoi.drug.model.DrugOut;
import ru.bmstu.cp.rsoi.drug.model.DrugOutShort;
import ru.bmstu.cp.rsoi.drug.service.DrugService;
import ru.bmstu.cp.rsoi.drug.web.event.PaginatedResultsRetrievedEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/1.0/rsoi")
@Api(value = "Drug service")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/protected/drug/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get drug by id", response = DrugOut.class)
    public DrugOut getDrug(@PathVariable String id) {
        return modelMapper.map(drugService.getDrug(id), DrugOut.class);
    }

    @GetMapping("/private/drug/{id}/analogs")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get analogs")
    public List<DrugOutShort> getDrugAnalogs(@PathVariable String id) {
        List<Drug> drugAnalogs = drugService.getDrugAnalogs(id);
        return drugAnalogs
                .stream()
                .map(drug -> modelMapper.map(drug, DrugOutShort.class))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/protected/drug", params = { "page", "size" })
    @ResponseStatus(HttpStatus.OK)
    public List<DrugOutShort> findDrug(@RequestParam(defaultValue = "", required = false) String text,
                                       @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                       @RequestParam(value = "size", defaultValue = "15", required = false) int size,
                                       UriComponentsBuilder uriBuilder,
                                       HttpServletResponse response,
                                       HttpServletRequest request) {
        Page<Drug> resultPage = drugService.findDrugs(text, page, size);

        uriBuilder.path(request.getRequestURI());
        uriBuilder.queryParam("text", text);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                Drug.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return resultPage.getContent()
                .stream()
                .map(drug -> modelMapper.map(drug, DrugOutShort.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/protected/drug")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add drug")
    public String postDrug(@RequestBody @Valid DrugIn drugIn) {
        Drug drug = modelMapper.map(drugIn, Drug.class);
        return drugService.postDrug(drug);
    }

    @PutMapping("/protected/drug/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update drug")
    public void putDrug(@PathVariable String id, @RequestBody @Valid DrugIn drugIn) {
        Drug drug = modelMapper.map(drugIn, Drug.class);
        drugService.putDrug(drug, id);
    }

}
