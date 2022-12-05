package com.vibee.controller.staffstatistic;

import com.vibee.model.response.adminstatistic.AdminStatisticResponse;
import com.vibee.service.vstatisticadmin.StatisticAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/vibee/api/v1/admins/statistic")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AdminStatisticController {
    @Autowired
    private StatisticAdminService statisticAdminService;


    @Autowired
    public AdminStatisticController(StatisticAdminService statisticAdminService) {
        this.statisticAdminService = statisticAdminService;
    }

    @GetMapping("")
    public AdminStatisticResponse getTotalPriceOfDay() {
        return this.statisticAdminService.totalPriceOfDay();
    }
}
