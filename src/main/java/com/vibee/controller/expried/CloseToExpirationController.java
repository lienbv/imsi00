package com.vibee.controller.expried;

import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.expired.CloseToExpiresResponse;
import com.vibee.model.request.expired.EditPriceExportRequest;
import com.vibee.service.closetoexpired.CloseToExpiredService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vibee/api/v1/close-to-expiration")
@CrossOrigin("*")
public class CloseToExpirationController {
    private CloseToExpiredService closeToExpiredService;

    @Autowired
    public CloseToExpirationController(CloseToExpiredService closeToExpiredService) {
        this.closeToExpiredService = closeToExpiredService;
    }

    @GetMapping("")
    public CloseToExpiresResponse getAll(@RequestParam(value = "nameSearch", defaultValue = "") String nameSearch,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "record", defaultValue = "10") int record) {
        return this.closeToExpiredService.getAll(nameSearch, page, record);
    }

    @PostMapping("/edit-price-export")
    public BaseResponse payment(@RequestBody EditPriceExportRequest request) {
        return this.closeToExpiredService.editPriceExport(request);
    }
}
