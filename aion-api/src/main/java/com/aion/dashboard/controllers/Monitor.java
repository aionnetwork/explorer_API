package com.aion.dashboard.controllers;

import com.aion.dashboard.controllers.mapper.ReorgDetailsMapper;
import com.aion.dashboard.datatransferobject.ReorgDetailsDTO;
import com.aion.dashboard.services.StatisticsService;
import com.aion.dashboard.services.ReorgDetailsService;
import com.aion.dashboard.view.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/v2/monitor")
public class Monitor {

    private ReorgDetailsService service;
    private StatisticsService statisticsService;

    @Autowired
    public Monitor(ReorgDetailsService service,
        StatisticsService statisticsService) {
        this.service = service;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/reorgs")
    public Result<ReorgDetailsDTO> reorgs(@RequestParam(value = "size", defaultValue = "25") Integer size,
                                   @RequestParam(value = "page", defaultValue = "0") Integer page){
        return ReorgDetailsMapper.getInstance().makeResult(service.findAll(page, size));
    }

    @GetMapping(value = "/health", produces = "text/plain")
    public String health(){
        return statisticsService.health().getStatus();
    }
}
