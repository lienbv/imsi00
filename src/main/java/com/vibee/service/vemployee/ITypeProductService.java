package com.vibee.service.vemployee;

import com.vibee.entity.VTypeProduct;
import com.vibee.model.request.category.*;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.category.*;

import java.util.List;

public interface ITypeProductService {
    TypeProductItemsResponse getAll();
    List<TypeItemsDto> getParentChild(List<VTypeProduct> list, String language);
    TypeItems getAll1(TypeProductRequest request);
    BaseResponse createType(CreateTypeProductRequest request);
    SelectionTypeProductItemsResponse getAllSelect();
    List<SelectionTypeProductItems> getAllSelected();
    BaseResponse delete(DeleteTypeProductRequest request);
    BaseResponse update(UpdateTypeProductRequest request);
    EditTypeProductResponse edit(DeleteTypeProductRequest request);
    SelectionTypeProductItems getAllSelectDetail(int id);
    BaseResponse createTypeDetail(CreateTypeProductDetailRequest request);

}
