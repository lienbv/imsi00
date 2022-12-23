package com.vibee.service.vproduct.impl;

import com.vibee.entity.*;
import com.vibee.model.Status;
import com.vibee.model.item.GetProductItem;
import com.vibee.model.item.GetTypeProductItem;
import com.vibee.model.item.PageItem;
import com.vibee.model.item.SelectExportItem;
import com.vibee.model.request.product.GetProductRequest;
import com.vibee.model.request.product.ViewStallRequest;
import com.vibee.model.response.product.*;
import com.vibee.model.ObjectResponse.ProductStallObject;
import com.vibee.model.result.ProductStallResult;
import com.vibee.model.result.SellOnlineResult;
import com.vibee.repo.*;
import com.vibee.service.vproduct.GetProductService;
import com.vibee.utils.CommonUtil;
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
import java.util.*;

@Service
@Log4j2
public class GetProductServiceImpl implements GetProductService {

    private final VImportRepo importRepo;
    private final VExportRepo exportRepo;
    private final VProductRepo productRepo;
    private final VUnitRepo unitRepo;
    private final VFileUploadRepo fileUploadRepo;
    private final VWarehouseRepo vWarehouseRepo;
    private final VUserRepo userRepo;
    private final VTypeProductRepo typeProductRepo;

    @Autowired
    public GetProductServiceImpl(VImportRepo importRepo,
                                 VExportRepo exportRepo,
                                 VProductRepo productRepo,
                                 VUnitRepo unitRepo,
                                 VFileUploadRepo fileUploadRepo, VWarehouseRepo vWarehouseRepo,
                                 VUserRepo userRepo,
                                 VTypeProductRepo typeProductRepo) {
        this.importRepo = importRepo;
        this.exportRepo = exportRepo;
        this.productRepo = productRepo;
        this.unitRepo = unitRepo;
        this.fileUploadRepo = fileUploadRepo;
        this.vWarehouseRepo = vWarehouseRepo;
        this.userRepo = userRepo;
        this.typeProductRepo = typeProductRepo;
    }

    @Override
    public SearchViewStallResponse viewStall(ViewStallRequest request) {
        log.info("GetProductService - view staff :: BEGIN");
        String language = request.getLanguage();
        String searchValue = request.getSearchValue();
        SearchViewStallResponse response = new SearchViewStallResponse();
        List<ProductStallResult> results = new ArrayList<>();
        List<VImport> imports = this.importRepo.getImportIsActive();
        Map<Integer, VImport> mapImport = new HashMap<>();
//        List<VImport> imports = this.importRepo.findImportByProductCode(searchValue);
        List<Integer> importIds = new ArrayList<>();
        if (imports.isEmpty()) {
            log.error("product not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.not.found"));
            return response;
        }
        for (int i = 0; i < imports.size(); i++) {
            for (int j = 1; j < imports.size(); j++) {
                if (imports.get(i).getWarehouseId()==imports.get(j).getWarehouseId()) {
                    if (imports.get(i).getExpiredDate().before(imports.get(j).getExpiredDate())) {
//                        imports.remove(imports.get(j));
                        mapImport.put(imports.get(i).getWarehouseId(), imports.get(i));
                    }
                }
            }
        }
//        for (int i = 0; i < imports.size(); i++) {
//            for (int j = 1; j < imports.size(); j++) {
//                if (imports.get(i).getWarehouseId()==imports.get(j).getWarehouseId()) {
//                    imports.remove(imports.get(j));
//                }
//            }
//        }
        for (VImport vImport : mapImport.values()) {
            VProduct product = this.productRepo.getProductByWarehouseId(vImport.getWarehouseId());
            if (product==null){
                continue;
            }
            ProductStallResult result = new ProductStallResult();
            result.setProductCode(vImport.getProductCode());
            result.setProductName(product.getProductName());
            result.setImg(this.fileUploadRepo.getURLById(product.getFileId()));
            result.setProductId(product.getId());
            result.setBarCode(product.getBarCode());
            result.setImportId(vImport.getId());
            results.add(result);
        }

        if (results.isEmpty()) {
            log.error("product not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.not.found"));
            return response;
        }

        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("");
        response.setResults(results);
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
            int unitId = (int) objects[7];
            if (unitId == 0) {
                item.setUnit("");
            } else {
                item.setUnit(this.unitRepo.getUnitNameByUnitId(unitId));
            }
            list.add(item);
        }
        return list;
    }

    @Override
    public DetailProductResponse detail(int productId, String language) {
        log.info("DetailProductService:: START");
        DetailProductResponse response = new DetailProductResponse();
        VProduct product = this.productRepo.getProduct(productId);
        VWarehouse warehouse = this.vWarehouseRepo.findByProductId(productId);

        if (product == null) {
            log.error("Product is not exits");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.not.already.exit"));
            return response;
        }
        if (warehouse == null) {
            log.error("warehouse not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.warehouse.not.found"));
            return response;
        }
        String creator = this.userRepo.findFullNameByUsername(product.getCreator());
        if (creator == null) {
            log.error("creator not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.creator.not.found"));
            return response;
        }
        String categoryName = this.typeProductRepo.getNameById(product.getProductType());
        if (categoryName == null) {
            log.error("categoryName not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.category.not.found"));
            return response;
        }
        String urlImage = this.fileUploadRepo.getURLById(product.getFileId());
        response.setId(productId);
        response.setCreatedDate(Utiliies.formatDateTime(product.getCreatedDate()));
        response.setCreator(creator);
        response.setStatusName(ProductUtils.getstatusname(product.getStatus(), language));
        response.setBarCode(product.getBarCode());
        response.setDescription(product.getDescription());
        response.setCategoryName(categoryName);
        double inAmount = warehouse.getInAmount();
        double outAmount = warehouse.getOutAmount();
        response.setSumInAmount(inAmount);
        response.setSumOutAmount(outAmount);
        response.setStatusCode(product.getStatus());
        response.setImage(urlImage);
        response.setName(product.getProductName());
        response.setInventory(inAmount - outAmount);
        response.setImportDate(Utiliies.formatDateTime(this.importRepo.getCreatedDateByProductId(warehouse.getProductId())));
        response.setSumImport(warehouse.getNumberOfEntries());
        response.setSupplierNames(product.getSupplierName());
        response.setUnitName(this.unitRepo.getUnitNameByUnitId(warehouse.getUnitId()));
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

    @Override
    public InfoUpdateProductResponse infoUpdate(int idProd, String language) {
        log.info("infoUpdateService :: Start");
        InfoUpdateProductResponse response = new InfoUpdateProductResponse();
        List<VTypeProduct> typeProducts = this.typeProductRepo.findByStatus(1);
        List<GetTypeProductItem> typeProductItems = this.convertItem(typeProducts);
        VProduct product = this.productRepo.getById(idProd);

        if (product == null) {
            log.error("infoUpdateService :: Product not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "product.not.found"));
            return response;
        }
        String urlImage = this.fileUploadRepo.getURLById(product.getFileId());
        String categoryName = this.typeProductRepo.getById(product.getProductType()).getName();
        response.setTypeProductItems(typeProductItems);
        response.setDescription(product.getDescription());
        response.setNameProd(product.getProductName());
        response.setImg(urlImage);
        response.setBarCode(product.getBarCode());
        response.setIdProd(product.getId());
        response.setCategoryName(categoryName);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("");
        response.setStatusItems(ProductUtils.getStatuss(language));
        response.setStatusName(ProductUtils.getstatusname(product.getStatus(), language));
        response.setStatusCode(product.getStatus());
        log.info("infoUpdateService :: End");
        return response;
    }

    @Override
    public SellOnlineResponse sellOnline(String language, int pageNumber, int pageSize, String search) {
        log.info("sellOnlineService :: Start");
        SellOnlineResponse response = new SellOnlineResponse();
        List<SellOnlineResult> sellOnlineResults = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<VProduct> products;
        long totalPage;
        if (CommonUtil.isEmptyOrNull(search)) {
            products = this.productRepo.searchProductByName(pageable);
            totalPage = this.productRepo.countProduct();
        } else {
            products = this.productRepo.searchProductByName(search, pageable);
            totalPage = this.productRepo.countProduct(search);
        }

        List<VWarehouse> warehouses = this.vWarehouseRepo.getAllWarehouse(pageable);
        if (products == null) {
            log.error("sellOnlineService :: warehouse not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.warehouse.not.found"));
            return response;
        }
        for (VProduct product : products) {
            VWarehouse warehouse = this.vWarehouseRepo.findByProductId(product.getId());
            VImport sellOnlineItems = this.importRepo.getImportByWarehouseId(warehouse.getId());
            if (sellOnlineItems == null) {
                continue;
            }
            SellOnlineResult sellOnlineResult = new SellOnlineResult();
            sellOnlineResult.setImportId(sellOnlineItems.getId());
            sellOnlineResult.setProductCode(sellOnlineItems.getProductCode());
            sellOnlineResult.setProductName(this.productRepo.getProduct(warehouse.getProductId()).getProductName());
            sellOnlineResult.setProductQuantity(warehouse.getInAmount() - warehouse.getOutAmount());
            sellOnlineResult.setProductImage(this.fileUploadRepo.getURLById(sellOnlineItems.getFileId()));
            sellOnlineResults.add(sellOnlineResult);
        }
        PageItem pageItem = new PageItem();
        pageItem.setPage(pageNumber);
        pageItem.setPageSize(pageSize);
        pageItem.setTotalItems(Math.toIntExact(totalPage));
        pageItem.setTotalPages((int) Math.ceil((double) totalPage / pageSize));
        response.setSellOnlineResults(sellOnlineResults);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("");
        log.info("sellOnlineService :: End");
        return response;
    }

    private List<GetTypeProductItem> convertItem(List<VTypeProduct> typeProducts) {

        List<GetTypeProductItem> items = new ArrayList<GetTypeProductItem>();
        for (VTypeProduct product : typeProducts) {
            GetTypeProductItem item = new GetTypeProductItem();
            item.setId(product.getId());
            item.setName(product.getName());
            items.add(item);
        }
        return items;
    }

}
