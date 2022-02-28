package ru.gb.gbthymeleafwinter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.gbthymeleafwinter.dao.CartRepository;
import ru.gb.gbthymeleafwinter.dao.ProductDao;
import ru.gb.gbthymeleafwinter.entity.Product;
import ru.gb.gbthymeleafwinter.entity.enums.Status;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductDao productDao;
    private final CartRepository cartRepository;

    public Product save(Product product) {
        if (product.getId() != null) {
            Optional<Product> productFromDbOptional = productDao.findById(product.getId());
            if (productFromDbOptional.isPresent()) {
                Product productFromDb = productFromDbOptional.get();
                productFromDb.setTitle(product.getTitle());
                productFromDb.setDate(product.getDate());
                productFromDb.setCost(product.getCost());
                productFromDb.setStatus(product.getStatus());
                return productDao.save(productFromDb);
            }
        }
        return productDao.save(product);
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productDao.findAll();
    }

    public void deleteById(Long id) {
        try {
            productDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("There isn't product with id {}", id);
        }

    }








    public void disableById(Long id) {
        productDao.findById(id).ifPresent(
                p -> {
                    p.setStatus(Status.DISABLE);
                    productDao.save(p);
                }
        );
    }

    public List<Product> findAllActive() {
        return productDao.findAllByStatus(Status.ACTIVE);
    }


    public List<Product> findAllActive(int page, int size) {
        return productDao.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size));
    }

    public List<Product> findAllActiveSortedById(Sort.Direction direction) {
        return productDao.findAllByStatus(Status.ACTIVE, Sort.by(direction, "id"));
    }

    public List<Product> findAllActiveSortedById(int page, int size, Sort.Direction direction) {
        return productDao.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size, Sort.by(direction, "id")));
    }

    @Transactional(propagation = Propagation.NEVER)
    public long count() {
        System.out.println(productDao.count());
        // какая-то логика
        return productDao.count();
    }

    public void addProductToCart(Long id) {
        if (id != null) {
            Product productFromDb = productDao.findById(id).orElse(null);
            if (productFromDb == null) throw new AssertionError();
            cartRepository.addProduct(productFromDb);
        }
    }

    public List<Product> showCart() {
        return cartRepository.showCart();
    }

    public void deleteFromCartById(Long id) {
        List<Product> list = cartRepository.showCart();
        for (Product product : list) {
            if (product.getId().equals(id)) {
                cartRepository.deleteProduct(product);
                break;
            }
        }

    }
}
