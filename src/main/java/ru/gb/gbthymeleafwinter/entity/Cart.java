package ru.gb.gbthymeleafwinter.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gb.gbthymeleafwinter.dao.CartRepository;

import java.util.*;


@Component
@RequiredArgsConstructor
@Getter
public class Cart implements CartRepository {


    private final List<Product> products = new ArrayList<>();


    public List<Product> showCart() {
        return products;
    }

    @Override
    public void deleteProduct(Product product) {
        products.remove(product);
    }

    @Override
    public void addProduct(Product product) {
        products.add(product);
    }

}
