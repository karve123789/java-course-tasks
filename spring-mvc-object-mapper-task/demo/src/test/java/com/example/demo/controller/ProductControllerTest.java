package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product1;
    private Product product2;

    @BeforeEach
        // Этот метод будет выполняться перед каждым тестом
    void setUp() {
        product1 = new Product(1L, "Ноутбук", "Мощный ноутбук", new BigDecimal("999.99"), 10);
        product2 = new Product(2L, "Мышь", "Беспроводная мышь", new BigDecimal("25.50"), 50);
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> allProducts = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(allProducts);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Ноутбук")));
    }

    @Test
    void testGetProductById_Success() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.name", is("Ноутбук")));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        // Преобразуем объект в JSON-строку
                        .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ноутбук")));
    }
}