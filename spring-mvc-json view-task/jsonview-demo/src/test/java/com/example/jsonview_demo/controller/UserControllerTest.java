package com.example.jsonview_demo.controller;

import com.example.jsonview_demo.model.Order;
import com.example.jsonview_demo.model.User;
import com.example.jsonview_demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;
    private Order order;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        order = new Order();
        order.setId(101L);
        order.setItem("Laptop");
        order.setAmount(1200.00);

        List<Order> orders = new ArrayList<>();
        orders.add(order);
        user.setOrders(orders);
    }

    @Test
    void whenGetAllUsers_thenReturnUserSummaryView() throws Exception {
        given(userService.getAllUsers()).willReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].orders").doesNotExist());
    }

    @Test
    void whenGetUserById_thenReturnUserDetailsView() throws Exception {
        given(userService.getUserById(1L)).willReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.orders").exists())
                .andExpect(jsonPath("$.orders[0].item").value("Laptop"));
    }
}