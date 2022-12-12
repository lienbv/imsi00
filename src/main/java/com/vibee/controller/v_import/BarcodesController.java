//package com.vibee.controller.v_import;
//import com.vibee.utils.ZxingBarcodeGenerator;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.awt.image.BufferedImage;
//
//@RestController
//@RequestMapping("/vibee/api/v1/auth")
//@CrossOrigin("*")
//public class BarcodesController {
//
//    @GetMapping(value = "/barbecue/upca/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> barbecueUPCABarcode(@PathVariable("barcode") String barcode) throws Exception {
//        return okResponse(ZxingBarcodeGenerator.generateUPCABarcodeImage1(barcode));
//    }
//    @GetMapping(value = "/barbecue/ean13/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> barbecueEAN13Barcode(@PathVariable("barcode") String barcode)
//            throws Exception {
//        return okResponse(ZxingBarcodeGenerator.generateEAN13BarcodeImage1(barcode));
//    }
//    @GetMapping(value = "/barbecue/qr/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> barbecueBarcodeQr(@PathVariable("barcode") String barcode)
//            throws Exception {
//        return okResponse(ZxingBarcodeGenerator.generateEAN13BarcodeImage1(barcode));
//    }
//    @GetMapping(value = "/zxing/upca/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> zxingUPCABarcode(@PathVariable("barcode") String barcode) throws Exception {
//        return okResponse(ZxingBarcodeGenerator.generateUPCABarcodeImage(barcode));
//    }
//
//    @GetMapping(value = "/zxing/ean13/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> zxingEAN13Barcode(@PathVariable("barcode") String barcode) throws Exception {
//        return okResponse(ZxingBarcodeGenerator.generateEAN13BarcodeImage(barcode));
//    }
//
//    @PostMapping(value = "/zxing/code128", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> zxingCode128Barcode(@RequestBody String barcode) throws Exception {
//        return okResponse(ZxingBarcodeGenerator.generateCode128BarcodeImage(barcode));
//    }
//
//    @PostMapping(value = "/zxing/pdf417", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> zxingPDF417Barcode(@RequestBody String barcode) throws Exception {
//        return okResponse(ZxingBarcodeGenerator.generatePDF417BarcodeImage(barcode));
//    }
//
//    @PostMapping(value = "/zxing/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<BufferedImage> zxingQRCode(@RequestBody String barcode) throws Exception {
//        return okResponse(ZxingBarcodeGenerator.generateQRCodeImage(barcode));
//    }
//    private ResponseEntity<BufferedImage> okResponse(BufferedImage image) {
//        return new ResponseEntity<>(image, HttpStatus.OK);
//    }
//
//}
