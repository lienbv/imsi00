package com.vibee.controller.product;

import com.vibee.jedis.CreateProduct;
import com.vibee.jedis.Update;
import com.vibee.model.item.FilterItem;
import com.vibee.model.request.product.*;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.product.*;
import com.vibee.service.vproduct.CloseProductService;
import com.vibee.service.vproduct.CreateProductService;
import com.vibee.service.vproduct.GetProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/vibee/api/v1/product")
@CrossOrigin("*")
public class ProductController {

    private final GetProductService getProductService;
    private final CreateProductService createProductService;
    private final CloseProductService closeProductService;
    @Autowired
    public ProductController(GetProductService getProductService,
                             CreateProductService createProductService,
                             CloseProductService closeProductService){
        this.getProductService=getProductService;
        this.createProductService=createProductService;
        this.closeProductService=closeProductService;
    }
    @GetMapping("/view-stall")
    public SearchViewStallResponse viewStall(ViewStallRequest request){
        return this.getProductService.viewStall(request);
    }

    @GetMapping("/view-manage")
    public GetProductResponse viewManage(@RequestParam(name = "pagenumber") int pageNumberReq,
                                     @RequestParam(name = "pagesize") int pageSizeReq,
                                     @RequestParam(name = "typefilter") String typeFilterReq,
                                     @RequestParam(name = "valuefilter") String valueFilterReq,
                                     @RequestParam(name= "language") String languageReq,
                                     @RequestParam(name = "search") String searchReq) {
        GetProductRequest getProductRequest = new GetProductRequest();
        getProductRequest.setPageNumber(pageNumberReq);
        getProductRequest.setPageSize(pageSizeReq);
        getProductRequest.setFilter(new FilterItem(typeFilterReq, valueFilterReq));
        getProductRequest.setLanguage(languageReq);
        getProductRequest.setSearchText(searchReq);
        return this.getProductService.viewManage(getProductRequest);
    }

    @GetMapping("/create/info")
    public InfoCreateProductResponse info(@RequestParam(name = "language") String languageReq) {
        return this.createProductService.info(languageReq);
    }

    @GetMapping("/selected/{id}/{cartCode}")
    public SelectedProductResponse selectProduct(@PathVariable("id") String productId, @PathVariable("cartCode") String cartCode, @RequestParam(name = "language") String languageReq) {
        return this.createProductService.selectProduct(productId,cartCode,languageReq);
    }

    @PostMapping("/create")
    public CreateProductResponse CreateProduct(@RequestBody CreateProductRequest request) {
        return this.createProductService.create(request);
    }

    @PostMapping("/create/upload")
    public CreateProductResponse createProduct(@RequestParam("file") MultipartFile file, @RequestParam("language") String language){
        return this.createProductService.upload(file,language);
    }

    @PostMapping("/lock")
    public LockResponse lock(@RequestBody LockRequest request) {
        return this.closeProductService.lock(request);
    }

    @PostMapping("/unlock")
    public LockResponse unLock(@RequestBody LockRequest request) {
        return this.closeProductService.unLock(request);
    }

    @PostMapping("/delete")
    public DeleteProductResponse delete(@RequestBody DeleteProductRequest request, @Valid BindingResult result) {
        return this.closeProductService.DeleteProduct(request,result);
    }

    @GetMapping("/detail/{id}")
    public DetailProductResponse Detail(@PathVariable("id") int id, @RequestParam(name = "language") String languageReq) {
        return this.getProductService.detail(id,languageReq);
    }
    @PostMapping("/deleteCart/{cartCode}")
    public BaseResponse deleteCartCode(@PathVariable("cartCode") String cartCode, @RequestParam(name = "language") String languageReq) {
        return this.createProductService.deleteCart(cartCode,languageReq);
    }
    @PostMapping("/updateCart/{cartCode}")
    public CreateProduct updateCart(@RequestBody Update request,@PathVariable("cartCode") String cartCode) {
        return this.createProductService.updateCart(request,cartCode);
    }
    @GetMapping("/online")
    public GetHomeSellOnlineResponse selectProduct(@RequestParam(name = "language") String language){
        return this.getProductService.selectProduct(language);
    }

}
