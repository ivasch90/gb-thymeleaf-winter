package ru.gb.gbthymeleafwinter.dao;


import org.springframework.stereotype.Component;
import ru.gb.gbthymeleafwinter.entity.Product;

import java.util.List;


public interface CartRepository {

    List<Product> showCart();
    void deleteProduct(Product product);
    void addProduct(Product product);
}
