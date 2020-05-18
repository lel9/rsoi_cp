package ru.bmstu.cp.rsoi.recommendation.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmstu.cp.rsoi.recommendation.domain.Recommendation;
import ru.bmstu.cp.rsoi.recommendation.model.PageRecommendationOut;
import ru.bmstu.cp.rsoi.recommendation.model.RecommendationIn;
import ru.bmstu.cp.rsoi.recommendation.model.RecommendationsCount;
import ru.bmstu.cp.rsoi.recommendation.service.RecommendationService;
import ru.bmstu.cp.rsoi.recommendation.web.event.PaginatedResultsRetrievedEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/1.0")
@Api(value = "Recommendation service")
public class RecommendationController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping(path = "/public/recommendation", params = { "page", "size" })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public PageRecommendationOut findPaginated(@RequestParam String drugId,
                                               @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                               @RequestParam(value = "size", required = false, defaultValue = "15") int size,
                                               UriComponentsBuilder uriBuilder,
                                               HttpServletResponse response,
                                               HttpServletRequest request) {
        Page<Recommendation> resultPage = recommendationService.getRecommendations(drugId, page, size);

        uriBuilder.path(request.getRequestURI());
        uriBuilder.queryParam("drugId", drugId);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                Recommendation.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        long totalElements = resultPage.getTotalElements();
        int totalPages = resultPage.getTotalPages();
        int number = resultPage.getNumber();
        int pageSize = resultPage.getSize();

        return new PageRecommendationOut(totalPages, totalElements, number, pageSize, resultPage.getContent());
    }

    @GetMapping(path = "/public/recommendation/count")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public RecommendationsCount getCount(@RequestParam String drugId) {
        return new RecommendationsCount(recommendationService.getCountByDrugId(drugId));
    }

    @Secured({"ROLE_EXPERT", "ROLE_ADMIN"})
    @PostMapping("/protected/recommendation")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add recommendation")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public String postDrug(@RequestBody @Valid RecommendationIn recommendation) throws URISyntaxException {
        return recommendationService.postRecommendation(recommendation);
    }

    @Secured({"ROLE_EXPERT", "ROLE_ADMIN"})
    @PutMapping("/protected/recommendation/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update recommendation")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void putDrug(@PathVariable String id, @RequestBody @Valid RecommendationIn recommendation) throws URISyntaxException {
        recommendationService.putRecommendation(id, recommendation);
    }

    @Secured({"ROLE_EXPERT", "ROLE_ADMIN"})
    @DeleteMapping("/protected/recommendation/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete recommendation")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public void deleteRecommendation(@PathVariable String id) {
        recommendationService.deleteRecommendation(id);
    }

}
