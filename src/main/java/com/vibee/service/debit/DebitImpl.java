package com.vibee.service.debit;

import com.vibee.entity.*;
import com.vibee.model.ObjectResponse.DebitObjectPayResponse;
import com.vibee.model.ObjectResponse.DetailBillOfDetailDebit;
import com.vibee.model.ObjectResponse.GetDebitOfUserObject;
import com.vibee.model.Status;
import com.vibee.model.item.BillItems;
import com.vibee.model.item.DebitItems;
import com.vibee.model.item.DebitUserItems;
import com.vibee.model.item.PayItems;
import com.vibee.model.request.debit.DebitPageRequest;
import com.vibee.model.request.debit.DebitRequest;
import com.vibee.model.request.debit.ListPayRequest;
import com.vibee.model.request.debit.PayRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.bill.GetTopTen;
import com.vibee.model.response.debit.DebitDetailResponse;
import com.vibee.model.response.debit.DebitItemsResponse;
import com.vibee.model.response.debit.DebitOfUserResponse;
import com.vibee.model.response.debit.GetDetailBill;
import com.vibee.repo.*;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.ProductUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DebitImpl {
    private final DebitRepository debitRepository;
    private final DebitDetailRepository debitDetailRepository;
    private final VBillRepo billRepo;
    private final VDetailBillRepo detailBillRepo;
    private final PayRepository payRepository;
    private final VUnitRepo vUnitRepo;
    private final VUserRepo vUserRepo;

    @Autowired
    public DebitImpl(DebitRepository debitRepository, DebitDetailRepository debitDetailRepository, VBillRepo billRepo, VDetailBillRepo detailBillRepo, PayRepository payRepository, VUnitRepo vUnitRepo, VUserRepo vUserRepo) {
        this.debitRepository = debitRepository;
        this.debitDetailRepository = debitDetailRepository;
        this.billRepo = billRepo;
        this.detailBillRepo = detailBillRepo;
        this.payRepository = payRepository;
        this.vUnitRepo = vUnitRepo;
        this.vUserRepo = vUserRepo;
    }

    public BigDecimal totalAmountOwed(ListPayRequest total) {
        Double sum = Double.valueOf(0);
        for (PayRequest getDetailBill : total.getData()) {
            sum += getDetailBill.getInPrice().doubleValue();
        }
        return BigDecimal.valueOf(sum);
    }

    public BaseResponse createDebit(DebitRequest request, BindingResult bindingResult) {

        BaseResponse response = new BaseResponse();
        VDebit debit = new VDebit();

        String creatorDebtorRequest = "Liên nè";
        String fullNameRequest = request.getFullName();
        String phoneNumberRequest = request.getPhoneNumber();
        BigDecimal totalAmountOwedRequest = request.getTotalAmountOwed();
        String creatorPayerRequest = request.getCreatorPayer();
        int billIdRequest = request.getBillId();
        String addressRequest = request.getAddress();
        String language = request.getLanguage();
        String description = request.getDescription();

        if (bindingResult.hasErrors()) {
            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
        } else {
            VBill bill = this.billRepo.findById(billIdRequest);
            if (bill == null) {
                response.getStatus().setMessage(MessageUtils.get(language, "Error 87"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            }
            if (totalAmountOwedRequest.doubleValue() > bill.getPrice().doubleValue()) {
                response.getStatus().setMessage(MessageUtils.get(language, "Error 91"));
                response.getStatus().setStatus(Status.Fail);
                return response;

            }
            VDebit debitContainerBill = this.debitRepository.findByBillId(bill.getId());
            if (debitContainerBill != null) {
                response.getStatus().setMessage(MessageUtils.get(language, "Bạn đã thêm cho sp này"));
                response.getStatus().setStatus(Status.Fail);
                return response;
            } else {

                VUser user = this.vUserRepo.findByNumberPhone(phoneNumberRequest);
                if (user != null) {
                    user.setAddress(addressRequest);
                    user.setFullname(fullNameRequest);
                    user.setNumberPhone(user.getNumberPhone());
                    user.setStatus(4);
                    user = this.vUserRepo.save(user);
                    debit.setUserId(user.getId());
                } else {
                    VUser vUserNew = new VUser();
                    vUserNew.setAddress(addressRequest);
                    vUserNew.setFullname(fullNameRequest);
                    vUserNew.setNumberPhone(phoneNumberRequest);
                    vUserNew.setStatus(4);
                    vUserNew = this.vUserRepo.save(vUserNew);
                    debit.setUserId(vUserNew.getId());
                }
                debit.setDebitDate(new Date());
                debit.setCreatorDebtor(creatorDebtorRequest);
                debit.setTotalAmountOwed(totalAmountOwedRequest);
                debit.setCreatorPayer(creatorPayerRequest);
                debit.setFullName(fullNameRequest);
                debit.setAddress(addressRequest);
                debit.setBillId(billIdRequest);
                debit.setStatus(2);
                debit.setTypeOfDebtor(1);
                debit.setDescription(description);
                debit.setPhoneNumber(phoneNumberRequest);
                this.debitRepository.save(debit);

                response.getStatus().setMessage(MessageUtils.get(language, "success"));
                response.getStatus().setStatus(Status.Success);
                return response;
            }
        }
        return response;
    }

    public BaseResponse updateDebit(int idDebit, DebitRequest request, BindingResult bindingResult) {

        BaseResponse response = new BaseResponse();

        String creatorDebtorRequest = "Liên nè";
        String fullNameRequest = request.getFullName();
        String phoneNumberRequest = request.getPhoneNumber();
        BigDecimal totalAmountOwedRequest = request.getTotalAmountOwed();
        String creatorPayerRequest = request.getCreatorPayer();
        int billIdRequest = request.getBillId();
        String addressRequest = request.getAddress();
        String language = request.getLanguage();
        String description = request.getDescription();

        if (bindingResult.hasErrors()) {
            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
        } else {
            VBill bill = this.billRepo.findById(billIdRequest);
            if (bill == null) {
                response.getStatus().setMessage(MessageUtils.get(language, "Error 87"));
                response.getStatus().setStatus(Status.Fail);
            }
            if (totalAmountOwedRequest.doubleValue() > bill.getPrice().doubleValue()) {
                response.getStatus().setMessage(MessageUtils.get(language, "Error 91"));
                response.getStatus().setStatus(Status.Fail);
            }
            else {
                VDebit debit = this.debitRepository.findById(idDebit);
                VUser user = this.vUserRepo.findById(debit.getUserId());
                user.setAddress(addressRequest);
                user.setFullname(fullNameRequest);
                user.setNumberPhone(phoneNumberRequest);
                user.setStatus(4);
                user = this.vUserRepo.save(user);
                debit.setUserId(user.getId());

                debit.setDebitDate(new Date());
                debit.setCreatorDebtor(creatorDebtorRequest);
                debit.setTotalAmountOwed(totalAmountOwedRequest);
                debit.setCreatorPayer(creatorPayerRequest);
                debit.setFullName(fullNameRequest);
                debit.setAddress(addressRequest);
                debit.setBillId(billIdRequest);
                debit.setStatus(2);
                debit.setTypeOfDebtor(1);
                debit.setPhoneNumber(phoneNumberRequest);
                debit.setDescription(description);
                this.debitRepository.save(debit);

                response.getStatus().setMessage(MessageUtils.get(language, "success"));
                response.getStatus().setStatus(Status.Success);
            }
        }
        return response;
    }

    public BaseResponse payDebit(int idDebit, PayRequest request, BindingResult bindingResult) {

        BaseResponse response = new BaseResponse();

        String creator = "Liên nè";
        String language = request.getLanguage();

        if (bindingResult.hasErrors()) {
            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
            response.getStatus().setStatus(Status.Fail);
        } else {
            VDebit debit = this.debitRepository.findById(idDebit);
            if (debit == null) {
                response.getStatus().setMessage(MessageUtils.get(language, "Error 161"));
                response.getStatus().setStatus(Status.Fail);
            }
            if (debit.getTotalAmountOwed().doubleValue() < request.getInPrice().doubleValue()) {
                response.getStatus().setMessage(MessageUtils.get(language, "Error 91"));
                response.getStatus().setStatus(Status.Fail);
            } else {
                List<VPay> pays = this.payRepository.findByDebitId(idDebit);
                DebitObjectPayResponse getPay = this.payRepository.getPay(debit.getId());
                VPay pay = new VPay();
                if (pays.size() == 0) {
                    if (bindingResult.hasErrors()) {
                        response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
                        response.getStatus().setStatus(Status.Fail);
                    }
                    if (debit.getTotalAmountOwed().doubleValue() < request.getInPrice().doubleValue()) {
                        response.getStatus().setMessage(MessageUtils.get(language, "Failed"));
                        response.getStatus().setStatus(Status.Fail);
                    } else if (debit.getTotalAmountOwed().doubleValue() == request.getInPrice().doubleValue()) {
                        pay.setStatus(1);
                        debit.setStatus(1);
                    }
                    else {
                        pay.setStatus(2);
                        debit.setStatus(2);
                    }

                    pay.setPrice(BigDecimal.valueOf(debit.getTotalAmountOwed().doubleValue() - request.getInPrice().doubleValue()));
                    pay.setIn_Price(request.getInPrice());
                    pay.setNumberOfPayOuts(1);
                    pay.setActualDateOfPaymentOfDebt(new Date());
                    pay.setCreator(creator);
                    pay.setDebitId(debit.getId());
                    this.payRepository.save(pay);
                    this.debitRepository.save(debit);

                    response.getStatus().setMessage(MessageUtils.get(language, "Success"));
                    response.getStatus().setStatus(Status.Success);

                } else {

                    VPay getNewDate = this.payRepository.getNewDate(debit.getId());
                    VPay payNumberOfPayOuts_1 = new VPay();
                    if (bindingResult.hasErrors()) {
                        response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
                        response.getStatus().setStatus(Status.Fail);
                    }
                    if (getNewDate.getPrice().doubleValue() == 0 || getNewDate.getPrice().equals("0.00")|| getNewDate.getPrice()== BigDecimal.valueOf(0)) {

                        response.getStatus().setMessage(MessageUtils.get(language, "Bạn đã thanh toán đầy đủ 1"));
                        response.getStatus().setStatus(Status.Success);
                    }
                    if (getNewDate.getStatus() == 1) {
                        response.getStatus().setMessage(MessageUtils.get(language, "Bạn đã thanh toán đầy đủ 1"));
                        response.getStatus().setStatus(Status.Success);
                    }
                    if (debit.getTotalAmountOwed().doubleValue() < getPay.getPrice().doubleValue()) {
                        response.getStatus().setMessage(MessageUtils.get(language, "Failed"));
                        response.getStatus().setStatus(Status.Fail);
                    }
                   else if (getPay.getInPrice() == debit.getTotalAmountOwed()) {

                        payNumberOfPayOuts_1.setStatus(1);
                        debit.setStatus(1);

                        response.getStatus().setMessage(MessageUtils.get(language, "Bạn đã thanh toán đầy đủ"));
                        response.getStatus().setStatus(Status.Success);
                    }
                   else {
                        payNumberOfPayOuts_1.setStatus(2);
                        debit.setStatus(2);
                    }

                    payNumberOfPayOuts_1.setPrice(BigDecimal.valueOf(getNewDate.getPrice().doubleValue() - request.getInPrice().doubleValue()));
                    payNumberOfPayOuts_1.setNumberOfPayOuts(getNewDate.getNumberOfPayOuts() + 1);
                    payNumberOfPayOuts_1.setActualDateOfPaymentOfDebt(new Date());
                    payNumberOfPayOuts_1.setDebitId(debit.getId());
                    payNumberOfPayOuts_1.setIn_Price(request.getInPrice());

                    this.payRepository.save(payNumberOfPayOuts_1);
                    this.debitRepository.save(debit);

                    response.getStatus().setMessage(MessageUtils.get(language, "Success"));
                    response.getStatus().setStatus(Status.Success);
                }
            }
        }
        return response;
    }

    public List<GetDetailBill> getDetailBill(int bill) {

        DebitRequest getDetailBills = new DebitRequest();
        List<GetDetailBill> getDetailBillList = new ArrayList<>();
        List<DetailBillOfDetailDebit> findByProductByBill = this.detailBillRepo.findByProductByBill(bill);
        for (DetailBillOfDetailDebit detailBill : findByProductByBill) {
            GetDetailBill response = new GetDetailBill();
            response.setProductName(detailBill.getProductName());
            response.setUnitId(detailBill.getUnitId());
            response.setAmount(detailBill.getAmountDetailBill());
            VUnit vUnit = this.vUnitRepo.findById(detailBill.getUnitId());
            response.setUnitName(vUnit.getUnitName());
            response.setPrice(detailBill.getPriceDetailBill());
            getDetailBillList.add(response);
//            getDetailBills.setDebitItems(getDetailBillList);
        }
        return getDetailBillList;
    }

    public GetTopTen getBill(){
        List<VBill> getTopTen = this.billRepo.getTopTen();
        List<BillItems> items = new ArrayList<>();
        GetTopTen response = new GetTopTen();
        for (VBill vBill: getTopTen){
            BillItems billItems = new BillItems();
            billItems.setTotal(vBill.getPrice());
            billItems.setId(vBill.getId());
            billItems.setCreateDate(vBill.getCreatedDate());
            items.add(billItems);
        }
        response.setItems(items);
        return response;
    }

    public DebitOfUserResponse listUserDebit(DebitPageRequest request) {
        int pageNumber = request.getPageNumber();
        int pageSize = request.getPageSize();
        String searchText = request.getSearchText();
        String typeFilter = request.getFilter().getTypeFilter();
        String valueFilter = request.getFilter().getValueFilter();
        String language = request.getLanguage();
        DebitOfUserResponse response = new DebitOfUserResponse();
        int totalPage = 0;

        Page<VDebit> findAll = null;
        if (typeFilter.equals("none") && valueFilter.equals("none")) {
            if (searchText.equals("") || searchText == null) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("status"));
                findAll = this.debitRepository.findAll(pageable);
                totalPage = findAll.getTotalPages();
            } else {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("status"));
                findAll = this.debitRepository.findByFullNameLikeOrPhoneNumberLike("%"+searchText +"%","%"+searchText +"%", pageable);
                totalPage = findAll.getTotalPages();
            }

        } else {
            if (!typeFilter.equals("id")  && !typeFilter.equals("fullName") && !typeFilter.equals("totalAmountOwed") &&
                    !typeFilter.equals("status") && !typeFilter.equals("phoneNumber")&& !typeFilter.equals("debitDate") ) {
                response.getStatus().setStatus(Status.Fail);
                response.getStatus().setMessage(MessageUtils.get(language, "error.invalid.filter"));
            }
            Pageable pageable = null;
            if (valueFilter.equals("asc")) {
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(typeFilter).ascending());
            } else {
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(typeFilter).descending());
            }
            if (searchText.equals("") || searchText == null) {
                findAll = this.debitRepository.findAll(pageable);
                totalPage = findAll.getTotalPages();

            } else {
                findAll = this.debitRepository.findByFullNameLikeOrPhoneNumberLike("%"+searchText +"%", "%"+searchText +"%", pageable);
                totalPage = findAll.getTotalPages();
            }

        }

        response.setItems(this.convertEntityToResponseOfPayAndDebit(findAll.getContent(), language));
        response.setPage(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalPages(totalPage);
        response.setTotalItems((int) findAll.getTotalElements());
        response.getStatus().setMessage(MessageUtils.get(language, "Success"));
        response.getStatus().setStatus(Status.Success);
        return response;

    }

    public DebitItemsResponse findAll(int idUser, DebitPageRequest request) {
        int pageNumber = request.getPageNumber();
        int pageSize = request.getPageSize();
        String searchText = request.getSearchText();
        String typeFilter = request.getFilter().getTypeFilter();
        String valueFilter = request.getFilter().getValueFilter();
        String language = request.getLanguage();
        DebitItemsResponse response = new DebitItemsResponse();
        int totalPage = 0;
        VUser vUser = this.vUserRepo.findById(idUser);

        Page<VDebit> findAll = null;
        if (typeFilter.equals("none") && valueFilter.equals("none")) {
            if (searchText.equals("") || searchText == null) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("status"));
                findAll = this.debitRepository.findByUserId(vUser.getId(), pageable);
                totalPage = findAll.getTotalPages();
            } else {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("status"));
//                findAll = this.debitRepository.findByFullNameOrPhoneNumberContainingIgnoreCase(searchText, searchText, pageable);
                totalPage = findAll.getTotalPages();
            }

        } else {
            if (!typeFilter.equals("id") && !typeFilter.equals("address") && !typeFilter.equals("fullname") && !typeFilter.equals("creator") &&
                    !typeFilter.equals("price") && !typeFilter.equals("debitDate") && !typeFilter.equals("phoneNumber") && !typeFilter.equals("status")) {
                response.getStatus().setStatus(Status.Fail);
                response.getStatus().setMessage(MessageUtils.get(language, "error.invalid.filter"));
            }
            Pageable pageable = null;
            if (valueFilter.equals("asc")) {
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(typeFilter).ascending());
            } else {
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(typeFilter).descending());
            }
            if (searchText.equals("") || searchText == null) {
                findAll = this.debitRepository.findByUserId(vUser.getId(), pageable);
                totalPage = findAll.getTotalPages();

            } else {
//                findAll = this.debitRepository.findByFullNameOrPhoneNumberContainingIgnoreCase(searchText, searchText, pageable);
                totalPage = findAll.getTotalPages();
            }

        }
        response.setItems(this.convertEntityToResponse(findAll.getContent(), language));
        response.setPage(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalPages(totalPage);
        response.setTotalItems((int) findAll.getTotalElements());
        response.getStatus().setMessage(MessageUtils.get(language, "Success"));
        response.getStatus().setStatus(Status.Success);
        return response;

    }

    public DebitDetailResponse findByIdDebitOfDebitDetail(int idDebit) {
        VDebit debit = this.debitRepository.findById(idDebit);
        List<GetDetailBill> debitItems = new ArrayList<>();
        DebitDetailResponse response = new DebitDetailResponse();
        if (debit == null) {
            return null;
        } else {

                VUser user = this.vUserRepo.findById(debit.getUserId());
                VBill vBill = this.billRepo.findById(debit.getBillId());

                response.setFullName(debit.getFullName());
                response.setTotalAmountOwed(debit.getTotalAmountOwed());
                response.setBillId(debit.getBillId());
                response.setAddress(user.getAddress());
                response.setPhoneNumber(debit.getPhoneNumber());
                response.setTypeOfDebtor(debit.getTypeOfDebtor());
                response.setIdDebit(debit.getId());
                response.setDescription(debit.getDescription());
                response.setTotal(vBill.getPrice());
                response.setCreateDate(vBill.getCreatedDate());
        }
        return response;
    }

    private List<DebitItems> convertEntityToResponse(List<VDebit> debits, String language) {
        List<DebitItems> debitItemsList = new ArrayList<>();

        for (VDebit debit : debits) {
            DebitItems items = new DebitItems();
            VUser user = this.vUserRepo.findById(debit.getUserId());

            items.setId(debit.getId());
            items.setDebitDate(debit.getDebitDate());
            items.setCreatorDebtor(debit.getCreatorDebtor());
            items.setFullName(user.getFullname());
            items.setPhoneNumber(user.getNumberPhone());
            items.setTotalAmountOwed(debit.getTotalAmountOwed());
            items.setCreatorPayer(debit.getCreatorPayer());
            items.setStatusCode(ProductUtils.convertStatus(debit.getStatus(), language));
            items.setBillId(debit.getBillId());
            items.setAddress(user.getAddress());
            items.setTypeOfDebtor(ProductUtils.convertTypeOfDebtor(debit.getTypeOfDebtor(), language));
            debitItemsList.add(items);
        }
        return debitItemsList;
    }
    private List<DebitUserItems> convertEntityToResponseOfPayAndDebit(List<VDebit> debits, String language) {
        List<DebitUserItems> debitItemsList = new ArrayList<>();
        List<PayItems> payItems = new ArrayList<>();

        for (VDebit debit : debits) {
            DebitUserItems items = new DebitUserItems();
            items.setId(debit.getId());
            items.setDebtDate(debit.getDebitDate());
            items.setCreator(debit.getCreatorDebtor());
            items.setFullName(debit.getFullName());
            items.setPhoneNumber(debit.getPhoneNumber());
            items.setTotal(debit.getTotalAmountOwed());
            items.setStatusCode(ProductUtils.convertStatus(debit.getStatus(), language));
            items.setIdBill(debit.getBillId());
            items.setTypeDebt(ProductUtils.convertTypeOfDebtor(debit.getTypeOfDebtor(), language));

            List<VPay> getAllPay = this.payRepository.findByDebitId(debit.getId());
            if(getAllPay!=null){
                for (VPay vPay: getAllPay){
                    PayItems itemsPay = new PayItems();
                    itemsPay.setCreator(vPay.getCreator());
                    itemsPay.setNumberOfPayOuts(vPay.getNumberOfPayOuts());
                    itemsPay.setId(vPay.getId());
                    itemsPay.setPrice(vPay.getPrice());
                    itemsPay.setIdDebt(vPay.getDebitId());
                    itemsPay.setDatePayment(vPay.getActualDateOfPaymentOfDebt());
                    itemsPay.setInPrice(vPay.getIn_Price());
                    itemsPay.setStatusCode(ProductUtils.convertStatus(debit.getStatus(), language));
                    payItems.add(itemsPay);

                }
            }else {

            }
            items.setPayItems(payItems);
            debitItemsList.add(items);
        }
        return debitItemsList;
    }


}
