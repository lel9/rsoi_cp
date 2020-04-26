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
import ru.bmstu.cp.rsoi.recommendation.exception.RecommendationsNotFoundException;
import ru.bmstu.cp.rsoi.recommendation.model.RecommendationIn;
import ru.bmstu.cp.rsoi.recommendation.service.RecommendationService;
import ru.bmstu.cp.rsoi.recommendation.web.event.PaginatedResultsRetrievedEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/1.0/rsoi")
@Api(value = "Recommendation service")
public class RecommendationController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping(path = "/protected/recommendation", params = { "page", "size" })
    public List<Recommendation> findPaginated(@RequestParam String drugId,
                                              @RequestParam("page") int page,
                                              @RequestParam("size") int size,
                                              UriComponentsBuilder uriBuilder,
                                              HttpServletResponse response,
                                              HttpServletRequest request) {
        Page<Recommendation> resultPage = recommendationService.getRecommendations(drugId, page, size);
        if (page > resultPage.getTotalPages()) {
            throw new RecommendationsNotFoundException();
        }

        uriBuilder.path(request.getRequestURI());
        uriBuilder.queryParam("drugId", drugId);
        eventPublisher.publishEvent(new PaginatedResultsRetrievedEvent<>(
                Recommendation.class, uriBuilder, response, page, resultPage.getTotalPages(), size));

        return resultPage.getContent();
    }

    @PostMapping("/protected/recommendation")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add recommendation")
    public String postDrug(@RequestBody @Valid RecommendationIn recommendation) throws URISyntaxException {
        return recommendationService.postRecommendation(recommendation);
    }

    @PutMapping("/protected/recommendation/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update recommendation")
    public void putDrug(@PathVariable String id, @RequestBody @Valid RecommendationIn recommendation) throws URISyntaxException {
        recommendationService.putRecommendation(id, recommendation);
    }

    @DeleteMapping("/protected/recommendation/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete recommendation")
    public void deleteRecommendation(@PathVariable String id) {
        recommendationService.deleteRecommendation(id);
    }

}
