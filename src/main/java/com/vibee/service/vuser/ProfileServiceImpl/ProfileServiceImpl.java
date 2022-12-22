package com.vibee.service.vuser.ProfileServiceImpl;

import com.vibee.entity.VUser;
import com.vibee.model.Status;
import com.vibee.model.request.user.UpdateAccountRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.request.user.ChangePasswordRequest;
import com.vibee.model.request.user.EmailRequest;
import com.vibee.model.response.email.EmailResponse;
import com.vibee.model.response.user.ProfileResponse;
import com.vibee.repo.VUserRepo;
import com.vibee.service.vemail.MailServiceImpl;
import com.vibee.service.vuser.IprofileService;
import com.vibee.utils.Const;
import com.vibee.utils.DataUtils;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Log4j2
public class ProfileServiceImpl implements IprofileService {
    private final VUserRepo userRepo;

    @Autowired
    MailServiceImpl mailService;

    @Autowired
    public ProfileServiceImpl(VUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public ProfileResponse profile() {
        log.info("ProfileServiceImpl :: Start");

        String username = "lienpt";
        ProfileResponse response = new ProfileResponse();
        String language = "";
        if (username.isEmpty()) {
            log.info("ProfileServiceImpl :: Failed");
            response.getStatus().setMessage(MessageUtils.get(language, "msg.username.not.exit"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }

        VUser user = new VUser();
        user.setUsername(username);
        user = userRepo.findByUsername(user.getUsername());
        response.setFullname(user.getFullname());
        response.setEmail(user.getEmail());
        response.setAddress(user.getAddress());
        response.setNumberPhone(user.getNumberPhone());
        response.setUsername(user.getUsername());
        response.setCccd(user.getCccd());
        log.info("ProfileServiceImpl :: End");
        return response;
    }

    @Override
    public BaseResponse updateAccount(UpdateAccountRequest request, BindingResult bindingResult) {
        log.info("UpdateUserService :: Start");
        BaseResponse response = new BaseResponse();
        List<VUser> userList = userRepo.findAll();

        String username = "lienpt";
        String language = request.getLanguage();
        String fullname = request.getFullname();
        String cccd = request.getCccd();
        String address = request.getAddress();
        String numberPhone = request.getNumberPhone();
        String email = request.getEmail();

        VUser crtuser  = userRepo.findByUsername(username);

        if (bindingResult.hasErrors()) {
            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
            System.out.println(response);
            return response;
        } else {

            if (crtuser == null) {
                log.error("Update account is failed");
                response.getStatus().setMessage(MessageUtils.get(language, "msg.update.failse"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            } else {
                for (VUser users : userList) {

                    if (email.equals(users.getEmail())) {
                        if (users.getId() != crtuser.getId()) {
                            response.getStatus().setMessage(MessageUtils.get(language, "msg.email.exist"));
                            response.getStatus().setStatus(Status.Fail);
                            return response;
                        }
                    }
                    if (cccd.equals(users.getCccd())) {
                        if (users.getId() != crtuser.getId()) {
                            response.getStatus().setMessage(MessageUtils.get(language, "msg.cccd.already.exist"));
                            response.getStatus().setStatus(Status.Fail);
                            return response;
                        }
                    }
                    if (numberPhone.equals(users.getNumberPhone())) {
                        if (users.getId() != crtuser.getId()) {
                            response.getStatus().setMessage(MessageUtils.get(language, "msg.phone.already.exist"));
                            response.getStatus().setStatus(Status.Fail);
                            return response;
                        }
                    }

                }
                crtuser.setId(crtuser.getId());
                crtuser.setEmail(email);
                crtuser.setFullname(fullname);
                crtuser.setStatus(1);
                crtuser.setCreated(new Date());
                crtuser.setNumberPhone(numberPhone);
                crtuser.setCccd(cccd);
                crtuser.setAddress(address);
                this.userRepo.save(crtuser);
                // save in to keycloak

                response.getStatus().setMessage(MessageUtils.get(language, "msg.update.success"));
                response.getStatus().setStatus(Status.Success);

                log.info("UpdateUserService :: End");
                return response;

            }

        }

    }

    @Override
    public BaseResponse sendEmail(EmailRequest emailRequest, BindingResult bindingResult) {
        log.info("Forgot password : Start");
        EmailResponse emailResponse = new EmailResponse();
        BaseResponse response = new BaseResponse();
        VUser user = userRepo.findByUsernameAndEmail(emailRequest.getUsername(), emailRequest.getEmail());
        Optional<VUser> userOptional = Optional.ofNullable(user);
        String language = emailRequest.getLanguage();

        if (bindingResult.hasErrors()) {
            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        if (!userOptional.isPresent()) {
            log.info("Forgot password : Failed");
            response.getStatus().setMessage(MessageUtils.get(language, "msg.email.already.exist"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }

        if (!response.getStatus().getStatus().equals("0")) {
            try {
                String password = DataUtils.generateTempPwd(1, 3, 1, 1);
                Map<String, Object> props = new HashMap<>();

                props.put("password", password);
                props.put("name", userOptional.get().getFullname());
                props.put("username", userOptional.get().getUsername());
                emailResponse.setProps(props);
                emailResponse.setTo(emailRequest.getEmail());
                emailResponse.setSubject(Const.SEND_MAIL_SUBJECT.CLIENT_FORGOT_PASS);

                mailService.sendHtmlMail(emailResponse, Const.TEMPLATE_FILE_NAME.CLIENT_FORGOT_PASS);

                // mã hóa password rùm t
                user.setPassword(password);
                user.setId(user.getId());
                this.userRepo.save(user);
                // save in to keycloak

                response.getStatus().setMessage(MessageUtils.get(language, "msg.forgotpass.success"));
                response.getStatus().setStatus(Status.Success);
                return response;
            } catch (MessagingException exp) {
                log.error("Forgot password : Failsed");
                response.getStatus().setMessage(MessageUtils.get(language, "msg.forgotpass.failse"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }
        }
        log.info("Forgot password : End");
        return response;

    }

    @Override
    public BaseResponse changePassword(ChangePasswordRequest request, BindingResult bindingResult) {
        log.info("Change password : Start");
        EmailResponse emailResponse = new EmailResponse();
        Map<String, Object> props = new HashMap<>();
        String language = request.getLanguage();
        String oldPass = request.getOldPassword();
        String newPass = request.getNewPassword();
        String reEnterPass = request.getReEnterPassword();

        BaseResponse response = new BaseResponse();
        // Call username from redis

        String username= "lienpt";
        VUser user = new VUser();
        user = this.userRepo.findByUsername(username);
        if (bindingResult.hasErrors()) {
            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
//        if ((BCrypt.checkpw(oldPass, user.getPassword())) == false) {
        // call password from keycloak


        if(!oldPass.equals(user.getPassword())){
            log.error("OldPassword is failse");
            response.getStatus().setMessage(MessageUtils.get(language, "msg.password.oldPassword.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        if (oldPass.equals(newPass)) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.password.newPassword.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        if (!newPass.equals(reEnterPass)) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.password.reEnterPassword.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        try {

            emailResponse.setProps(props);
            emailResponse.setTo(user.getEmail());
            emailResponse.setSubject(Const.SEND_MAIL_CHANGPASSWORD.CLIENT_FORGOTPASSWORD);
            props.put("name", user.getFullname());
            SimpleDateFormat fr = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
            Date date = new Date();
            props.put("date", fr.format(date));

            // mã hóa rùm t
            user.setPassword(newPass);
            user.setId(user.getId());
            mailService.sendHtmlMail(emailResponse, Const.TEMPLATE_CHANGE_PASSWORD.CLIENT_CHANGE_PASSWORD);
            this.userRepo.save(user);
            // save in keycloak

            response.getStatus().setMessage(MessageUtils.get(language, "msg.password.success"));
            response.getStatus().setStatus(Status.Success);
            log.info("Change password : End");
            return response;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        //
    }
}
