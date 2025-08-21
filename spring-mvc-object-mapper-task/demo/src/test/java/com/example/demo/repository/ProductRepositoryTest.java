package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindById_thenReturnProduct() {
        Product product = new Product(null, "Тестовый продукт", "Описание", new BigDecimal("10.00"), 100);
        entityManager.persist(product);
        entityManager.flush();

        Optional<Product> found = productRepository.findById(product.getProductId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(product.getName());
    }
}