package com.vibee.service.debit;

import com.vibee.entity.*;
import com.vibee.model.ObjectResponse.DebitObjectPayResponse;
import com.vibee.model.ObjectResponse.DetailBillOfDetailDebit;
import com.vibee.model.Status;
import com.vibee.model.item.DebitItems;
import com.vibee.model.request.debit.DebitPageRequest;
import com.vibee.model.request.debit.DebitRequest;
import com.vibee.model.request.debit.PayRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.debit.DebitDetailResponse;
import com.vibee.model.response.debit.DebitItemsResponse;
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
import java.util.Calendar;
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

    @Autowired
    public DebitImpl(DebitRepository debitRepository, DebitDetailRepository debitDetailRepository, VBillRepo billRepo, VDetailBillRepo detailBillRepo, PayRepository payRepository, VUnitRepo vUnitRepo) {
        this.debitRepository = debitRepository;
        this.debitDetailRepository = debitDetailRepository;
        this.billRepo = billRepo;
        this.detailBillRepo = detailBillRepo;
        this.payRepository = payRepository;
        this.vUnitRepo = vUnitRepo;
    }

    public BaseResponse createDebit(DebitRequest request, BindingResult bindingResult) {

        BaseResponse response = new BaseResponse();
        Debit debit = new Debit();


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
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
        ;

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
            } else {
                debit.setDebitDate(new Date());
                debit.setCreatorDebtor(creatorDebtorRequest);
                debit.setFullName(fullNameRequest);
                debit.setPhoneNumber(phoneNumberRequest);
                debit.setTotalAmountOwed(totalAmountOwedRequest);
                debit.setCreatorPayer(creatorPayerRequest);
                debit.setBillId(billIdRequest);
                debit.setStatus(1);
                debit.setAddress(addressRequest);
                debit.setTypeOfDebtor(typeOfDebtorRequest);
                if (expectedDateOfPaymentOfDebtRequest == null) {
                    debit.setExpectedDateOfPaymentOfDebt(null);
                } else {
                    debit.setExpectedDateOfPaymentOfDebt(date);
                }
                debit = this.debitRepository.save(debit);

                List<DebitDetail> debitDetails = new ArrayList<>();
                List<GetDetailBill> debitItems = request.getDebitItems();
//                 if(debitItems!=null){
                for (GetDetailBill items : debitItems) {
                    DebitDetail debitDetail = new DebitDetail();
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
                        debitDetails.add(debitDetail);
                        this.debitDetailRepository.saveAll(debitDetails);
                    }
                }
//                 }

                response.getStatus().setMessage(MessageUtils.get(language, "success"));
                response.getStatus().setStatus(Status.Success);
            }
        }
        return response;
    }

    public BaseResponse payDebit(int idDebit, PayRequest request, BindingResult bindingResult) {

        BaseResponse response = new BaseResponse();

        String creatorDebtorRequest = "Liên nè";
        String fullNameRequest = request.getFullName();
        String phoneNumberRequest = request.getPhoneNumber();
        BigDecimal totalAmountOwedRequest = request.getTotalAmountOwed();
        String creatorPayerRequest = request.getCreatorPayer();
        int billIdRequest = request.getBillId();
        String addressRequest = request.getAddress();
        int typeOfDebtorRequest = request.getTypeOfDebtor();
        String expectedDateOfPaymentOfDebtRequest = request.getExpectedDateOfPaymentOfDebt();
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
            } else {
                Debit debit = this.debitRepository.findById(idDebit);
                debit.setDebitDate(new Date());
                debit.setCreatorDebtor(creatorDebtorRequest);
                debit.setFullName(fullNameRequest);
                debit.setPhoneNumber(phoneNumberRequest);
                debit.setTotalAmountOwed(totalAmountOwedRequest);
                debit.setCreatorPayer(creatorPayerRequest);
                debit.setBillId(billIdRequest);
                debit.setStatus(1);
                debit.setAddress(addressRequest);
                debit.setTypeOfDebtor(typeOfDebtorRequest);
                if (expectedDateOfPaymentOfDebtRequest == null) {
                    debit.setExpectedDateOfPaymentOfDebt(null);
                } else {
                    debit.setExpectedDateOfPaymentOfDebt(debit.getExpectedDateOfPaymentOfDebt());
                }
                debit = this.debitRepository.save(debit);

                List<DebitDetail> debitDetails = new ArrayList<>();
                List<GetDetailBill> debitItems = request.getDebitItems();
                List<Pay> pays = this.payRepository.findByDebitId(idDebit);
                DebitObjectPayResponse getPay = this.payRepository.getPay(debit.getId());
                Pay pay = new Pay();
                if (pays.size() == 0) {
                    for (GetDetailBill items : debitItems) {
                        if (bindingResult.hasErrors()) {
                            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
                            response.getStatus().setStatus(Status.Fail);
                        }
                        if (debit.getTotalAmountOwed().doubleValue() < request.getTotalAmountOwed().doubleValue()) {
                            response.getStatus().setMessage(MessageUtils.get(language, "Failed"));
                            response.getStatus().setStatus(Status.Fail);
                            return response;
                        }
                        pay.setPrice(debit.getTotalAmountOwed());
                        pay.setIn_Price(request.getInPrice());
                        pay.setStatus(1);
                        pay.setNumberOfPayOuts(1);
                        pay.setActualDateOfPaymentOfDebt(new Date());
                        pay.setDebitId(debit.getId());
                        this.payRepository.save(pay);
                        response.getStatus().setMessage(MessageUtils.get(language, "Success"));
                        response.getStatus().setStatus(Status.Success);
                        return response;

                    }
                } else {
                    for (GetDetailBill items : debitItems) {
                        if (bindingResult.hasErrors()) {
                            response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
                            response.getStatus().setStatus(Status.Fail);
                        }
                        else if (debit.getTotalAmountOwed().doubleValue() < request.getInPrice().doubleValue()) {
                            response.getStatus().setMessage(MessageUtils.get(language, "Failed"));
                            response.getStatus().setStatus(Status.Fail);
                            return response;
                        }
                     else if (getPay.getInPrice().doubleValue()<getPay.getPrice().doubleValue()) {
                            Pay payNumberOfPayOuts_1 = new Pay();
                            payNumberOfPayOuts_1.setPrice(debit.getTotalAmountOwed());
                            payNumberOfPayOuts_1.setStatus(1);
                            payNumberOfPayOuts_1.setNumberOfPayOuts(pay.getNumberOfPayOuts() + 1);
                            payNumberOfPayOuts_1.setActualDateOfPaymentOfDebt(new Date());
                            payNumberOfPayOuts_1.setDebitId(debit.getId());
                            payNumberOfPayOuts_1.setIn_Price(items.getInPrice());
                            this.payRepository.save(payNumberOfPayOuts_1);
                            response.getStatus().setMessage(MessageUtils.get(language, "Success"));
                            response.getStatus().setStatus(Status.Success);

                        }
//                     else {
//                            Pay payNumberOfPayOuts_2 = new Pay();
//                            payNumberOfPayOuts_2.setPrice(debit.getTotalAmountOwed());
//                            payNumberOfPayOuts_2.setStatus(1);
//                            payNumberOfPayOuts_2.setNumberOfPayOuts(pay.getNumberOfPayOuts() + 1);
//                            payNumberOfPayOuts_2.setActualDateOfPaymentOfDebt(new Date());
//                            payNumberOfPayOuts_2.setDebitId(debit.getId());
//                            payNumberOfPayOuts_2.setIn_Price(items.getInPrice());
//
//                            this.payRepository.save(payNumberOfPayOuts_2);
//                        }

                        response.getStatus().setMessage(MessageUtils.get(language, "Success"));
                        response.getStatus().setStatus(Status.Success);
                    }
                }


                response.getStatus().setMessage(MessageUtils.get(language, "success"));
                response.getStatus().setStatus(Status.Success);
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


    public BaseResponse pay(int idDebit, BigDecimal inPrice, String language, BindingResult bindingResult) {
        BaseResponse response = new BaseResponse();
        Pay pay = new Pay();
//        int idDebit = request.getDebiId();
//        BigDecimal amountPay = request.getInPrice();
//        String language = request.getLanguage();

        Debit debit = this.debitRepository.findById(idDebit);

        if (debit == null) {
            response.getStatus().setMessage(MessageUtils.get(language, "Error 121"));
            response.getStatus().setStatus(Status.Fail);
        }
        List<Pay> pays = this.payRepository.findByDebitId(idDebit);
        if (pays == null) {
            pay.setPrice(debit.getTotalAmountOwed());
            pay.setStatus(1);
            pay.setNumberOfPayOuts(1);
            pay.setActualDateOfPaymentOfDebt(new Date());
            pay.setDebitId(debit.getId());
            this.payRepository.save(pay);
        } else {
            if (bindingResult.hasErrors()) {
                response.getStatus().setMessage(MessageUtils.get(language, bindingResult.getAllErrors().get(0).getDefaultMessage()));
                response.getStatus().setStatus(Status.Fail);
            }
            if (debit.getTotalAmountOwed().doubleValue() < inPrice.doubleValue()) {
                response.getStatus().setMessage(MessageUtils.get(language, "Error 209"));
                response.getStatus().setStatus(Status.Fail);
//            } else if (debit.getTotalAmountOwed().doubleValue() == amountPay.doubleValue()) {
                Pay payNumberOfPayOuts = new Pay();
                payNumberOfPayOuts.setPrice(debit.getTotalAmountOwed());
                payNumberOfPayOuts.setStatus(1);
                payNumberOfPayOuts.setNumberOfPayOuts(pay.getNumberOfPayOuts() + 1);
                payNumberOfPayOuts.setActualDateOfPaymentOfDebt(new Date());
                payNumberOfPayOuts.setDebitId(debit.getId());
                this.payRepository.save(payNumberOfPayOuts);
//            } else if (debit.getTotalAmountOwed().doubleValue() > amountPay.doubleValue()) {
//                pay.setPrice(amountPay);
//                pay.setStatus(2);
//                pay.setNumberOfPayOuts(1);
            }

            response.getStatus().setMessage(MessageUtils.get(language, "Success"));
            response.getStatus().setStatus(Status.Success);
        }

        return response;
    }

    public DebitItemsResponse findAll(DebitPageRequest request) {
        int pageNumber = request.getPageNumber();
        int pageSize = request.getPageSize();
        String searchText = request.getSearchText();
        String typeFilter = request.getFilter().getTypeFilter();
        String valueFilter = request.getFilter().getValueFilter();
        String language = request.getLanguage();
        DebitItemsResponse response = new DebitItemsResponse();
        int totalPage = 0;

        List<Debit> debits = this.debitRepository.findAll();
        for (Debit debit : debits) {
            List<Pay> pays = this.payRepository.findByDebitId(debit.getId());
            DebitObjectPayResponse getPay = this.payRepository.getPay(debit.getId());
            for (Pay pay : pays) {
                if (pay == null) {
                    debit.setStatus(3);
                } else {
                    if (debit.getTotalAmountOwed().doubleValue() == pay.getPrice().doubleValue()) {
                        debit.setStatus(1);
                        pay.setStatus(1);
                    } else if (debit.getTotalAmountOwed().doubleValue() < pay.getPrice().doubleValue()) {
                        debit.setStatus(2);
                        pay.setStatus(2);
                    }
                }
                this.debitRepository.save(debit);
                this.payRepository.save(pay);
            }
        }

        Page<Debit> findAll = null;
        if (typeFilter.equals("none") && valueFilter.equals("none")) {
            if (searchText.equals("") || searchText == null) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("status"));
                findAll = this.debitRepository.findAll(pageable);
                totalPage = findAll.getTotalPages();
            } else {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("status"));
                findAll = this.debitRepository.findByFullNameOrPhoneNumberContainingIgnoreCase(searchText, searchText, pageable);
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
                findAll = this.debitRepository.findAll(pageable);
                totalPage = findAll.getTotalPages();

            } else {
                findAll = this.debitRepository.findByFullNameOrPhoneNumberContainingIgnoreCase(searchText, searchText, pageable);
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
        Debit debit = this.debitRepository.findById(idDebit);
        List<DebitDetailResponse> debitDetailResponses = new ArrayList<>();
        List<GetDetailBill> debitItems = new ArrayList<>();
        DebitDetailResponse response = new DebitDetailResponse();
        if (debit == null) {
            return null;
        } else {
            List<DebitDetail> debitDetails = this.debitDetailRepository.findByDebitId(debit.getId());
            if (debitDetails == null) {
                return null;
            } else {

                response.setFullName(debit.getFullName());
                response.setTotalAmountOwed(debit.getTotalAmountOwed());
                response.setBillId(debit.getBillId());
                response.setAddress(debit.getAddress());
                response.setPhoneNumber(debit.getPhoneNumber());
                response.setTypeOfDebtor(debit.getTypeOfDebtor());
                response.setIdDebit(debit.getId());
                response.setExpectedDateOfPaymentOfDebt(String.valueOf(debit.getExpectedDateOfPaymentOfDebt()));
                for (DebitDetail debitDetail : debitDetails) {
                    GetDetailBill getDetailBill = new GetDetailBill();
                    getDetailBill.setPrice(debitDetail.getPrice());
                    getDetailBill.setProductName(debitDetail.getProductName());
                    getDetailBill.setInPrice(debitDetail.getPrice());
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

    private List<DebitItems> convertEntityToResponse(List<Debit> debits, String language) {
        List<DebitItems> debitItemsList = new ArrayList<>();

        for (Debit debit : debits) {
            DebitItems items = new DebitItems();

            items.setId(debit.getId());
            items.setDebitDate(debit.getDebitDate());
            items.setCreatorDebtor(debit.getCreatorDebtor());
            items.setFullName(debit.getFullName());
            items.setPhoneNumber(debit.getPhoneNumber());
            items.setTotalAmountOwed(debit.getTotalAmountOwed());
            items.setCreatorPayer(debit.getCreatorPayer());
            items.setStatusCode(this.convertStatus(debit.getStatus(), language));
            items.setBillId(debit.getBillId());
            items.setAddress(debit.getAddress());
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
