package com.example.jsonview_demo.model;

import com.example.jsonview_demo.view.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserDetails.class)
    private Long id;

    @JsonView(Views.UserDetails.class)
    private String item;

    @JsonView(Views.UserDetails.class)
    private Double amount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
