package com.vibee.service.v_customeraccount.Impl;

import com.vibee.entity.VUser;
import com.vibee.entity.VUserRole;
import com.vibee.model.Status;
import com.vibee.model.item.CustomerAccountItem;
import com.vibee.model.request.v_customeraccount.CustomerAccontRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.v_customeraccount.CreateCustomerAccountResponse;
import com.vibee.model.response.v_customeraccount.CustomerAccountsResponse;
import com.vibee.repo.VBillRepo;
import com.vibee.repo.VUserRepo;
import com.vibee.repo.VUserRoleRepo;
import com.vibee.service.v_customeraccount.CustomerAccountService;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Log4j2
@Service
public class CustomerAccountServiceImpl implements CustomerAccountService {

//    @Autowired
//    UserRoleDao userRoleDao;

//    @Autowired
//    UserDao userDao;

    @Autowired
    VUserRepo userRepo;

//    @Autowired
//    BillDao billDao;

    @Autowired
    VBillRepo billRepo;

    @Autowired
    VUserRoleRepo userRoleRepo;

    private final static String ASC = "asc";
    private final static String DESC = "desc";

    private static Map<Integer, String> typeFilter = new HashMap<Integer, String>();

    static {
        typeFilter.put(0, "fullname");
        typeFilter.put(1, "address");
        typeFilter.put(2, "numberPhone");
        typeFilter.put(3, "cccd");
        typeFilter.put(4, "email");
    }

    private final String regexNumberPhone = "^0\\d{9}$";
    private final String regexEmail = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    private final String regexCCCD = "^\\d{12}$";


    @Override
    public CustomerAccountsResponse listAll(String search, int status, int pageNumber, int size, String fullname, String address, String numberPhone, String cccd, String email) {
        log.info("CustomerService-ListAll :: Start");
        List<String> filters = new ArrayList<>();
        filters.add(fullname);
        filters.add(address);
        filters.add(numberPhone);
        filters.add(cccd);
        filters.add(email);

        CustomerAccountsResponse response = new CustomerAccountsResponse();
        Pageable pageable = PageRequest.of(pageNumber, size);

        for (int i = 0; i < filters.size(); i++) {
            String filter = filters.get(i);
            if (filter.equalsIgnoreCase(ASC)) {
                pageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, typeFilter.get(i)));
            } else if (filter.equalsIgnoreCase(DESC)) {
                pageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, typeFilter.get(i)));
            }
        }

        List<CustomerAccountItem> customers = new ArrayList<>();
        List<VUser> list = new ArrayList<>();
        Page<VUser> users = null;

        if (isNumeric(search)) {
            if (search.matches(regexNumberPhone)) {
                System.out.println("--------------> phone");
                list = userRepo.getCustomerByNumberPhones(status, "%"+search+"%");
                users = userRepo.getCustomerByNumberPhone(status, "%"+search+"%", pageable);;
            } else {
                System.out.println("--------------> cccd");
                list = userRepo.getCustomerByCCCD(status, "%"+search+"%");
                users = userRepo.getCustomerByCCCD(status, "%"+search+"%", pageable);
            }
        } else {
            if (search.matches(regexEmail)) {
                System.out.println("--------------> email");
                list = userRepo.getCustomerByEmail(status, "%"+search+"%");
                users = userRepo.getCustomerByEmail(status, "%"+search+"%", pageable);;
            } else {
                System.out.println("--------------> not email");
                list = userRepo.getCustomerByNames(status, "%"+search+"%");
                users = userRepo.getCustomerByName(status, "%"+search+"%", pageable);
            }
        }

        for (VUser user: users.getContent()) {
            CustomerAccountItem item = new CustomerAccountItem();
            item.setIdCustomer(user.getId());
            item.setAddress(user.getAddress());
            item.setEmail(user.getEmail());
            item.setFullname(user.getFullname());
            item.setCccd(user.getCccd());
            item.setNumberphone(user.getNumberPhone());
            item.setStatus(user.getStatus());
            item.setStatusName(Utiliies.convertStatusUser(item.getStatus()));
            customers.add(item);
        }
        response.setTotalItems(list.size());
        response.setTotalPages(Math.round(list.size()/size));
        response.setCustomers(customers);
        log.info("CustomerService-ListAll :: End");
        return response;
    }

    @Override
    public CreateCustomerAccountResponse save(@Valid CustomerAccontRequest request, BindingResult bindingResult) {
        log.info("CustomerService-save :: Start");
        CreateCustomerAccountResponse response = new CreateCustomerAccountResponse();
        String language = "";
        if(bindingResult.hasErrors()) {
            log.error("CustomerService-save :: error blank");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            return response;
        }

        List<VUser> users = userRepo.findAll();
        for (VUser user: users) {
            if (user.getNumberPhone().equals(request.getNumberPhone())) {
                log.error("CustomerService-save :: error number phone");
                response.getStatus().setStatus(Status.Fail);
                response.getStatus().setMessage(MessageUtils.get(language, "msg.numberphone.exits"));
                return response;
            }

            if (user.getEmail().equals(request.getEmail())) {
                log.error("CustomerService-save :: error email");
                response.getStatus().setStatus(Status.Fail);
                response.getStatus().setMessage(MessageUtils.get(language, "msg.email.exits"));
                return response;
            }

            if (user.getCccd().equals(request.getCccd())) {
                log.error("CustomerService-save :: error cccd");
                response.getStatus().setStatus(Status.Fail);
                response.getStatus().setMessage(MessageUtils.get(language, "msg.cccd.exits"));
                return response;
            }

            if (user.getUsername().equals(request.getUsername())) {
                log.error("CustomerService-save :: error username");
                response.getStatus().setStatus(Status.Fail);
                response.getStatus().setMessage(MessageUtils.get(language, "msg.username.exits"));
                return response;
            }
        }

        VUser user = new VUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullname(request.getFullname());
        user.setAddress(request.getAddress());
        user.setPassword(request.getPassword());
        user.setCccd(request.getCccd());
        user.setNumberPhone(request.getNumberPhone());
        user.setCreated(new Date());
        user.setStatus(1);
        VUser save = userRepo.save(user);
        VUserRole userRole = new VUserRole();
        userRole.setRoleId(2);
        userRole.setUserId(save.getId());
        userRoleRepo.save(userRole);

        response.setFullname(user.getFullname());
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.success"));
        log.info("CustomerService-save :: End");
        return response;
    }

    @Override
    public BaseResponse unlockAndLockAccount(int id, String language) {
        log.info("CustomerService-UnlockAndLockAccount :: Start");
        BaseResponse response = new BaseResponse();

        VUser customer = userRepo.findById(id).get();
        if (customer == null) {
            response.getStatus().setMessage(MessageUtils.get(language,"msg.username.delete"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }

        //User user = userRepo.getById(request.getIdCustomer());
        if (customer.getStatus() == 1) {
            customer.setStatus(0);
            userRepo.save(customer);
            response.getStatus().setMessage(MessageUtils.get(language," locked "+customer.getFullname()));
            response.getStatus().setStatus(Status.Success);
        } else {
            customer.setStatus(1);
            userRepo.save(customer);
            response.getStatus().setMessage(MessageUtils.get(language," unlocked "+customer.getFullname()));
            response.getStatus().setStatus(Status.Success);
        }
        log.info("CustomerService-UnlockAndLockAccount :: End");
        return response;
    }

    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
