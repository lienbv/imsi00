package com.vibee.controller.statistic;

import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.staffstatistic.StaffStatisticResponse;
import com.vibee.service.vstaffstatistic.StaffStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RequestMapping("/vibee/api/v1/staff/statistic")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController

public class StaffStatisticController {
    private final StaffStatisticService service;

    @Autowired
    private StaffStatisticController(StaffStatisticService service) {
        this.service = service;
    }


    @GetMapping("")
    public StaffStatisticResponse display(
            //@RequestBody BaseRequest request,
            @RequestParam(value = "language", defaultValue = "vi") String language,
            @RequestParam(value = "numberPage", defaultValue = "0") int numberPage,
            @RequestParam(value = "sizePage", defaultValue = "10") int sizePage,
            @RequestParam(value = "idBill", defaultValue = "") String idBill,
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "createdDate", defaultValue = "") String createdDate,
            @RequestParam(value = "price", defaultValue = "") String price,
            @RequestParam(value = "creator", defaultValue = "") String creator
    ) {
        //System.out.println(idBill+"-"+status+"-"+price+"-"+creator);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return service.displaỵ(language,
                numberPage,
                sizePage,
                idBill,
                status,
                createdDate,
                price,
                creator,
                format.format(date)
        );
    }

    @GetMapping("/export")
    public BaseResponse exportToExcel(HttpServletResponse response,
                                      //@RequestBody BaseRequest request,
                                      @RequestParam(value = "language", defaultValue = "vi") String language,
                                      @RequestParam(value = "numberPage", defaultValue = "0") int numberPage,
                                      @RequestParam(value = "sizePage", defaultValue = "100") int sizePage,
                                      @RequestParam(value = "idBill", defaultValue = "") String idBill,
                                      @RequestParam(value = "status", defaultValue = "") String status,
                                      @RequestParam(value = "createdDate", defaultValue = "") String createdDate,
                                      @RequestParam(value = "price", defaultValue = "") String price,
                                      @RequestParam(value = "creator", defaultValue = "") String creator

    ) throws IOException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return service.export(language,
                numberPage,
                sizePage,
                idBill,
                status,
                createdDate,
                price,
                creator,
                format.format(date)
        );
    }
}
