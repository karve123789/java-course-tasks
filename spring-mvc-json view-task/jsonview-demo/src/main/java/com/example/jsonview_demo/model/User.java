package com.example.jsonview_demo.model;

import com.example.jsonview_demo.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserSummary.class)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @JsonView(Views.UserSummary.class)
    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    @JsonView(Views.UserSummary.class)
    private String email;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Views.UserDetails.class)
    private List<Order> orders;
}
