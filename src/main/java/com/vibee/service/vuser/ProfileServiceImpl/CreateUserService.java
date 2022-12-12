package com.vibee.service.vuser.ProfileServiceImpl;
import com.vibee.entity.VRole;
import com.vibee.entity.VUser;
import com.vibee.model.Status;
import com.vibee.model.item.UserItems;
import com.vibee.model.request.user.CheckAccountRequest;
import com.vibee.model.request.user.EditAccountRequest;
import com.vibee.model.request.user.UpdateAccountRequest;
import com.vibee.model.request.v_unit.GetOrderByStringRequest;
import com.vibee.model.request.vaccountemployee.CreateUserRequest;
import com.vibee.model.request.vaccountemployee.IdUserRoleRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.user.CreateUserResponse;
import com.vibee.model.response.user.GetUserItemsResponse;
import com.vibee.repo.VUserRepo;
import com.vibee.service.vuser.IUserService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class CreateUserService implements IUserService {
    @Autowired
    private VUserRepo userRepo;

    @Override
    public CreateUserResponse createAccount(@Valid CreateUserRequest request, BindingResult bindingResult) {
        log.info("CreateUserService :: Start");
        CreateUserResponse response = new CreateUserResponse();
        List<VUser> userList = userRepo.findAll();

        String language = request.getLanguage();
        String username = request.getUsername();
        String fullname = request.getFullname();
        String password = request.getPassword();
        String cccd = request.getCccd();
        String address = request.getAddress();
        String numberPhone = request.getNumberPhone();
        String email = request.getEmail();

        if (bindingResult.hasErrors()) {
            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        for (VUser users : userList) {
            if (username.equals(users.getUsername())) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.username.exist"));
                response.getStatus().setStatus(Status.Fail);
                return response;

            }
            if (email.equals(users.getEmail())) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.email.exist"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }

            if (cccd.equals(users.getCccd())) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.cccd.already.exist"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }
            if (numberPhone.equals(users.getNumberPhone())) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.phone.already.exist"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }
        }
        VUser crtuser = new VUser();

        crtuser.setUsername(username);
        crtuser.setPassword(password);
        crtuser.setEmail(email);
        crtuser.setFullname(fullname);
        crtuser.setStatus(Integer.parseInt(Status.Success));
        crtuser.setCreated(new Date());
        crtuser.setNumberPhone(numberPhone);
        crtuser.setCccd(cccd);
        crtuser.setAddress(address);
        crtuser = this.userRepo.save(crtuser);

        VRole roles = new VRole();
        roles.setId(2);

        response.setNameRole(roles.getName());
        response.setFullname(crtuser.getFullname());
        response.setPhoneNumber(crtuser.getNumberPhone());
        response.setEmail(crtuser.getEmail());
        response.setStatusName(this.convertStatus(1, language));
        response.getStatus().setMessage(MessageUtils.get(language, "msg.createaccount.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("CreateUserService :: End");
        return response;
    }

    @Override
    public CreateUserResponse edit(EditAccountRequest request, String language) {
        log.info("CreateUserService :: Start");
        List<VUser> userList = userRepo.findAll();
        CreateUserResponse response = new CreateUserResponse();
        VUser vUser = this.userRepo.findById(response.getIdUser());

        if (vUser != null) {
            response.setIdUser(vUser.getId());
            response.setUsername(vUser.getUsername());
            response.setFullname(vUser.getFullname());
            response.setPhoneNumber(vUser.getNumberPhone());
            response.setEmail(vUser.getEmail());
            response.setAddress(vUser.getAddress());
            response.setCccd(vUser.getCccd());
        }
        return response;

    }

    @Override
    public CreateUserResponse updateAccount(@Valid UpdateAccountRequest request, BindingResult bindingResult) {
        log.info("UpdateUserService :: Start");
        CreateUserResponse response = new CreateUserResponse();
        List<VUser> userList = userRepo.findAll();

        String language = request.getLanguage();
        String fullname = request.getFullname();
        String cccd = request.getCccd();
        String address = request.getAddress();
        String numberPhone = request.getPhoneNumber();
        String email = request.getEmail();

        VUser vUser = this.userRepo.findById(response.getIdUser());

        if (bindingResult.hasErrors()) {
            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
            System.out.println(response);
            return response;
        }
        if (vUser == null) {
            log.error("Update account is failed");
            response.getStatus().setMessage(MessageUtils.get(language, "msg.update.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        } else {

            for (VUser users : userList) {

                if (email.equals(users.getEmail())) {
                    if (users.getId() != vUser.getId()) {
                        response.getStatus().setMessage(MessageUtils.get(language, "msg.email.exist"));
                        response.getStatus().setStatus(Status.Fail);
                        return response;
                    }
                }
                if (cccd.equals(users.getCccd())) {
                    if (users.getId() != vUser.getId()) {
                        response.getStatus().setMessage(MessageUtils.get(language, "msg.cccd.already.exist"));
                        response.getStatus().setStatus(Status.Fail);
                        return response;
                    }
                }
                if (numberPhone.equals(users.getNumberPhone())) {
                    if (users.getId() != vUser.getId()) {
                        response.getStatus().setMessage(MessageUtils.get(language, "msg.phone.already.exist"));
                        response.getStatus().setStatus(Status.Fail);
                        return response;
                    }
                }

            }

            vUser.setEmail(email);
            vUser.setFullname(fullname);
            vUser.setStatus(1);
            vUser.setCreated(new Date());
            vUser.setNumberPhone(numberPhone);
            vUser.setCccd(cccd);
            vUser.setAddress(address);

            vUser = this.userRepo.save(vUser);

            response.setFullname(vUser.getFullname());
            response.setStatusName(this.convertStatus(vUser.getStatus(), language));
            response.setPhoneNumber(vUser.getNumberPhone());
            response.setAddress(vUser.getAddress());
            response.setUsername(vUser.getUsername());
            response.setEmail(vUser.getEmail());
            response.getStatus().setMessage(MessageUtils.get(language, "msg.update.success"));
            response.getStatus().setStatus(Status.Success);

            log.info("UpdateUserService :: End");
            return response;

        }

    }

    @Override
    public BaseResponse deleteAccount( IdUserRoleRequest request) {
        log.info("DeleteUserService :: Start");

        BaseResponse response = new BaseResponse();
        String language = request.getLanguage();
        VUser vUser = userRepo.findById(request.getIdUserRole());

        if (vUser == null) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.delete.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        if (vUser.getStatus() == 2 || vUser.getStatus() == 2) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.delete.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        vUser.setStatus(3);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.delete.success"));
        response.getStatus().setStatus(Status.Success);
        return response;
    }

    @Override
    public BaseResponse unlockAccount(@Valid IdUserRoleRequest request) {
        log.info("UnlockUserService :: Start");

        BaseResponse response = new BaseResponse();
        String language = request.getLanguage();
        VUser vUser = userRepo.findById(request.getIdUserRole());

        if (vUser == null) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.delete.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        if (vUser.getStatus() == 2 || vUser.getStatus() == 2) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.delete.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        vUser.setStatus(2);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.delete.success"));
        response.getStatus().setStatus(Status.Success);
        return response;
    }

    @Override
    public GetUserItemsResponse getAllAccount(GetOrderByStringRequest request) {
        log.info("GetAllAccount :: Start");
        GetUserItemsResponse response = new GetUserItemsResponse();
        int pageNumber = request.getPageNumber();
        int pageSize = request.getPageSize();
        int totalPage = 0;
        String searchText = request.getSearchText();
        Page<VUser> findAll=null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if (searchText == null || searchText.equals("")) {
            findAll = this.userRepo.findAll(pageable);
            if (findAll == null) {
                response.getStatus().setMessage(MessageUtils.get(request.getLanguage(), "msg.not.data"));
                response.getStatus().setStatus(Status.Fail);
            }
            totalPage = findAll.getTotalPages();

        } else {
            findAll = this.userRepo.findByFullname(searchText + "%", pageable);
            if (findAll == null) {
                response.getStatus().setMessage(MessageUtils.get(request.getLanguage(), "msg.not.data"));
                response.getStatus().setStatus(Status.Fail);
            }
            totalPage = findAll.getTotalPages();
        }
        response.setItems(this.convertUser(findAll.getContent(), request.getLanguage()));
        response.setPage(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalPages(totalPage);
        response.setTotalItems(findAll.getContent().size());
        response.getStatus().setMessage(MessageUtils.get(request.getLanguage(), "msg.have.data"));
        response.getStatus().setStatus(Status.Success);
        log.info("GetAllAccount :: End");
        return response;
    }

    private List<UserItems> convertUser(List<VUser> request, String language) {
        List<UserItems> userItems = new ArrayList<>();
        int status = 0;
        for (VUser user : request) {
            UserItems userItem = new UserItems();
            userItem.setId(user.getId());
            userItem.setCreator("Liên nè");
            userItem.setStatus(user.getStatus());
            userItem.setStatusName(this.convertStatus(user.getStatus(), language));
            userItem.setAddress(user.getAddress());
            userItem.setEmail(user.getEmail());
            userItem.setFullname(user.getFullname());
            userItem.setNumberPhone(user.getNumberPhone());
            userItem.setUsername(user.getUsername());
            userItems.add(userItem);
        }
        return userItems;
    }

    public String convertStatus(int status, String language) {
        switch (status) {
            case 1:
                return "Active";
            case 2:
                return "Inactive";
            default:
                return "không biết";
        }
    }

    @Override
    public CreateUserResponse checkAccount(@Valid CheckAccountRequest request) {

        CreateUserResponse response = new CreateUserResponse();
        String language = request.getLanguage();
        String username = request.getUsername();
        String cccd = request.getCccd();
        String numberPhone = request.getNumberPhone();
        String email = request.getEmail();
        VUser checkUsername = this.userRepo.findByUsername(username);
        VUser checkEmail = this.userRepo.findByEmail(email);
        VUser checkPhone = this.userRepo.findByNumberPhone(numberPhone);
        VUser checkCccd = this.userRepo.findByCccd(cccd);
        if (checkUsername != null) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.username.exist"));
            response.getStatus().setStatus("username");
            return response;
        }
        if (checkEmail != null) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.email.exist"));
            response.getStatus().setStatus("email");
            return response;
        }
        if (checkCccd != null) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.cccd.already.exist"));
            response.getStatus().setStatus("cccd");
            return response;
        }
        if (checkPhone != null) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.phone.already.exist"));
            response.getStatus().setStatus("phone");
            return response;
        }

        return response;

    }

    @Override
    public CreateUserResponse checkAccountUpdate(@Valid CheckAccountRequest request) {
        int idUserRole = request.getIdUserRoles();

        VUser vUser = this.userRepo.findById(idUserRole);

        CreateUserResponse response = new CreateUserResponse();
        String language = request.getLanguage();
        String username = request.getUsername();
        String cccd = request.getCccd();
        String numberPhone = request.getNumberPhone();
        String email = request.getEmail();
        VUser checkUsername = this.userRepo.findByUsername(username);
        VUser checkEmail = this.userRepo.findByEmail(email);
        VUser checkPhone = this.userRepo.findByNumberPhone(numberPhone);
        VUser checkCccd = this.userRepo.findByCccd(cccd);
        if (checkUsername != null) {
            if (checkUsername.getId() != vUser.getId()) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.username.exist"));
                response.getStatus().setStatus("username");
                return response;
            }

        }
        if (checkEmail != null) {
            if (checkEmail.getId() != vUser.getId()) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.email.exist"));
                response.getStatus().setStatus("email");
                return response;
            }

        }
        if (checkCccd != null) {
            if (checkCccd.getId() != vUser.getId()) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.cccd.already.exist"));
                response.getStatus().setStatus("cccd");
                return response;
            }

        }
        if (checkPhone != null) {
            if (checkPhone.getId() != vUser.getId()) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.phone.already.exist"));
                response.getStatus().setStatus("phone");
                return response;
            }
        }
        return response;

    }
}

