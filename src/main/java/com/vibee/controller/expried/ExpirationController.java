package com.vibee.controller.expried;

import com.vibee.model.response.ExpirationResponse;
import com.vibee.service.expired.ExpiredServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vibee/api/v1/expiration")
@CrossOrigin("*")
public class ExpirationController {
    private ExpiredServer expiredServer;

    @Autowired
    public ExpirationController(ExpiredServer expiredServer) {
        this.expiredServer = expiredServer;
    }

    @GetMapping("")
    public ExpirationResponse getAll(@RequestParam(value = "nameSearch", defaultValue = "") String nameSearch,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "record", defaultValue = "10") int record) {
        return this.expiredServer.getAll(nameSearch,page,record);
    }
}
