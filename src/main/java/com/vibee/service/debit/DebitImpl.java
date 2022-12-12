package com.vibee.service.debit;

import com.vibee.entity.*;
import com.vibee.model.ObjectResponse.DebitObjectPayResponse;
import com.vibee.model.ObjectResponse.DetailBillOfDetailDebit;
import com.vibee.model.ObjectResponse.GetDebitOfUserObject;
import com.vibee.model.Status;
import com.vibee.model.item.DebitItems;
import com.vibee.model.item.DebitUserItems;
import com.vibee.model.request.debit.DebitPageRequest;
import com.vibee.model.request.debit.DebitRequest;
import com.vibee.model.request.debit.ListPayRequest;
import com.vibee.model.request.debit.PayRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.debit.DebitDetailResponse;
import com.vibee.model.response.debit.DebitItemsResponse;
import com.vibee.model.response.debit.DebitOfUserResponse;
import com.vibee.model.response.debit.GetDetailBill;
import com.vibee.repo.*;
import com.vibee.utils.DataUtils;
import com.vibee.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy");

        String creatorDebtorRequest = "Liên nè";
        String fullNameRequest = request.getFullName();
        String phoneNumberRequest = request.getPhoneNumber();
        BigDecimal totalAmountOwedRequest = request.getTotalAmountOwed();
        String creatorPayerRequest = request.getCreatorPayer();
        int billIdRequest = request.getBillId();
        String addressRequest = request.getAddress();
        int typeOfDebtorRequest = request.getTypeOfDebtor();
        String expectedDateOfPaymentOfDebtRequest = request.getExpectedDateOfPaymentOfDebt();

        String dateInput = null;
        try {
            dateInput = DataUtils.modifyDateLayout(expectedDateOfPaymentOfDebtRequest);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date date = null;
        try {
            date = sdfInput.parse(dateInput);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String language = request.getLanguage();

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
            VDebit debitContainerBill = this.debitRepository.findByBillId(bill.getId());
            if (debitContainerBill != null) {
                response.getStatus().setMessage(MessageUtils.get(language, "Bạn đã thêm cho sp này"));
                response.getStatus().setStatus(Status.Fail);
            } else {

                VUser user = this.vUserRepo.findByNumberPhone(phoneNumberRequest);
                if (user != null) {
                    user.setAddress(addressRequest);
                    user.setFullname(fullNameRequest);
                    user.setNumberPhone(user.getNumberPhone());
                    user.setStatus(2);
                    user = this.vUserRepo.save(user);
                    debit.setUserId(user.getId());
                } else {
                    VUser vUserNew = new VUser();
                    vUserNew.setAddress(addressRequest);
                    vUserNew.setFullname(fullNameRequest);
                    vUserNew.setNumberPhone(phoneNumberRequest);
                    vUserNew.setStatus(2);
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
                debit.setStatus(1);
                debit.setTypeOfDebtor(typeOfDebtorRequest);
                if (expectedDateOfPaymentOfDebtRequest == null) {
                    debit.setExpectedDateOfPaymentOfDebt(null);
                } else {
                    debit.setExpectedDateOfPaymentOfDebt(date);
                }
                debit = this.debitRepository.save(debit);

                List<VDebitDetail> debitDetails = new ArrayList<>();
                List<GetDetailBill> debitItems = request.getDebitItems();

                for (GetDetailBill items : debitItems) {
                    VDebitDetail debitDetail = new VDebitDetail();
                    if (items.getInPrice() == null || items.getInPrice().equals("")) {
                        return null;
                    } else if (items.getInPrice().doubleValue() > items.getPrice().doubleValue()) {
                        response.getStatus().setMessage(MessageUtils.get(language, "Failed"));
                        response.getStatus().setStatus(Status.Fail);
                    } else {
                        debitDetail.setAmount(items.getAmount());
                        debitDetail.setPrice(items.getPrice());
                        debitDetail.setProductName(items.getProductName());
                        debitDetail.setUnitId(items.getUnitId());
                        debitDetail.setDebitId(debit.getId());
                        debitDetail.setStatus(1);
                        debitDetails.add(debitDetail);
                        this.debitDetailRepository.saveAll(debitDetails);
                    }
                }
                response.getStatus().setMessage(MessageUtils.get(language, "success"));
                response.getStatus().setStatus(Status.Success);
            }
        }
        return response;
    }

    public BaseResponse updateDebit(int idDebit, DebitRequest request, BindingResult bindingResult) {

        BaseResponse response = new BaseResponse();

        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy");

        String creatorDebtorRequest = "Liên nè";
        String fullNameRequest = request.getFullName();
        String phoneNumberRequest = request.getPhoneNumber();
        BigDecimal totalAmountOwedRequest = request.getTotalAmountOwed();
        String creatorPayerRequest = request.getCreatorPayer();
        int billIdRequest = request.getBillId();
        String addressRequest = request.getAddress();
        int typeOfDebtorRequest = request.getTypeOfDebtor();
        String expectedDateOfPaymentOfDebtRequest = request.getExpectedDateOfPaymentOfDebt();

        String dateInput = null;
        try {
            dateInput = DataUtils.modifyDateLayout(expectedDateOfPaymentOfDebtRequest);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date date = null;
        try {
            date = sdfInput.parse(dateInput);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String language = request.getLanguage();

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
                VUser user = this.vUserRepo.findById(debit.getId());
                user.setAddress(addressRequest);
                user.setFullname(fullNameRequest);
                user.setNumberPhone(phoneNumberRequest);
                user.setStatus(2);
                user = this.vUserRepo.save(user);
                debit.setUserId(user.getId());

                debit.setDebitDate(new Date());
                debit.setCreatorDebtor(creatorDebtorRequest);
                debit.setTotalAmountOwed(totalAmountOwedRequest);
                debit.setCreatorPayer(creatorPayerRequest);
                debit.setFullName(fullNameRequest);
                debit.setAddress(addressRequest);
                debit.setBillId(billIdRequest);
                debit.setStatus(1);
                debit.setTypeOfDebtor(typeOfDebtorRequest);
                if (expectedDateOfPaymentOfDebtRequest == null) {
                    debit.setExpectedDateOfPaymentOfDebt(null);
                } else {
                    debit.setExpectedDateOfPaymentOfDebt(date);
                }
                debit = this.debitRepository.save(debit);

                List<VDebitDetail> debitDetails = new ArrayList<>();
                List<GetDetailBill> debitItems = request.getDebitItems();

                for (GetDetailBill items : debitItems) {
                    VDebitDetail debitDetail = new VDebitDetail();
                    if (items.getInPrice() == null || items.getInPrice().equals("")) {
                        return null;
                    } else if (items.getInPrice().doubleValue() > items.getPrice().doubleValue()) {
                        response.getStatus().setMessage(MessageUtils.get(language, "Failed"));
                        response.getStatus().setStatus(Status.Fail);
                    } else {
                        debitDetail.setAmount(items.getAmount());
                        debitDetail.setPrice(items.getPrice());
                        debitDetail.setProductName(items.getProductName());
                        debitDetail.setUnitId(items.getUnitId());
                        debitDetail.setDebitId(debit.getId());
                        debitDetail.setStatus(1);
                        debitDetails.add(debitDetail);
                        this.debitDetailRepository.saveAll(debitDetails);
                    }
                }
                response.getStatus().setMessage(MessageUtils.get(language, "success"));
                response.getStatus().setStatus(Status.Success);
            }
        }
        return response;
    }

    public BaseResponse payDebit(int idDebit, PayRequest request, BindingResult bindingResult) {

        BaseResponse response = new BaseResponse();

        String creatorDebtorRequest = "Liên nè";
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
                        pay.setPrice(BigDecimal.valueOf(debit.getTotalAmountOwed().doubleValue() - request.getInPrice().doubleValue()));
                        pay.setIn_Price(request.getInPrice());
                        pay.setStatus(2);
                        pay.setNumberOfPayOuts(1);
                        pay.setActualDateOfPaymentOfDebt(new Date());
                        pay.setDebitId(debit.getId());
                        this.payRepository.save(pay);
                    } else {
                        pay.setPrice(BigDecimal.valueOf(debit.getTotalAmountOwed().doubleValue() - request.getInPrice().doubleValue()));
                        pay.setIn_Price(request.getInPrice());
                        pay.setStatus(1);
                        pay.setNumberOfPayOuts(1);
                        pay.setActualDateOfPaymentOfDebt(new Date());
                        pay.setDebitId(debit.getId());
                        this.payRepository.save(pay);
                    }
                    response.getStatus().setMessage(MessageUtils.get(language, "Success"));
                    response.getStatus().setStatus(Status.Success);

                } else {

                    VPay getNewDate = this.payRepository.getNewDate(debit.getId());
                    VPay payNumberOfPayOuts_1 = new VPay();
                    if (bindingResult.hasErrors()) {
                        response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
                        response.getStatus().setStatus(Status.Fail);
                        return response;
                    }
                    if (getNewDate.getPrice().doubleValue() == 0 || getNewDate.getPrice().equals("0.00")) {
                        getNewDate.setStatus(2);
                        this.payRepository.save(getNewDate);
                        response.getStatus().setMessage(MessageUtils.get(language, "Bạn đã thanh toán đầy đủ 1"));
                        response.getStatus().setStatus(Status.Success);
                        return response;
                    }
                    if (getNewDate.getStatus() == 2) {
                        response.getStatus().setMessage(MessageUtils.get(language, "Bạn đã thanh toán đầy đủ 1"));
                        response.getStatus().setStatus(Status.Success);
                        return response;
                    }
                    if (debit.getTotalAmountOwed().doubleValue() < getNewDate.getPrice().doubleValue()) {
                        response.getStatus().setMessage(MessageUtils.get(language, "Failed"));
                        response.getStatus().setStatus(Status.Fail);
                        return response;
                    }
                    if (getPay.getInPrice() == debit.getTotalAmountOwed()) {

                        payNumberOfPayOuts_1.setPrice(BigDecimal.valueOf(getNewDate.getPrice().doubleValue() - request.getInPrice().doubleValue()));
                        payNumberOfPayOuts_1.setStatus(2);
                        payNumberOfPayOuts_1.setNumberOfPayOuts(getNewDate.getNumberOfPayOuts() + 1);
                        payNumberOfPayOuts_1.setActualDateOfPaymentOfDebt(new Date());
                        payNumberOfPayOuts_1.setDebitId(debit.getId());
                        payNumberOfPayOuts_1.setIn_Price(request.getInPrice());
                        this.payRepository.save(payNumberOfPayOuts_1);
                        response.getStatus().setMessage(MessageUtils.get(language, "Bạn đã thanh toán đầy đủ"));
                        response.getStatus().setStatus(Status.Success);
                        return response;
                    }

                    payNumberOfPayOuts_1.setPrice(BigDecimal.valueOf(getNewDate.getPrice().doubleValue() - request.getInPrice().doubleValue()));
                    payNumberOfPayOuts_1.setStatus(1);
                    payNumberOfPayOuts_1.setNumberOfPayOuts(getNewDate.getNumberOfPayOuts() + 1);
                    payNumberOfPayOuts_1.setActualDateOfPaymentOfDebt(new Date());
                    payNumberOfPayOuts_1.setDebitId(debit.getId());
                    payNumberOfPayOuts_1.setIn_Price(request.getInPrice());
                    this.payRepository.save(payNumberOfPayOuts_1);

                    response.getStatus().setMessage(MessageUtils.get(language, "Success"));
                    response.getStatus().setStatus(Status.Success);
                    return response;

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
            getDetailBills.setDebitItems(getDetailBillList);
        }
        return getDetailBillList;
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

        Page<GetDebitOfUserObject> findAll = null;
        if (typeFilter.equals("none") && valueFilter.equals("none")) {
            if (searchText.equals("") || searchText == null) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("status"));
                findAll = this.debitRepository.getDebitOfUser(pageable);
                totalPage = findAll.getTotalPages();
            } else {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("status"));
//                findAll = this.debitRepository.findByFullNameOrPhoneNumberContainingIgnoreCase(searchText, searchText, pageable);
                totalPage = findAll.getTotalPages();
            }

        } else {
            if (!typeFilter.equals("id") && !typeFilter.equals("address") && !typeFilter.equals("fullName") && !typeFilter.equals("amountUserDebit") &&
                    !typeFilter.equals("status") && !typeFilter.equals("phone")) {
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
                findAll = this.debitRepository.getDebitOfUser(pageable);
                totalPage = findAll.getTotalPages();

            } else {
//                findAll = this.debitRepository.findByFullNameOrPhoneNumberContainingIgnoreCase(searchText, searchText, pageable);
                totalPage = findAll.getTotalPages();
            }

        }
        List<DebitUserItems> debitItemsList = new ArrayList<>();

        for (GetDebitOfUserObject getDebitOfUserObject : findAll.getContent()) {
            DebitUserItems items = new DebitUserItems();
            items.setFullName(getDebitOfUserObject.getFullName());
            items.setAddress(getDebitOfUserObject.getAddress());
            items.setStatusCode(this.convertStatus(getDebitOfUserObject.getStatus(), language));
            items.setPhoneNumber(getDebitOfUserObject.getPhone());
            items.setAmountUserDebit(getDebitOfUserObject.getAmountUserDebit());
            items.setTotal(getDebitOfUserObject.getTotal());
            items.setId(getDebitOfUserObject.getIdUser());
            debitItemsList.add(items);
        }
        response.setItems(debitItemsList);
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
                    !typeFilter.equals("price") && !typeFilter.equals("createdDate") && !typeFilter.equals("phoneNumber") && !typeFilter.equals("status")) {
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
        List<DebitDetailResponse> debitDetailResponses = new ArrayList<>();
        List<GetDetailBill> debitItems = new ArrayList<>();
        DebitDetailResponse response = new DebitDetailResponse();
        if (debit == null) {
            return null;
        } else {
            List<VDebitDetail> debitDetails = this.debitDetailRepository.findByDebitId(debit.getId());
            if (debitDetails == null) {
                return null;
            } else {
                VUser user = this.vUserRepo.findById(debit.getUserId());
                response.setFullName(user.getFullname());
                response.setTotalAmountOwed(debit.getTotalAmountOwed());
                response.setBillId(debit.getBillId());
                response.setAddress(user.getAddress());
                response.setPhoneNumber(user.getNumberPhone());
                response.setTypeOfDebtor(debit.getTypeOfDebtor());
                response.setIdDebit(debit.getId());
                try {
                    response.setExpectedDateOfPaymentOfDebt((DataUtils.modifyStringLayout(String.valueOf(debit.getExpectedDateOfPaymentOfDebt()))));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                for (VDebitDetail debitDetail : debitDetails) {
                    GetDetailBill getDetailBill = new GetDetailBill();
                    getDetailBill.setPrice(debitDetail.getPrice());
                    getDetailBill.setProductName(debitDetail.getProductName());
                    VUnit vUnit = this.vUnitRepo.findById(debitDetail.getUnitId());
                    getDetailBill.setUnitName(vUnit.getUnitName());
                    getDetailBill.setUnitId(debitDetail.getUnitId());
                    debitItems.add(getDetailBill);
                }
                response.setDebitItems(debitItems);
            }
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
            items.setStatusCode(this.convertStatus(debit.getStatus(), language));
            items.setBillId(debit.getBillId());
            items.setAddress(user.getAddress());
            items.setTypeOfDebtor(this.convertTypeOfDebtor(debit.getTypeOfDebtor(), language));
            items.setExpectedDateOfPaymentOfDebt(debit.getExpectedDateOfPaymentOfDebt());
            debitItemsList.add(items);
        }
        return debitItemsList;
    }

    public String convertStatus(int status, String language) {

        switch (status) {
            case 1:
                return "Paid in full";
            case 2:
                return "Not paid yet";
            case 3:
                return "Unpaid";
            default:
                return "";
        }
    }

    public String convertTypeOfDebtor(int status, String language) {
        switch (status) {
            case 1:
                return "Customer Buying odd";
            case 2:
                return "Customer Buy wholesale";
            default:
                return "0";
        }
    }


}
