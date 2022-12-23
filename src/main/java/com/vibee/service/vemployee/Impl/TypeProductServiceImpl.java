package com.vibee.service.vemployee.Impl;

import com.vibee.entity.VTypeProduct;
import com.vibee.model.Status;
import com.vibee.model.request.category.*;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.category.*;
import com.vibee.repo.VProductRepo;
import com.vibee.repo.VTypeProductRepo;
import com.vibee.service.vemployee.ITypeProductService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class TypeProductServiceImpl implements ITypeProductService {

    private final VTypeProductRepo typeProductRepo;
    private final VProductRepo productRepo;

    public TypeProductServiceImpl(VTypeProductRepo repo, VProductRepo productRepo) {
        this.typeProductRepo = repo;
        this.productRepo = productRepo;
    }
    @Override
    public TypeProductItemsResponse getAll() {

        List<VTypeProduct> list = this.typeProductRepo.findByStatus(1);
        Map<Integer, TypeItemsDto> hm = new HashedMap();
        String language = "";
        TypeProductItemsResponse response = new TypeProductItemsResponse();

        List<TypeItemsDto> DX = new ArrayList<TypeItemsDto>();
        for (VTypeProduct p : list) {
            //  ----- Child -----
            TypeItemsDto mmdChild;
            if (hm.containsKey(p.getId())) {
                mmdChild = hm.get(p.getId());
            } else {
                mmdChild = new TypeItemsDto();
                hm.put(p.getId(), mmdChild);
            }
            mmdChild.getData().setName(p.getName());
            mmdChild.getData().setDescription(p.getDescription());
            mmdChild.getData().setStatus(p.getStatus());
            mmdChild.getData().setStatusName(this.convertStatus(p.getStatus(), language));
            mmdChild.getData().setCreatedDate(p.getCreatedDate());
            mmdChild.getData().setCreator(p.getCreator());
            mmdChild.getData().setId(String.valueOf(p.getId()));
            mmdChild.getData().setParentId(String.valueOf(p.getParentId()));

//            String amountProduct = this.productRepo.amountProductByType(p.getId());
            String amountProduct ="10";
            if (amountProduct == null) {
                mmdChild.getData().setAmountProduct("0");
            } else {
                mmdChild.getData().setAmountProduct(amountProduct);
            }
            // ------ Parent ----
            TypeItemsDto mmdParent;
            if (hm.containsKey(p.getParentId())) {
                mmdParent = hm.get(p.getParentId());
            } else {
                mmdParent = new TypeItemsDto();
                hm.put(p.getParentId(), mmdParent);
            }

            mmdParent.getData().setId(String.valueOf(p.getParentId()));
            mmdParent.getData().setParentId(mmdParent.getData().getParentId());
            mmdParent.addChildren(mmdChild);
        }

        for (TypeItemsDto mmd : hm.values()) {
            if (mmd.getData().getParentId().equals("0"))
                DX.add(mmd);
        }

        for (TypeItemsDto mmd : DX) {
            System.out.println(DX.size() + "size  " + DX);
            response.setData(DX);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.success.typeProduct"));
            response.getStatus().setStatus(Status.Success);
        }
        return response;
    }
    @Override
    public List<TypeItemsDto> getParentChild(List<VTypeProduct> list, String language) {

        Map<Integer, TypeItemsDto> hm = new HashedMap();
        List<TypeItemsDto> DX = new ArrayList<TypeItemsDto>();
        if (list == null) {
            return null;
        }
        for (VTypeProduct p : list) {
            //  ----- Child -----
            TypeItemsDto mmdChild;
            if (hm.containsKey(p.getId())) {
                mmdChild = hm.get(p.getId());
            } else {
                mmdChild = new TypeItemsDto();
                hm.put(p.getId(), mmdChild);
            }
            mmdChild.getData().setName(p.getName());
            mmdChild.getData().setDescription(p.getDescription());
            mmdChild.getData().setStatus(p.getStatus());
            mmdChild.getData().setCreatedDate(p.getCreatedDate());
            mmdChild.getData().setStatusName(this.convertStatus(p.getStatus(), language));
            mmdChild.getData().setCreator(p.getCreator());
            mmdChild.getData().setId(String.valueOf(p.getId()));
            mmdChild.getData().setParentId(String.valueOf(p.getParentId()));
            String amountProduct = this.productRepo.amountProductByType(p.getId());
            String amountProduct1 = this.productRepo.amountProductByType1(p.getId());
            if (p.getParentId() == 0) {
                mmdChild.getData().setAmountProduct(amountProduct1);

            } else {
                if (amountProduct == null || amountProduct1 == null) {
                    mmdChild.getData().setAmountProduct("0");
                } else {
                    mmdChild.getData().setAmountProduct(amountProduct);
                }

            }
            // ------ Parent ----
            TypeItemsDto mmdParent;
            if (hm.containsKey(p.getParentId())) {
                mmdParent = hm.get(p.getParentId());
            } else {
                mmdParent = new TypeItemsDto();
                hm.put(p.getParentId(), mmdParent);
            }

            mmdParent.getData().setId(String.valueOf(p.getParentId()));
            mmdParent.getData().setParentId(mmdParent.getData().getParentId());
            mmdParent.addChildren(mmdChild);
        }

        for (TypeItemsDto mmd : hm.values()) {
            if (mmd.getData().getParentId().equals("0"))
                DX.add(mmd);
        }
        return DX;
    }
    @Override
    public TypeItems getAll1(TypeProductRequest request) {
        int pageNumber = request.getPageNumber();
        int pageSize = request.getPageSize();
        String searchText = request.getSearchText();
        String typeFilter = request.getFilter().getTypeFilter();
        String valueFilter = request.getFilter().getValueFilter();
        String language = request.getLanguage();
        TypeItems response = new TypeItems();
        Map<Integer, TypeItemsDto> hm = new HashedMap();
        List<TypeItemsDto> DX = new ArrayList<TypeItemsDto>();
        int totalPage = 0;
        List<VTypeProduct> list = null;
        List<VTypeProduct> listByPage = null;
        List<Integer> listByIdParent = null;

        if (typeFilter.equals("none") && valueFilter.equals("none")) {
            if (searchText.equals("") || searchText == null) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("status"));
                listByIdParent = this.typeProductRepo.listId(pageable);
                list = this.typeProductRepo.findByAll1(listByIdParent);
                listByPage = this.typeProductRepo.findByParentIdAndStatus(0, 1);
                totalPage = (int) Math.ceil((double) listByPage.size() / pageSize);

            } else {
                Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("status"));
                listByIdParent = this.typeProductRepo.listIdParent(searchText + "%", pageable);
                list = this.typeProductRepo.searchNameByPageAndFilter(listByIdParent);
                listByPage = this.typeProductRepo.listId(searchText + "%");
                totalPage = (int) Math.ceil((double) listByPage.size() / pageSize);

            }

        } else {
            if (!typeFilter.equals("id") && !typeFilter.equals("name") && !typeFilter.equals("description") && !typeFilter.equals("creator") &&
                    !typeFilter.equals("createdDate") && !typeFilter.equals("amountProduct") && !typeFilter.equals("status")) {
            }
            Pageable pageable = null;
            if (valueFilter.equals("asc")) {
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(typeFilter).ascending());
            } else {
                pageable = PageRequest.of(pageNumber, pageSize, Sort.by(typeFilter).descending());
            }
            if (searchText.equals("") || searchText == null) {
                listByIdParent = this.typeProductRepo.listId(pageable);
                list = this.typeProductRepo.findByAll1(listByIdParent);
                listByPage = this.typeProductRepo.findByParentIdAndStatus(0, 1);
                totalPage = (int) Math.ceil((double) listByPage.size() / pageSize);

            } else {
                listByIdParent = this.typeProductRepo.listIdParent("%"+searchText + "%", pageable);
                list = this.typeProductRepo.searchNameByPageAndFilter(listByIdParent);
                listByPage = this.typeProductRepo.listId("%"+searchText + "%");
                totalPage = (int) Math.ceil((double) listByPage.size() / pageSize);
            }
        }

        response.setData(this.getParentChild(list, language));
        response.setTotalPages(totalPage);
        response.setPage(pageNumber);
        response.setPageSize(pageSize);
        response.setTotalItems(listByPage.size());
        return response;
    }
    @Override
    public BaseResponse createType(CreateTypeProductRequest request) {
        BaseResponse response = new BaseResponse();
        String name = request.getName();
        String decription = request.getDescription();
        String parentId = request.getParentId();
        String language = request.getLanguage();

        VTypeProduct typeProduct = new VTypeProduct();
        typeProduct.setName(name);
        typeProduct.setCreator("lienpt");
        typeProduct.setCreatedDate(new Date());
        typeProduct.setStatus(1);
        typeProduct.setDescription(decription);
        this.typeProductRepo.save(typeProduct);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct.success"));
        return response;
    }
    @Override
    public SelectionTypeProductItemsResponse getAllSelect() {
        String language = "";
        SelectionTypeProductItemsResponse response = new SelectionTypeProductItemsResponse();
        List<Object> list = this.typeProductRepo.getByParentIdAndStatus();
        response.setData(this.convert(list, language));
        return response;
    }
    @Override
    public List<SelectionTypeProductItems> getAllSelected() {
        String language = "";
        List<SelectionTypeProductItems> response = new ArrayList<>();
        List<Object> list = this.typeProductRepo.getByParentIdAndStatus();

        return this.convert(list, language);
    }

    private List<SelectionTypeProductItems> convert(List<Object> request, String language) {
        List<SelectionTypeProductItems> selectionTypeProductItems = new ArrayList<>();
        int status = 0;
        for (Object o : request) {
            Object[] objects = (Object[]) o;
            SelectionTypeProductItems items = new SelectionTypeProductItems();
            items.setId((Integer) objects[0]);
            items.setName((String) objects[2]);
            items.setParentId((Integer) objects[1]);
            selectionTypeProductItems.add(items);
        }
        return selectionTypeProductItems;
    }
    @Override
    public BaseResponse delete(DeleteTypeProductRequest request) {
        BaseResponse response = new BaseResponse();
        String language = request.getLanguage();
        int id = request.getId();
        String amountProduct = null;
        String amountProduct1 = null;
        VTypeProduct typeProduct = new VTypeProduct();
        typeProduct = this.typeProductRepo.findById(id);

        int amount = 0;
        if (typeProduct == null) {
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct.failse"));
            return response;
        } else {
            if (typeProduct.getStatus() == 2) {
                response.getStatus().setStatus(Status.Fail);
                response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct.status.fails"));
                return response;
            } else {
                List<VTypeProduct> typeProducts = this.typeProductRepo.findId(id);
                amountProduct = this.productRepo.amountProductByType(id);
                amountProduct1 = this.productRepo.amountProductByType1(id);

                for (VTypeProduct p : typeProducts) {
                    if (p.getParentId() == 0) {
                        if (amountProduct1 == null) {
                            amount = Integer.parseInt("0");
                        } else {
                            amount = Integer.parseInt(amountProduct1);
                        }
                        if (amount <= 0 || amountProduct1 == "0" || amountProduct1 == null) {
                            this.typeProductRepo.updateTypeProductBystatus(p.getId());
                            this.typeProductRepo.updateTypeProductBystatus(id);
                            response.getStatus().setStatus(Status.Success);
                            response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct1.Succes"));
                        } else {
                            response.getStatus().setStatus(Status.Fail);
                            response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct.failse-amount"));
                        }

                    } else {
                        if (amountProduct == null || amountProduct1 == null) {
                            amount = Integer.parseInt("0");
                            if (amount <= 0 || amountProduct == "0" || amountProduct == null) {
                                this.typeProductRepo.updateTypeProductBystatus(p.getId());
                                this.typeProductRepo.updateTypeProductBystatus(id);
                                response.getStatus().setStatus(Status.Success);
                                response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct1.Succes"));
                            }
                        } else {
                            amount = Integer.parseInt(amountProduct);
                            if (amount <= 0) {
                                this.typeProductRepo.updateTypeProductBystatus(p.getId());
                                this.typeProductRepo.updateTypeProductBystatus(id);
                                response.getStatus().setStatus(Status.Success);
                                response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct1.Succes"));
                            } else {
                                response.getStatus().setStatus(Status.Fail);
                                response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct.failse-amount"));
                            }

                        }

                    }

                }

                return response;
            }
        }
    }

    @Override
    public BaseResponse update(UpdateTypeProductRequest request) {
        BaseResponse response = new BaseResponse();
        int id = request.getId();
        String name = request.getName();
        String desrestion = request.getDescription();
        int parentId = request.getParentId();
        String language = request.getLanguage();

        VTypeProduct typeProduct = this.typeProductRepo.findById(id);
        if (typeProduct == null) {
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProducId.notExits"));
        } else {
            if (typeProduct.getStatus() == 1) {
                typeProduct.setName(name);
                typeProduct.setCreator("lienpt");
                typeProduct.setCreatedDate(new Date());
                typeProduct.setStatus(1);
                typeProduct.setDescription(desrestion);
                if (parentId == 0) {
                    typeProduct.setParentId(0);
                } else {
                    typeProduct.setParentId(parentId);
                }
                this.typeProductRepo.save(typeProduct);
                response.getStatus().setStatus(Status.Success);
                response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct.success"));
            } else {
                response.getStatus().setStatus(Status.Fail);
                response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct.fail"));
            }
        }
        return response;

    }
    @Override
    public EditTypeProductResponse edit(int id) {
        EditTypeProductResponse response = new EditTypeProductResponse();

        VTypeProduct typeProduct = this.typeProductRepo.findById(id);
        if (typeProduct == null) {
            response.getStatus().setStatus(Status.Fail);
        } else {
            if (typeProduct.getStatus() == 1) {
                response.setId(id);
                response.setParentid(typeProduct.getParentId());
                response.setDescription(typeProduct.getDescription());
                response.setName(typeProduct.getName());
                response.getStatus().setStatus(Status.Success);

            } else {
                response.getStatus().setStatus(Status.Fail);
            }
        }
        return response;

    }

    public String convertStatus(int status, String language) {
        switch (status) {
            case 1:
                return "Hoạt động ";
            case 2:
                return "Không hoạt động";
            default:
                return "không biết";
        }
    }

    public SelectionTypeProductItems getAllSelectDetail(int id) {
        SelectionTypeProductItems response = new SelectionTypeProductItems();
        VTypeProduct typeProduct = this.typeProductRepo.getByParentIdAndStatus(id);
        response.setName(typeProduct.getName());
        response.setId(typeProduct.getId());
        response.setParentId(typeProduct.getParentId());
        return response;
    }
    @Override
    public BaseResponse createTypeDetail(CreateTypeProductDetailRequest request) {
        BaseResponse response = new BaseResponse();
        String name = request.getName();
        String decription = request.getDescription();
        int id = request.getId();

        String language = request.getLanguage();

        VTypeProduct typeProduct = new VTypeProduct();
        typeProduct.setName(name);
        typeProduct.setCreator("lienpt");
        typeProduct.setCreatedDate(new Date());
        typeProduct.setStatus(1);
        typeProduct.setDescription(decription);
        typeProduct.setParentId(id);
        this.typeProductRepo.save(typeProduct);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.typeProduct.success"));
        return response;
    }
}