package ru.bmstu.cp.rsoi.recommendation.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/1.0/rsoi/recommendation")
@Api(value = "Recommendation service")
public class RecommendationController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping(path = "/", params = { "page", "size" })
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

        return new PageRecommendationOut(totalPages, totalElements, page, size, resultPage.getContent());
    }

    @GetMapping(path = "/count")
    public RecommendationsCount getCount(@RequestParam String drugId) {
        return new RecommendationsCount(recommendationService.getCountByDrugId(drugId));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add recommendation")
    public String postDrug(@RequestBody @Valid RecommendationIn recommendation) throws URISyntaxException {
        return recommendationService.postRecommendation(recommendation);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update recommendation")
    public void putDrug(@PathVariable String id, @RequestBody @Valid RecommendationIn recommendation) throws URISyntaxException {
        recommendationService.putRecommendation(id, recommendation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete recommendation")
    public void deleteRecommendation(@PathVariable String id) {
        recommendationService.deleteRecommendation(id);
    }

}
