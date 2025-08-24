package com.example.InteriorsECM.controller;

import com.example.InteriorsECM.converter.ProductConverter;
import com.example.InteriorsECM.dto.ProductDTO;
import com.example.InteriorsECM.model.mysql.Product;
import com.example.InteriorsECM.model.mysql.Product_image;
import com.example.InteriorsECM.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;

    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/san-pham")
    public String products(
            @RequestParam(value = "sortby", required = false) String sortBy,
            @RequestParam(value = "searchQuery", required = false) String searchQuery,
            @RequestParam(value = "editId", required = false) Integer editId,
            Model model) {
        System.out.print("HELLO");
        System.out.println(editId);
        // Initialize product list
        List<ProductDTO> products;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            products = productService.searchProductsByName(searchQuery);
        } else {
            products = productService.findAllProducts();
        }

        // Apply sorting if requested
        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "giaban":
                    products = productService.sortByPrice();
                    break;
                case "hangtrongkho":
                    products = productService.sortByStock();
                    break;
            }
        }

        // Add attributes for product list and search/sort state
        model.addAttribute("products", products);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("sortBy", sortBy);

        // Initialize DTOs for modals
        model.addAttribute("createProductDTO", new ProductDTO());
        model.addAttribute("showCreateModal", false);

        // Handle edit modal
        if (editId != null) {
            try {
                ProductDTO editProductDTO = productService.findProductById(editId);
                model.addAttribute("editProductDTO", editProductDTO);
                model.addAttribute("showEditModal", true);
            } catch (Exception e) {
                model.addAttribute("editError", "Unable to load product: " + e.getMessage());
                model.addAttribute("editProductDTO", new ProductDTO());
                model.addAttribute("showEditModal", false);
            }
        } else {
            model.addAttribute("editProductDTO", new ProductDTO());
            model.addAttribute("showEditModal", false);
        }

        return "admin/admin-pages/products-manager";
    }

    @PostMapping("/san-pham/create")
    public String createProduct(
            @ModelAttribute("createProductDTO") ProductDTO createProductDTO,
            @RequestParam(value = "primaryImageFile", required = false) MultipartFile primaryImageFile,
            @RequestParam(value = "productImageFiles", required = false) MultipartFile[] productImageFiles,
            Model model) {
        try {
            // Xử lý ảnh đại diện (primary image)
            if (primaryImageFile != null && !primaryImageFile.isEmpty()) {
                String fileName = saveImage(primaryImageFile);
                createProductDTO.setPrimary_image("/assets/img/product_image/" + fileName);
            } else if (createProductDTO.getPrimary_image() == null || createProductDTO.getPrimary_image().isEmpty()) {
                createProductDTO.setPrimary_image("");
            }

            // Xử lý danh sách ảnh phụ
            List<Product_image> productImages = createProductDTO.getProduct_images();
            if (productImages != null) {
                for (int i = 0; i < productImages.size(); i++) {
                    Product_image image = productImages.get(i);
                    MultipartFile uploadedFile = (productImageFiles != null && i < productImageFiles.length) ? productImageFiles[i] : null;

                    if (uploadedFile != null && !uploadedFile.isEmpty()) {
                        String fileName = saveImage(uploadedFile);
                        image.setImage_url("/assets/img/product_image/" + fileName);
                    } else if (image.getImage_url() == null || image.getImage_url().isEmpty()) {
                        image.setImage_url("");
                    }
                }
            }

            // Chuyển DTO sang entity
            Product product = ProductConverter.mapToEntity(createProductDTO);

            // Gán ngược Product cho từng ảnh phụ
            if (product.getProduct_images() != null) {
                for (Product_image image : product.getProduct_images()) {
                    image.setProduct(product);
                }
            }

            // Lưu vào database
            productService.createProduct(createProductDTO);

            return "redirect:/admin/san-pham";
        } catch (Exception e) {
            model.addAttribute("createError", "Error creating product: " + e.getMessage());
            model.addAttribute("createProductDTO", createProductDTO);
            model.addAttribute("editProductDTO", new ProductDTO());
            model.addAttribute("products", productService.findAllProducts());
            model.addAttribute("showCreateModal", true);
            model.addAttribute("showEditModal", false);
            return "admin/admin-pages/products-manager";
        }
    }


    @PostMapping("/san-pham")
    public String updateProduct(
            @ModelAttribute("editProductDTO") ProductDTO editProductDTO,
            @RequestParam(value = "primaryImageFile", required = false) MultipartFile primaryImageFile,
            @RequestParam(value = "productImageFiles", required = false) MultipartFile[] productImageFiles,
            Model model) {
        try {
            // Handle primary image
            if (primaryImageFile != null && !primaryImageFile.isEmpty()) {
                String fileName = saveImage(primaryImageFile);
                editProductDTO.setPrimary_image("/assets/img/product_image/" + fileName);
            } else if (editProductDTO.getPrimary_image() == null || editProductDTO.getPrimary_image().isEmpty()) {
                editProductDTO.setPrimary_image("");
            } // Keep HTTP URL if provided

            // Handle additional product images
            List<Product_image> productImages = editProductDTO.getProduct_images();
            if (productImages != null) {
                for (int i = 0; i < productImages.size(); i++) {
                    Product_image image = productImages.get(i);
                    MultipartFile uploadedFile = (productImageFiles != null && i < productImageFiles.length) ? productImageFiles[i] : null;
                    if (uploadedFile != null && !uploadedFile.isEmpty()) {
                        String fileName = saveImage(uploadedFile);
                        image.setImage_url("/assets/img/product_image/" + fileName);
                    } else if (image.getImage_url() == null || image.getImage_url().isEmpty()) {
                        image.setImage_url("");
                    } // Keep HTTP URL if provided
                }
            }

            // Update product in the database
            productService.updateProduct(editProductDTO.getProduct_id(), editProductDTO);
            return "redirect:/admin/san-pham";
        } catch (Exception e) {
            model.addAttribute("editError", "Error saving product: " + e.getMessage());
            model.addAttribute("editProductDTO", editProductDTO);
            model.addAttribute("createProductDTO", new ProductDTO());
            model.addAttribute("products", productService.findAllProducts());
            model.addAttribute("showCreateModal", false);
            model.addAttribute("showEditModal", true);
            return "admin/admin-pages/products-manager";
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        String uploadDir = "D:/my projects/InteriorsECM/src/main/resources/static/assets/img/product_image";
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    @GetMapping("/san-pham/{id}/delete")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return "redirect:/admin/san-pham";
    }
}