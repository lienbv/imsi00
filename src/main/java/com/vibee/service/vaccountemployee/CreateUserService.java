package com.vibee.service.vaccountemployee;

import com.vibee.model.request.vaccountemployee.CreateUserRequest;
import com.vibee.model.request.vaccountemployee.IdUserRoleRequest;
import com.vibee.model.response.vaccountemployee.CreateUserResponse;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;

public interface CreateUserService {
    public CreateUserResponse createAccount(CreateUserRequest request, BindingResult bindingResult);
    public CreateUserResponse updateAccount(CreateUserRequest request, BindingResult bindingResult);
    public CreateUserResponse deleteAccount (IdUserRoleRequest request);
    public List<Object[]> getAllAccount();
}
