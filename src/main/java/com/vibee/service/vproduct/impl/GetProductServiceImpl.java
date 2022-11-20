package com.vibee.service.vproduct.impl;

import com.vibee.entity.VImport;
import com.vibee.entity.VProduct;
import com.vibee.entity.VUploadFile;
import com.vibee.entity.VWarehouse;
import com.vibee.model.Status;
import com.vibee.model.item.GetProductItem;
import com.vibee.model.item.SelectExportItem;
import com.vibee.model.request.product.GetProductRequest;
import com.vibee.model.request.product.ViewStallRequest;
import com.vibee.model.response.product.*;
import com.vibee.model.ObjectResponse.ProductStallObject;
import com.vibee.repo.*;
import com.vibee.service.vproduct.GetProductService;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.ProductUtils;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class GetProductServiceImpl implements GetProductService {

    private final VImportRepo importRepo;
    private final VExportRepo exportRepo;
    private final VProductRepo productRepo;
    private final VUnitRepo unitRepo;
    private final VFileUploadRepo fileUploadRepo;
    private final VWarehouseRepo vWarehouseRepo;

    @Autowired
    public GetProductServiceImpl(VImportRepo importRepo,
                                 VExportRepo exportRepo,
                                 VProductRepo productRepo,
                                 VUnitRepo unitRepo,
                                 VFileUploadRepo fileUploadRepo, VWarehouseRepo vWarehouseRepo) {
        this.importRepo = importRepo;
        this.exportRepo = exportRepo;
        this.productRepo = productRepo;
        this.unitRepo = unitRepo;
        this.fileUploadRepo = fileUploadRepo;
        this.vWarehouseRepo = vWarehouseRepo;
    }

    @Override
    public SearchViewStallResponse viewStall(ViewStallRequest request) {
        log.info("GetProductService - view staff :: BEGIN");
        String language = request.getLanguage();
        String searchValue = request.getSearchValue();
        SearchViewStallResponse response = new SearchViewStallResponse();
        List<ProductStallObject> products = new ArrayList<>();
        try {
            Integer.parseInt(searchValue);
            products = this.productRepo.searchProductByBarCode(searchValue + "%");
        } catch (Exception e) {
            if (searchValue.startsWith("VB")) {
                products = this.productRepo.searchProductByProductCode(searchValue + "%");
            } else {
                products = this.productRepo.searchProductByproductName(searchValue + "%");
            }

        }

        if (products.isEmpty()) {
            log.error("product not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.not.found"));
            return response;
        }

        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("");
        response.setResults(products);
        log.info("GetProductService - view staff :: END");
        return response;
    }

    @Override
    public GetProductResponse viewManage(GetProductRequest request) {
        log.info("ProductService-getall :: Start");
        int pageNow = request.getPageNumber();
        int pageSize = request.getPageSize();
        String searchText = request.getSearchText();
        String typeFilter = request.getFilter().getTypeFilter();
        String valueFilter = request.getFilter().getValueFilter();
        String language = request.getLanguage();
        GetProductResponse response = new GetProductResponse();
        long totalItems = 0;
        List<GetProductItem> items = new ArrayList<>();
        Pageable pageable = null;
        List<Object> o = null;
        if (typeFilter.equals("none") && valueFilter.equals("none")) {
            pageable = PageRequest.of(pageNow, pageSize, Sort.by("status"));
            o = this.productRepo.getProducts(searchText, pageable);
//				o = this.productRepo.getProducts(searchText,"product.status","asc",pagetotal,pageSize );
            if (searchText.equals("") || searchText.isEmpty()) {
                totalItems = this.productRepo.count();
            } else {
                totalItems = this.productRepo.countProduct(searchText);
            }
        } else {
            if (typeFilter.equals("productName") || typeFilter.equals("status") || typeFilter.equals("e.outPrice") || typeFilter.equals("s.nameSup")) {
                if (valueFilter.equals("asc")) {
                    pageable = PageRequest.of(pageNow, pageSize, Sort.by(typeFilter).ascending());
                } else {
                    pageable = PageRequest.of(pageNow, pageSize, Sort.by(typeFilter).descending());
                }
                o = this.productRepo.getProducts(searchText, pageable);
                totalItems = this.productRepo.countProduct(searchText);
            } else if (typeFilter.equals("inventory")) {
                if (valueFilter.equals("asc")) {
                    pageable = PageRequest.of(pageNow, pageSize, JpaSort.unsafe("(sum(w.inAmount)-sum(e.outAmount/u.amount))").ascending());
                    o = this.productRepo.getProducts(searchText, pageable);
                } else {
                    pageable = PageRequest.of(pageNow, pageSize, JpaSort.unsafe("(sum(w.inAmount)-sum(e.outAmount/u.amount))").descending());
                    o = this.productRepo.getProducts(searchText, pageable);
                }
            }
        }

        items = this.convertResponse(o, language);

        response.setItems(items);
        response.setFilter(request.getFilter());
        response.setTotalItems((int) totalItems);
        response.setTotalPages((int) (totalItems / pageSize));
        response.setPage(pageNow);
        response.setPageSize(pageSize);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, ""));
        log.info("ProductService-getall :: End");
        return response;
    }

    private List<GetProductItem> convertResponse(List<Object> products, String language) {
        List<GetProductItem> list = new ArrayList<GetProductItem>();
        int statusCode = 0;
        int productId = 0;
        int fileId = 0;
        for (Object o : products) {
            Object[] objects = (Object[]) o;
            GetProductItem item = new GetProductItem();

            productId = (Integer) objects[0];

            item.setProductCode(productId);
            item.setProductName((String) objects[1]);
            statusCode = (int) objects[2];
            item.setProfit((BigDecimal) objects[5]);
            item.setInventory((Double) objects[4]);
            fileId = (int) objects[3];

            item.setImg(this.fileUploadRepo.getURLById(fileId));
            item.setSupName((String) objects[6]);
            item.setStatusCode(statusCode);
            item.setStatusName(ProductUtils.getstatusname(statusCode, language));
            item.setUnit((int) objects[7]);
            list.add(item);
        }
        return list;
    }

    @Override
    public DetailProductResponse detail(int productId, String language) {
        log.info("DetailProductService:: START");
        DetailProductResponse response = new DetailProductResponse();
        Object item = new DetailProductResponse();
        item = this.productRepo.findProductById(productId);
        if (item == null) {
            log.error("Product is not exits");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.not.already.exit"));
            return response;
        }
        Object[] objects = (Object[]) item;
        response.setId(objects[0] != null ? (int) objects[0] : 0);
        response.setCreatedDate(objects[1] != null ? Utiliies.formatDateTime((Date) objects[1]) : "");
        response.setCreator(objects[2] != null ? (String) objects[2] : "");
        response.setStatusName(objects[3] != null ? ProductUtils.getstatusname((Integer) objects[3], language) : "");
        response.setBarCode(objects[4] != null ? objects[4].toString() : "");
        response.setDescription(objects[5] != null ? objects[5].toString() : "");
        response.setCategoryName(objects[6] != null ? objects[6].toString() : "");
        double inAmount = (Double) objects[7];
        BigDecimal outAmount = (BigDecimal) objects[8];
        response.setSumInAmount(inAmount);
        response.setSumOutAmount(outAmount);
        response.setStatusCode(objects[3] != null ? (Integer) objects[3] : 0);
        response.setImage(objects[9] != null ? objects[9].toString() : "");
        response.setName(objects[10] != null ? objects[10].toString() : "");
        response.setInventory(inAmount - outAmount.doubleValue());
        response.setImportDate(Utiliies.formatDateTime(this.importRepo.getCreatedDateByProductId(productId)));
//        response.setSumImport(this.importRepo.countWarehouseByProductId(productId));
        response.setSupplierNames(this.importRepo.getSupplierNamesByProductId(productId));
        response.setUnitName(this.unitRepo.getUnitNameByProductId(productId));
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.product.detail.success"));
        return response;
    }

    @Override
    public GetHomeSellOnlineResponse selectProduct(String language) {
        log.info("selectProductService:: BEGIN");
        GetHomeSellOnlineResponse response = new GetHomeSellOnlineResponse();
        List<IgetHomeSellOnline> igetHomeSellOnlines = this.productRepo.getHomeSellOnline();
        response.setItems(igetHomeSellOnlines);
        log.info("selectProductService:: END");
        return response;
    }
}
