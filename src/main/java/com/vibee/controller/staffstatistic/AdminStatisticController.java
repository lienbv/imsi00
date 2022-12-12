package com.vibee.controller.staffstatistic;

import com.vibee.model.item.InterestRateItem;
import com.vibee.model.request.BaseRequest;
import com.vibee.model.response.adminstatistic.ReportTopProductResponse;
import com.vibee.model.response.adminstatistic.StatisticAdminResponse;
import com.vibee.model.response.adminstatistic.TypeProductResponse;
import com.vibee.service.vstatisticadmin.StatisticAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/total-price-of-day")
    public StatisticAdminResponse getTotalPriceOfDay() {
        return this.statisticAdminService.totalPriceOfDay();
    }

    @GetMapping("")
    public StatisticAdminResponse display7days(BaseRequest request, @RequestParam(value = "startDate", defaultValue = "") String startDate,
                                               @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        return statisticAdminService.statisticDisplay7Days(request, startDate, endDate);
    }

    @GetMapping("/report-sum-product")
    public StatisticAdminResponse displaySumProduct(BaseRequest request){
        return statisticAdminService.reportSumProduct(request);
    }

    @GetMapping("/top-5-product")
    public ReportTopProductResponse displayTop6Product(@RequestParam("language") String lang){
        return this.statisticAdminService.getTop6Product(lang);
    }

    @GetMapping("/report-sum-order")
    public StatisticAdminResponse displaySumOrder(BaseRequest request){
        return this.statisticAdminService.reportSumOrder(request);
    }

    @GetMapping("/top5-category")
    public TypeProductResponse displayTop5TypeProduct(BaseRequest request){
        return statisticAdminService.displayTop5TypeProduct(request);
    }

    @GetMapping("/interest-rate")
    public InterestRateItem interestRate(){
        return statisticAdminService.interestRate();
    }
}
