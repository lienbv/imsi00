package com.vibee.service.vsupplier.impl;

import com.vibee.entity.VSupplier;
import com.vibee.model.Status;
import com.vibee.model.item.SupplierItem;
import com.vibee.model.request.v_supplier.CreateSupplierRequest;
import com.vibee.model.request.v_supplier.UpdateSupplierRequste;
import com.vibee.model.response.v_supplier.CreateSupplierResponse;
import com.vibee.model.response.v_supplier.DeleteSuplierResponse;
import com.vibee.model.response.v_supplier.ListSupplierResponse;
import com.vibee.model.response.v_supplier.UpdateSuplierResponse;
import com.vibee.repo.VBillRepo;
import com.vibee.repo.VDetailBillRepo;
import com.vibee.repo.VSupplierRepo;
import com.vibee.repo.VUserRepo;
import com.vibee.service.vsupplier.SupplierService;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    private VSupplierRepo supplierRepo;

    @Autowired
    private VUserRepo userRepo;

    @Override
    public CreateSupplierResponse createSup(CreateSupplierRequest request, BindingResult bindingResult, String language) {
        CreateSupplierResponse response = new CreateSupplierResponse();
        List<VSupplier> supplierList = supplierRepo.findAll();

        language = request.getLanguage();
        String nameSup = request.getNameSup();
        //String creator = SecurityUtil.getPrincipal().getUsername();
        String address = request.getAddress();
        String email = request.getEmail();
        String numberPhone = request.getNumberPhone();

        if (bindingResult.hasErrors()) {
            response.getStatus()
                    .setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }

        for (VSupplier sup : supplierList) {
            if (sup.getEmail().equals(email) && sup.getStatus() != 3) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.email.already.exist"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }
            if (sup.getNumberPhone().equals(numberPhone)  && sup.getStatus() != 3) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.phone.already.exist"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }

            Pattern pattern = Pattern.compile("(84|0[3|5|7|8|9])+([0-9]{8})");
            numberPhone = numberPhone.replaceAll("[\\-\\+]", "");
            Matcher matcher = pattern.matcher(numberPhone);
            if (!matcher.matches()) {
                response.getStatus().setMessage(MessageUtils.get(language, "msg.phone.format"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }
        }

        VSupplier crtsup = new VSupplier();

        crtsup.setNameSup(nameSup.trim());
        //crtsup.setCreator(creator);
        crtsup.setCreatedDate(new Date());
        crtsup.setAddress(address.trim());
        crtsup.setEmail(email.trim());
        crtsup.setNumberPhone(numberPhone);
        crtsup.setStatus(Integer.parseInt(Status.Success));

        VSupplier sup = supplierRepo.save(crtsup);

        if (sup == null) {
            log.error("Create supplier is failed");
            response.getStatus().setMessage(MessageUtils.get(language, "msg.createsuplier.failse"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        response.setId(sup.getId());
        response.setNameSup(sup.getNameSup().trim());
        response.getStatus().setMessage(MessageUtils.get(language, "msg.createsuplier.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("createsuplierService :: End");
        return response;
    }

    @Override
    public UpdateSuplierResponse UpdateSup(UpdateSupplierRequste request, BindingResult bindingResult, String language) {
        UpdateSuplierResponse response = new UpdateSuplierResponse();
        List<VSupplier> supplierList = supplierRepo.findAll();

        language = request.getLanguage();
        int id = request.getId();
        String nameSup = request.getNameSup();
        String address = request.getAddress();
        String email = request.getEmail();
        String numberPhone = request.getNumberPhone();
        VSupplier crtsup = null;

        if (bindingResult.hasErrors()) {
            response.getStatus()
                    .setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }

        // check SĐT
        Pattern pattern = Pattern.compile("(84|0[3|5|7|8|9])+([0-9]{8})");
        numberPhone = numberPhone.replaceAll("[\\-\\+]", "");
        Matcher matcher = pattern.matcher(numberPhone);
        if (!matcher.matches()) {
            response.getStatus().setMessage(MessageUtils.get(language, "msg.phone.format"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        for (VSupplier supplier : supplierList) {
            if (supplier.getId() == id) {
                crtsup = supplier;
                // Supplier emailID = supplierRepo.findById(id).get();
            }

        }
        if(crtsup != null) {
            for (VSupplier sup : supplierList) {
                // Check email đã tồn tại trong list
                if (!crtsup.getEmail().equals(sup.getEmail())  && sup.getStatus() != 3) {
                    if (sup.getEmail().equals(email)) {
                        response.getStatus().setMessage(MessageUtils.get(language, "msg.exit.email"));
                        response.getStatus().setStatus(Status.Fail);
                        return response;
                    }
                }
                // Check SĐT đã tồn tại trong list
                if (!crtsup.getNumberPhone().equals(sup.getNumberPhone())  && sup.getStatus() != 3) {
                    if (sup.getNumberPhone().equals(numberPhone)) {
                        response.getStatus().setMessage(MessageUtils.get(language, "msg.exit.phoneNumber"));
                        response.getStatus().setStatus(Status.Fail);
                        return response;
                    }
                }
            }

            crtsup = supplierRepo.findById(id).get();
            crtsup.setNameSup(nameSup.trim());
            crtsup.setAddress(address.trim());
            crtsup.setEmail(email.trim());
            crtsup.setNumberPhone(numberPhone.trim());

            VSupplier sup = supplierRepo.save(crtsup);
            response.setId(sup.getId());
            response.setNameSup(sup.getNameSup().trim());
            response.getStatus().setMessage(MessageUtils.get(language, "msg.update.success"));
            response.getStatus().setStatus(Status.Success);
            log.info("Update Supplier :: End");
            return response;
        }
        response.getStatus().setMessage(MessageUtils.get(language, "msg.update.failed"));
        response.getStatus().setStatus(Status.Success);
        log.info("Update Supplier :: End");
        return response;
    }

    @Override
    public DeleteSuplierResponse lockAnhUnLock(String language, int id) {
        DeleteSuplierResponse response = new DeleteSuplierResponse();
        log.info("DeleteUserService :: Start");
        VSupplier vSupplier = supplierRepo.getById(id);
        if (vSupplier.getStatus() == 2) {
            vSupplier.setStatus(1);
            supplierRepo.save(vSupplier);
        } else {
            vSupplier.setStatus(2);
            supplierRepo.save(vSupplier);
        }
        response.getStatus().setMessage(MessageUtils.get(language, "msg.delete.supplier.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("DeleteUserService :: End");
        return response;
    }

    @Override
    public DeleteSuplierResponse delete(String language, int id) {
        log.info("DeleteUserService :: Start");
        DeleteSuplierResponse response = new DeleteSuplierResponse();
        VSupplier vSupplier = supplierRepo.getById(id);
        vSupplier.setStatus(3);
        supplierRepo.save(vSupplier);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.delete.supplier.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("DeleteUserService :: End");
        return null;
    }

    private final static String ASC = "asc";
    private final static String DESC = "desc";

    private static Map<Integer, String> typeFilter = new HashMap<Integer, String>();

    static {
        typeFilter.put(0, "nameSup");
        typeFilter.put(1, "numberPhone");
        typeFilter.put(2, "email");
        typeFilter.put(3, "createdDate");
        typeFilter.put(4, "address");
    }

    @Override
    public ListSupplierResponse displaySupplier(int status, String nameSup, String language, String name, String phoneNumber, String email, String createDate, String address, int pageNumber, int size) {
        log.info("displaySupplier :: Start");
        List<String> filters = new ArrayList<>();
        filters.add(name);
        filters.add(phoneNumber);
        filters.add(email);
        filters.add(createDate);
        filters.add(address);

        ListSupplierResponse response = new ListSupplierResponse();
        Pageable pageable = PageRequest.of(pageNumber, size);

        for (int i = 0; i < filters.size(); i++) {
            String filter = filters.get(i);
            if (filter.equalsIgnoreCase(ASC)) {
                pageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, typeFilter.get(i)));
            } else if (filter.equalsIgnoreCase(DESC)) {
                pageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, typeFilter.get(i)));
            }
        }
        List<VSupplier> supplierList = new ArrayList<>();
        List<VSupplier> suppliers = new ArrayList<>();
        List<VSupplier> suppliersActive = supplierRepo.findBySuppliers("%" + nameSup + "%", 1);
        if (status == 0) {
            supplierList = supplierRepo.findBySuppliers("%" + nameSup + "%", pageable);
            suppliers = supplierRepo.findBySuppliers("%" + nameSup + "%");
        } else {
            supplierList = supplierRepo.findBySuppliers(status,"%" + nameSup + "%", pageable);
            suppliers = supplierRepo.findBySuppliers(status,"%" + nameSup + "%");
        }


        List<SupplierItem> supplierItems = new ArrayList<>();
        for (VSupplier supplier: supplierList) {
            SupplierItem item = new SupplierItem();
            item.setId(supplier.getId());
            item.setAddress(supplier.getAddress());
            item.setEmail(supplier.getEmail());
            item.setNameSup(supplier.getNameSup());
            item.setCreator(supplier.getCreator());
            item.setNumberPhone(supplier.getNumberPhone());
            item.setStatus(supplier.getStatus());
            item.setCreatedDate(supplier.getCreatedDate());
            item.setStatusName(Utiliies.convertStatusSupplier(supplier.getStatus(),language));
            supplierItems.add(item);
        }
        response.setTotalItems(suppliers.size());
        response.setTotalPages(Math.round(suppliers.size()/size));
        response.setSupplierActive(suppliersActive.size());
        response.setSupplierItems(supplierItems);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("displaySupplier :: End");
        return response;
    }
}
