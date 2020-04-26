package ru.bmstu.cp.rsoi.drug.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmstu.cp.rsoi.drug.domain.Drug;
import ru.bmstu.cp.rsoi.drug.exception.DrugsNotFoundException;
import ru.bmstu.cp.rsoi.drug.model.DrugIn;
import ru.bmstu.cp.rsoi.drug.model.ListDrugOutShort;
import ru.bmstu.cp.rsoi.drug.service.DrugService;
import ru.bmstu.cp.rsoi.drug.web.event.PaginatedResultsRetrievedEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/1.0/rsoi")
@Api(value = "Drug service")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping(path = "/protected/drug", params = { "page", "size" })
    public List<Drug> findPaginated(@RequestParam("page") int page,
                                    @RequestParam("size") int size,
                                    UriComponentsBuilder uriBuilder,
                                    HttpServletResponse response,
                                    HttpServletRequest request) {
        Page<Drug> resultPage = drugService.getDrugs(page, size);
        if (page > resultPage.getTotalPages()) {
            throw new DrugsNotFoundException();
        }

        uriBuilder.path(request.getRequestURI());
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                Drug.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return resultPage.getContent();
    }

    @GetMapping("/protected/drug/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get drug by id", response = Drug.class)
    public Drug getDrug(@PathVariable String id) {
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
    public String postDrug(@RequestBody @Valid DrugIn drug) {
        return drugService.postDrug(drug);
    }

    @PutMapping("/protected/drug/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update drug")
    public void putDrug(@PathVariable String id, @RequestBody @Valid DrugIn drug) {
        drugService.putDrug(drug, id);
    }

}
