package com.vibee.service.vaccountemployee.Impl;

import com.vibee.entity.VRole;
import com.vibee.entity.VUser;
import com.vibee.entity.VUserRole;
import com.vibee.model.Status;
import com.vibee.model.item.CreateItemsUser;
import com.vibee.model.request.vaccountemployee.CreateUserRequest;
import com.vibee.model.request.vaccountemployee.IdUserRoleRequest;
import com.vibee.model.response.vaccountemployee.CreateUserResponse;
import com.vibee.repo.VRoleRepo;
import com.vibee.repo.VUserRepo;
import com.vibee.repo.VUserRoleRepo;
import com.vibee.service.vaccountemployee.CreateUserService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class CreateUserSeviceImpl implements CreateUserService {
//    @Autowired
//    private UserDao userDao;
//    @Autowired
//    private RoleDao roleDao;
//    @Autowired
//    private UserRoleDao userRoleDao;
    @Autowired
    private VUserRepo userRepo;
    @Autowired
    private VUserRoleRepo userRoleRepo;
    @Autowired
    private VRoleRepo roleRepo;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public CreateUserResponse createAccount(CreateUserRequest request, BindingResult bindingResult) {
        CreateUserResponse response = new CreateUserResponse();
        List<VUser> userList =userRepo.findAll();

        String language = request.getLanguage();
        String username = request.getUsername();
        String fullname = request.getFullname();
        String password = request.getPassword();
        String cccd = request.getCccd();
        String address = request.getAddress();
        String numberPhone = request.getNumberPhone();
        String email = request.getEmail();
        int role = request.getRole();

        if (bindingResult.hasErrors()) {
            response.getStatus().setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            response.getStatus().setStatus(Status.Fail);
            return response;
        }

        for (VUser users: userList) {
            if(users.getUsername().equals(username) ){
                response.getStatus().setMessage(MessageUtils.get(language, "msg.username.exist"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }if(users.getEmail().equals(email) ){
                response.getStatus().setMessage(MessageUtils.get(language, "msg.email.exist"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }
            if(users.getCccd().equals(cccd) ){
                response.getStatus().setMessage(MessageUtils.get(language, "msg.cccd.already.exist"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }
            if(users.getNumberPhone().equals(numberPhone) ){
                response.getStatus().setMessage(MessageUtils.get(language, "msg.phone.already.exist"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }

        }

        VUser crtuser = new VUser();

        crtuser.setUsername(username);
        crtuser.setPassword(encoder.encode(password));
        crtuser.setEmail(email);
        crtuser.setFullname(fullname);
        crtuser.setStatus(Integer.parseInt(Status.Success));
        crtuser.setCreated(new Date());
        crtuser.setNumberPhone(numberPhone);
        crtuser.setCccd(cccd);
        crtuser.setAddress(address);

        CreateItemsUser createItemsUser = this.createAccount(crtuser);

        if (createItemsUser == null) {
            log.error("Create account is failed");
            response.getStatus().setMessage(MessageUtils.get(language, "msg.createaccount.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        VRole roles = roleRepo.getById(role);
        VUserRole uRole = new VUserRole();
        uRole.setRole(roles.getId());
        uRole.setUserId(createItemsUser.getId());
        VUserRole userRole = userRoleRepo.save(uRole);

        response.setNameRole(roles.getName());
        response.setFullname(createItemsUser.getFullname());
        response.setPhoneNumber(createItemsUser.getNumberPhone());
        response.setEmail(createItemsUser.getEmail());
        response.setBirthday(createItemsUser.getBirthday());
        response.getStatus().setMessage(MessageUtils.get(language, "msg.createaccount.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("CreateUserService :: End");
        return response;
    }

    public CreateItemsUser createAccount(VUser request) {
        VUser user= userRepo.save(request);
        CreateItemsUser responseItems = new CreateItemsUser();

        responseItems.setId(request.getId());
        responseItems.setUsername(request.getUsername());
        responseItems.setFullname(request.getFullname());
        responseItems.setPassword(request.getPassword());
        responseItems.setCccd(request.getCccd());
        responseItems.setAddress(request.getAddress());
        responseItems.setNumberPhone(request.getNumberPhone());
        responseItems.setEmail(request.getEmail());
        //responseItems.setStatuss(request.getStatus());
        return responseItems;
    }

    @Override
    public CreateUserResponse updateAccount(CreateUserRequest request, BindingResult bindingResult) {
        log.info("UpdateUserService :: Start");
        CreateUserResponse response = new CreateUserResponse();
        List<VUser> userList =userRepo.findAll();

        String language = request.getLanguage();
        int idUserRole = request.getIdUserRole();
        String username = request.getUsername();
        String fullname = request.getFullname();
        String cccd = request.getCccd();
        String address = request.getAddress();
        String numberPhone = request.getNumberPhone();
        String email = request.getEmail();
        int role = request.getRole();

        VUserRole userRole = userRoleRepo.findById(idUserRole).get();
        if (userRole == null) {
            log.error("Update account is failed");
            response.getStatus().setMessage(MessageUtils.get(language, "msg.update.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        if (bindingResult.hasErrors()) {
            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
            System.out.println(response);
            return response;
        } else {
            VUser crtuser = new VUser();
            crtuser = userRepo.findById(userRole.getUserId()).get();

            if (crtuser == null) {
                log.error("Update account is failed");
                response.getStatus().setMessage(MessageUtils.get(language, "msg.update.failse"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            } else {
                for (VUser users : userList) {
                    if (users.getUsername().equals(username)) {
                        if (users.getId() != crtuser.getId()) {
                            response.getStatus().setMessage(MessageUtils.get(language, "msg.username.exist"));
                            response.getStatus().setStatus(Status.Fail);
                            return response;
                        }
                    }
                    if (users.getEmail().equals(email)) {
                        if (users.getId() != crtuser.getId()) {
                            response.getStatus().setMessage(MessageUtils.get(language, "msg.email.exist"));
                            response.getStatus().setStatus(Status.Fail);
                            return response;
                        }
                    }
                    if(users.getCccd().equals(cccd) ){
                        if(users.getId()!=crtuser.getId()){
                            response.getStatus().setMessage(MessageUtils.get(language, "msg.cccd.already.exist"));
                            response.getStatus().setStatus(Status.Fail);
                            return response;
                        }
                    }
                    if(users.getNumberPhone().equals(numberPhone) ){
                        if(users.getId()!=crtuser.getId()){
                            response.getStatus().setMessage(MessageUtils.get(language, "msg.phone.already.exist"));
                            response.getStatus().setStatus(Status.Fail);
                            return response;
                        }
                    }

                }
                crtuser.setUsername(username);
                crtuser.setEmail(email);
                crtuser.setFullname(fullname);
                crtuser.setStatus(Integer.parseInt(Status.Success));
                crtuser.setCreated(new Date());
                crtuser.setNumberPhone(numberPhone);
                crtuser.setCccd(cccd);
                crtuser.setAddress(address);

                CreateItemsUser createItemsUser = this.updateAccount(crtuser);

                this.UpdateRoleAccount(role, userRole.getId());
                VRole roles = roleRepo.getById(role);

                response.setNameRole(roles.getName());
                response.setRole(userRole.getRole());
                response.setFullname(createItemsUser.getFullname());
                //  response.setStatusName(Utiliies.convertStatusUser(createItemsUser.getStatus()));
                response.setPhoneNumber(createItemsUser.getNumberPhone());
                response.setEmail(createItemsUser.getEmail());
                response.setBirthday(createItemsUser.getBirthday());
                response.getStatus().setMessage(MessageUtils.get(language, "msg.update.success"));
                response.getStatus().setStatus(Status.Success);
                log.info("UpdateUserService :: End");
                return response;

            }

        }
    }

    public CreateItemsUser updateAccount(VUser request) {
        VUser user = new VUser();
        user = userRepo.findById(request.getId()).get();
        if (user == null){
            return  null;
        }
        CreateItemsUser responseItems = new CreateItemsUser();
        if (user != null){
            user=userRepo.save(request);
            responseItems.setId(request.getId());
            responseItems.setUsername(request.getUsername());
            responseItems.setFullname(request.getFullname());
            responseItems.setPassword(request.getPassword());
            responseItems.setCccd(request.getCccd());
            responseItems.setAddress(request.getAddress());
            responseItems.setNumberPhone(request.getNumberPhone());
            responseItems.setEmail(request.getEmail());
            //responseItems.setStatus(request.getStatus());
            return responseItems;
        }
        return null;

    }

    public void UpdateRoleAccount(int id, int idUserRole) {
        userRoleRepo.updateRoleAccount(id, idUserRole);
    }

    @Override
    public CreateUserResponse deleteAccount(IdUserRoleRequest request) {
        log.info("DeleteUserService :: Start");
        CreateUserResponse response = new CreateUserResponse();
        String language = request.getLanguage();
        VUserRole userRole = userRoleRepo.findById(request.getIdUserRole()).get();
        VUser crtuser = userRepo.findById(userRole.getUserId()).get();
        if (crtuser ==null){
            response.getStatus().setMessage(MessageUtils.get(language, "msg.delete.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        this.updateStatus(crtuser.getId());
        response.getStatus().setMessage(MessageUtils.get(language, "msg.delete.success"));
        response.getStatus().setStatus(Status.Success);
        return response;
    }

    public void updateStatus(int id) {
        userRepo.updateStatusAccount(id);
    }


    public List<Object[]> getAllAccount(){
        List<Object[]> userRoles = userRoleRepo.getAllAccount();
        return userRoles;
    }

    public String convertStatus(int status) {
        switch (status) {
            case 1 :
                return "đang hoạt động";
            case 2 :
                return "không hoạt động";
            default:
                return "không biết";
        }
    }
}
