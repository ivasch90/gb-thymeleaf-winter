package ru.gb.gbthymeleafwinter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.gbthymeleafwinter.entity.Product;
import ru.gb.gbthymeleafwinter.service.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public String getProductList(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product-list";
    }

    @GetMapping
    public String showForm(Model model, @RequestParam(name = "id", required = false) Long id) {
        Product product;

        if (id != null) {
            product = productService.findById(id);
        } else {
            product = new Product();
        }
        model.addAttribute("product", product);
        return "product-form";
    }

    @PostMapping
    public String saveProduct(Product product) {
        productService.save(product);
        return "redirect:/product/all";
    }

    @GetMapping("/delete")
    public String deleteById(@RequestParam(name = "id") Long id) {
        productService.deleteById(id);
        return "redirect:/product/all";
    }

    @GetMapping("/cart")
    public String showCart (Model model) {
        List<Product> products = productService.showCart();
        model.addAttribute("products", products);
        return "cart";
    }

    @GetMapping("/cart/add")
    public String addToCart(@RequestParam(name = "id") Long id) {
        if (id != null) {
            productService.addProductToCart(id);
        }
        return "redirect:/product/cart";
    }

    @GetMapping("/cart/delete")
    public String deleteFromCart(@RequestParam(name = "id") Long id) {
        productService.deleteFromCartById(id);
        return "redirect:/product/cart";
    }

}
