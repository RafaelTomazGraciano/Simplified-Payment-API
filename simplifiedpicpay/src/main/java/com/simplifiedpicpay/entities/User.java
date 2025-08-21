package com.simplifiedpicpay.entities;

import com.simplifiedpicpay.dtos.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity(name="users")
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    @NotBlank(message = "Document is required")
    @Column(unique=true, nullable=false)
    private String document;

    @NotBlank(message = "Email is required")
    @Column(unique=true, nullable=false)
    private String email;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserDTO userDTO){
        this.name =  userDTO.name();
        this.password = userDTO.password();
        this.document = userDTO.document();
        this.email = userDTO.email();
        this.balance = userDTO.balance();
        this.userType = userDTO.userType();
    }
}
