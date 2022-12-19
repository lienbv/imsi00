package com.vibee.controller.expried;

import com.vibee.model.response.expired.CloseToExpiresResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vibee/api/v1/close-to-expiration/")
@CrossOrigin("*")
public class CloseToExpirationController {
    @GetMapping("")
    public CloseToExpiresResponse getAll(@RequestParam(value = "nameSearch", defaultValue = "") String nameSearch) {
        return null;
    }
}
