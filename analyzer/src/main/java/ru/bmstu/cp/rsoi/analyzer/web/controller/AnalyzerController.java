package ru.bmstu.cp.rsoi.analyzer.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.cp.rsoi.analyzer.model.ListSearchResultsOut;
import ru.bmstu.cp.rsoi.analyzer.model.ReceptionIn;
import ru.bmstu.cp.rsoi.analyzer.service.AnalyzerService;

import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@Api(value = "Analyzer service")
@RequestMapping("/api/1.0")
public class AnalyzerController {

    @Autowired
    private AnalyzerService analyzerService;

    @PostMapping("/public/analyzer")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Search drug")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ListSearchResultsOut searchDrug(@RequestBody @Valid ReceptionIn receptionIn) throws URISyntaxException {
        return analyzerService.searchDrugs(receptionIn);
    }
}
