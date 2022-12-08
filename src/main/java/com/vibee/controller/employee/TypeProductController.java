package com.vibee.controller.employee;

import com.vibee.model.item.FilterItem;
import com.vibee.model.request.category.*;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.category.SelectionTypeProductItems;
import com.vibee.model.response.category.SelectionTypeProductItemsResponse;
import com.vibee.model.response.category.TypeItems;
import com.vibee.model.response.category.TypeProductItemsResponse;
import com.vibee.service.vemployee.ITypeProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/vibee/api/v1/auth")
@CrossOrigin(origins = "*")
public class TypeProductController {
    private final ITypeProductService typeProductService;

    @Autowired
    private TypeProductController(ITypeProductService typeProductService) {
        this.typeProductService = typeProductService;
    }

    @GetMapping(value = "getAllType1")
    public TypeItems getAllType1(@RequestParam(name = "pagenumber") int pageNumberReq,
                                 @RequestParam(name = "pagesize") int pageSizeReq,
                                 @RequestParam(name = "typefilter") String typeFilterReq,
                                 @RequestParam(name = "valuefilter") String valueFilterReq,
                                 @RequestParam(name= "language") String languageReq,
                                 @RequestParam(name = "search") String searchReq){
        TypeProductRequest request = new TypeProductRequest();
        request.setPageNumber(pageNumberReq);
        request.setPageSize(pageSizeReq);
        request.setFilter(new FilterItem(typeFilterReq, valueFilterReq));
        request.setLanguage(languageReq);
        request.setSearchText(searchReq);
        return this.typeProductService.getAll1(request);
    }

    @GetMapping(value = "getAllType")
    public TypeProductItemsResponse getAllType(){
        return this.typeProductService.getAll();
    }
    @PostMapping(value = "createTyProduct")
    public BaseResponse createTyProduct(@Valid @RequestBody CreateTypeProductRequest request, BindingResult bindingResult){
        return this.typeProductService.createType(request);
    }
    @GetMapping(value = "getAllSelectType")
    public SelectionTypeProductItemsResponse getAllSelectType(){
        return this.typeProductService.getAllSelect();
    }
    @PostMapping(value = "deleteType")
    public BaseResponse delete(@Valid @RequestBody DeleteTypeProductRequest request){
        return this.typeProductService.delete(request);
    }
    @PostMapping(value = "updateType")
    public BaseResponse update(@Valid @RequestBody UpdateTypeProductRequest request){
        return this.typeProductService.update(request);
    }

    @GetMapping (value = "editType/{id}")
    public EditTypeProductResponse edit(@PathVariable(name = "id") int id) {
        DeleteTypeProductRequest request = new DeleteTypeProductRequest();
        request.setId(id);
        return typeProductService.edit(request);
    }
    @GetMapping(value = "getDetailParentById/{id}")
    public SelectionTypeProductItems getDetailParentById(@PathVariable(name = "id") int id){
        return this.typeProductService.getAllSelectDetail(id);
    }
    @PostMapping(value = "createTyProductDetail")
    public BaseResponse createTyProductDetail(@Valid @RequestBody CreateTypeProductDetailRequest request, BindingResult bindingResult){
        return this.typeProductService.createTypeDetail(request);
    }
}
