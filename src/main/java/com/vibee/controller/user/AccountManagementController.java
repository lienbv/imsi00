package com.vibee.controller.user;
import com.vibee.model.request.user.CheckAccountRequest;
import com.vibee.model.request.user.EditAccountRequest;
import com.vibee.model.request.user.UpdateAccountRequest;
import com.vibee.model.request.v_unit.GetOrderByStringRequest;
import com.vibee.model.request.vaccountemployee.CreateUserRequest;
import com.vibee.model.request.vaccountemployee.IdUserRoleRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.user.CreateUserResponse;
import com.vibee.model.response.user.GetUserItemsResponse;
import com.vibee.service.vuser.IUserService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/vibee/api/v1/auth/user")
@CrossOrigin("*")
public class AccountManagementController {

    private final IUserService iUserService;

    public AccountManagementController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @PostMapping (value = "/create")
    public CreateUserResponse createUser(@Valid @RequestBody CreateUserRequest request, BindingResult bindingResult) {
        return iUserService.createAccount(request, bindingResult);
    }
    @GetMapping (value = "/edit/{id}")
    public CreateUserResponse edit(@PathVariable(name = "id") int id,
                                   @RequestParam(name= "language",required = false, defaultValue = "") String language) {
        EditAccountRequest request = new EditAccountRequest();
        request.setIdUserRoles(id);
        return iUserService.edit(request,language);
    }

    @PostMapping  (value = "/update")
    public CreateUserResponse updateUser(@Valid @RequestBody UpdateAccountRequest request, BindingResult bindingResult) {
        return iUserService.updateAccount(request, bindingResult);
    }
    @PostMapping (value = "/delete")
    public BaseResponse deleteUser(@Valid @RequestBody IdUserRoleRequest request) {
        return iUserService.deleteAccount(request);
    }
    @PostMapping (value = "/unlock")
    public BaseResponse unlock(@Valid @RequestBody IdUserRoleRequest request) {
        BaseResponse response = iUserService.unlockAccount(request);
        return response;
    }
    @GetMapping (value = "/getAll-user")
    public GetUserItemsResponse getAll(@RequestParam(name = "pagenumber") int pageNumberReq,
                                       @RequestParam(name = "pagesize") int pageSizeReq,
                                       @RequestParam(name= "language",required = false, defaultValue = "") String languageReq,
                                       @RequestParam(name = "search", required = false, defaultValue = "") String searchReq) {
        GetOrderByStringRequest request = new GetOrderByStringRequest();
        request.setPageNumber(pageNumberReq);
        request.setPageSize(pageSizeReq);
        request.setLanguage(languageReq);
        request.setSearchText(searchReq);
        return this.iUserService.getAllAccount(request);
    }
    @PostMapping(value = "/checkAccount")
    public CreateUserResponse checkAccount(@Valid @RequestBody CheckAccountRequest request){
        return this.iUserService.checkAccount(request);
    }
    @PostMapping(value = "/checkAccountUpdate")
    public CreateUserResponse checkAccountUpdate(@Valid @RequestBody CheckAccountRequest request){
        return this.iUserService.checkAccountUpdate(request);
    }

}
