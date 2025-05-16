package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.dto.ProductDto;
import com.example.InteriorsECM.model.Product;
import com.example.InteriorsECM.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {
    ProductService productService;

    @Autowired
    public AdminController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(){
        return "/admin/dashboard";
    }
    @GetMapping("/admin/san-pham")
    public String products(@RequestParam(value="sortby", required = false) String ofType,
                           @RequestParam(value="searchQuery", required = false) String searchQuery,
                           Model model){
        List<ProductDto> products;
        String sortType = ofType;
        products = productService.findAllProducts();
        if(searchQuery != null){
            products = productService.searchProductsByName(searchQuery);
        }
        if(ofType != null){
            switch(sortType){
                case "giaban":
                    products = productService.sortByPrice();
                    break;
                case "hangtrongkho":
                    products = productService.sortByStock();
                    break;
            }
        }
        model.addAttribute("products", products);
        return "/admin/admin-pages/products-manager.html";
    }
}
