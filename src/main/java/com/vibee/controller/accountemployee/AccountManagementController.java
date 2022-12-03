package com.vibee.controller.accountemployee;

import com.vibee.model.request.vaccountemployee.CreateUserRequest;
import com.vibee.model.request.vaccountemployee.IdUserRoleRequest;
import com.vibee.model.response.vaccountemployee.CreateUserResponse;
import com.vibee.repo.VUserRoleRepo;
import com.vibee.service.vaccountemployee.CreateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("vibee/api/v1/admins/customer")
public class AccountManagementController {
    private CreateUserService createUserService;
    VUserRoleRepo userRepo;
    BCryptPasswordEncoder encoder;

    @Autowired
    public AccountManagementController(
            CreateUserService createUserService,
            VUserRoleRepo userRepo,
            BCryptPasswordEncoder encoder
    ) {
        this.createUserService =createUserService;
        this.encoder = encoder;
        this.userRepo = userRepo;
    }

    @PostMapping(value = "/create" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

    public CreateUserResponse createUser(@Valid @RequestBody CreateUserRequest request, BindingResult bindingResult) {
        return createUserService.createAccount(request, bindingResult);
    }
    @PostMapping  (value = "/update" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateUserResponse updateUser(@Valid @RequestBody CreateUserRequest request, BindingResult bindingResult) {
        return createUserService.updateAccount(request, bindingResult);
    }
    @GetMapping (value = "/delete")
    public CreateUserResponse deleteUser(@Valid @RequestBody IdUserRoleRequest request) {
        CreateUserResponse response = createUserService.deleteAccount(request);
        return response;
    }
    @GetMapping (value = "/vibee/api/v1/adminsStaff/getAll",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Object[]> getAll() {
        List<Object[]> responses = createUserService.getAllAccount();
        return responses;
    }

}
