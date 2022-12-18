package com.vibee.controller.product;

import com.vibee.jedis.CreateProduct;
import com.vibee.jedis.Update;
import com.vibee.model.item.FilterItem;
import com.vibee.model.request.product.*;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.product.*;
import com.vibee.service.vproduct.CloseProductService;
import com.vibee.service.vproduct.SaveProductService;
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
    private final SaveProductService saveProductService;
    private final CloseProductService closeProductService;
    @Autowired
    public ProductController(GetProductService getProductService,
                             SaveProductService saveProductService,
                             CloseProductService closeProductService){
        this.getProductService=getProductService;
        this.saveProductService=saveProductService;
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
        return this.saveProductService.info(languageReq);
    }

    @GetMapping("/selected/{id}/{cartCode}")
    public SelectedProductResponse selectProduct(@PathVariable("id") String productId, @PathVariable("cartCode") String cartCode, @RequestParam(name = "language") String languageReq) {
        return this.saveProductService.selectProduct(productId,cartCode,languageReq);
    }

    @PostMapping("/create")
    public CreateProductResponse CreateProduct(@RequestBody CreateProductRequest request) {
        return this.saveProductService.create(request);
    }

    @PostMapping("/create/upload")
    public CreateProductResponse createProduct(@RequestParam("file") MultipartFile file, @RequestParam("language") String language){
        return this.saveProductService.upload(file,language);
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
        return this.saveProductService.deleteCart(cartCode,languageReq);
    }
    @PostMapping("/updateCart/{cartCode}")
    public CreateProduct updateCart(@RequestBody Update request,@PathVariable("cartCode") String cartCode) {
        return this.saveProductService.updateCart(request,cartCode);
    }
    @GetMapping("/online")
    public GetHomeSellOnlineResponse selectProduct(@RequestParam(name = "language") String language){
        return this.getProductService.selectProduct(language);
    }

    @GetMapping("/update/info/{id}")
    public InfoUpdateProductResponse infoUpdate(@PathVariable("id") int id, @RequestParam(name = "language") String languageReq) {
        return this.getProductService.infoUpdate(id, languageReq);
    }

    @PostMapping("/update")
    public UpdateProductResponse updateProduct(@RequestBody UpdateProductRequest request, @Valid BindingResult result) {
        return this.saveProductService.UpdateProduct(request, result);
    }

    @PostMapping("/update/upload/{id}")
    public UpdateProductResponse updateProduct(@RequestParam("file") MultipartFile file,@PathVariable("id") int id,@RequestParam("language") String language){
        return this.saveProductService.updateUpload(file,id,language);
    }

    @GetMapping("/get-product-sell-online")
    public SellOnlineResponse sellOnline(@RequestParam(name = "page-number") int pageNumberReq,
                                         @RequestParam(name = "page-size") int pageSizeReq,
                                         @RequestParam(name= "language") String languageReq,
                                         @RequestParam(name = "search") String searchReq) {
        return this.getProductService.sellOnline(languageReq, pageNumberReq, pageSizeReq, searchReq);
    }
}
