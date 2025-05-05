package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.dto.ProductDto;
import com.example.InteriorsECM.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class ProductController {
    ProductService productService;
    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/menu")
    public String menu(){
        return "index";
    }

    @GetMapping("/menu/products")
    public String products(@RequestParam(value = "name", required = false) String productName, Model model){
        List<ProductDto> products = productName == null ?
                                    productService.findAllProducts()
                                    :productService.searchProductsByName(productName);
        model.addAttribute("products", products);
        return "products";
    }
    @GetMapping("/menu/products/{productId}/product-detail")
    public String productDetail(@PathVariable("productId") int product_id, Model model){
        ProductDto productDto = productService.findProductById(product_id);
        model.addAttribute("product", productDto);
        return "product-detail";
    }
}
