package ru.bmstu.cp.rsoi.statistic.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bmstu.cp.rsoi.statistic.model.ListStatisticOut;
import ru.bmstu.cp.rsoi.statistic.service.StatisticService;

@RestController
@RequestMapping("/api/1.0")
@Api(value = "Recommendation service")
public class StatisticController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StatisticService statisticService;

    @Secured({"ROLE_ADMIN"})
    @GetMapping(path = "/protected/statistic")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public ListStatisticOut getStatistic(@RequestParam(value = "dateStart", required = true) Long dateStart,
                                          @RequestParam(value = "dateEnd", required = true) Long dateEnd,
                                          @RequestParam(value = "entity", required = true) String entity) {

        return statisticService.getStatistic(dateStart, dateEnd, entity);
    }

}
