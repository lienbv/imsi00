package com.vibee.service.vstatisticadmin;

import com.vibee.model.item.InterestRateItem;
import com.vibee.model.request.BaseRequest;
import com.vibee.model.response.adminstatistic.ReportTopProductResponse;
import com.vibee.model.response.adminstatistic.StatisticAdminResponse;
import com.vibee.model.response.adminstatistic.TypeProductResponse;


public interface StatisticAdminService {
    public StatisticAdminResponse totalPriceOfDay();
    public InterestRateItem interestRate();
    public StatisticAdminResponse statisticDisplay7Days(BaseRequest request, String startDate, String endDate);
    public TypeProductResponse displayTop5TypeProduct(BaseRequest request);
    public StatisticAdminResponse reportSumProduct(BaseRequest request);
    public StatisticAdminResponse reportSumOrder(BaseRequest request);
    ReportTopProductResponse getTop6Product(String language);
}
