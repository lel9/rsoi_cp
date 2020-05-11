package ru.bmstu.cp.rsoi.drug.web.controller;

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
import ru.bmstu.cp.rsoi.drug.domain.Drug;
import ru.bmstu.cp.rsoi.drug.model.*;
import ru.bmstu.cp.rsoi.drug.service.DrugService;
import ru.bmstu.cp.rsoi.drug.web.event.PaginatedResultsRetrievedEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(value = "Drug service")
@RequestMapping("/api/1.0/rsoi/drug")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get drug by id", response = DrugOut.class)
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public DrugOut getDrug(@PathVariable String id) {
        return modelMapper.map(drugService.getDrug(id), DrugOut.class);
    }

    @GetMapping("/{id}/analogs")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get analogs")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public List<DrugOutShort> getDrugAnalogs(@PathVariable String id) {
        List<Drug> drugAnalogs = drugService.getDrugAnalogs(id);
        return drugAnalogs
                .stream()
                .map(drug -> modelMapper.map(drug, DrugOutShort.class))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/", params = { "page", "size" })
    @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public PageDrugOut findDrug(@RequestParam(defaultValue = "", required = false) String text,
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

        int totalPages = resultPage.getTotalPages();
        long totalElements = resultPage.getTotalElements();
        int number = resultPage.getNumber();
        int pageSize = resultPage.getSize();
        List<DrugOutShort> result = resultPage.getContent()
                .stream()
                .map(drug -> modelMapper.map(drug, DrugOutShort.class))
                .collect(Collectors.toList());

        return new PageDrugOut(totalPages, totalElements, number, pageSize, result);

    }

    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add drug")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public String postDrug(@RequestBody @Valid DrugInPost drugIn) {
        Drug drug = modelMapper.map(drugIn, Drug.class);
        return drugService.postDrug(drug);
    }

    @Secured({"ROLE_OPERATOR", "ROLE_ADMIN"})
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update drug")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void patchDrug(@PathVariable String id, @RequestBody @Valid DrugInPatch drugIn) {
        Drug drug = modelMapper.map(drugIn, Drug.class);
        drugService.patchDrug(drug, id);
    }

}
