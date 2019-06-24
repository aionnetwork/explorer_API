package com.aion.dashboard.controllers;


import com.aion.dashboard.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/v2/monitor")
public class Monitor {

    private StatisticsService statisticsService;
    @Autowired
    public Monitor(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(value = "/health", produces = "text/plain")
    public String health(){
        return statisticsService.health().getStatus();
    }
}
