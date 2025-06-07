package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.dto.ProductDto;
import com.example.InteriorsECM.model.mysql.UserPrincipal;
import com.example.InteriorsECM.service.AccessLogService;
import com.example.InteriorsECM.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
public class ProductController {
    public ProductService productService;
    public AccessLogService accessLogService;

    @Autowired
    public ProductController(ProductService productService, AccessLogService accessLogService){
        this.accessLogService = accessLogService;
        this.productService = productService;
    }
    @GetMapping("/menu")
    public String menu(HttpServletRequest request){
        // Log the access event
        Long userId = getUserIdFromAuthentication();
        String ipAddress = getClientIp(request);
        System.out.println(" IP Address: " + ipAddress);
        System.out.println(" user id: " + userId);
        accessLogService.logAccessEvent(userId, "view_menu", ipAddress, true);
        return "index";
    }

    @GetMapping("/menu/products")
    public String products(@RequestParam(value = "name", required = false) String productName, Model model){
        List<ProductDto> products = productName == null ?
                                    productService.findAllProducts()
                                    :productService.searchProductsByName(productName);
        model.addAttribute("products", products);
        return "products.html";
    }
    @GetMapping("/menu/products/{productId}/product-detail")
    public String productDetail(@PathVariable("productId") int product_id, Model model){
        ProductDto productDto = productService.findProductById(product_id);
        model.addAttribute("product", productDto);
        return "product-detail";
    }

    private Long getUserIdFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
            return (long)userDetails.getUser().getUser_id();
        }
        return 0L;
    }
    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
