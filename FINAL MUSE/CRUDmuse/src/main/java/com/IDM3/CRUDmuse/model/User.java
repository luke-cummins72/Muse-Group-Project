package com.IDM3.CRUDmuse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_type", length=50)
    private String userType;

    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", length = 100)
    private String email;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @NotBlank(message = "Surname is required")
    @Size(max = 100, message = "Surname must not exceed 100 characters")
    @Column(name = "surname", length = 100, nullable = false)
    private String surname;

    @NotBlank(message = "Password is required\n")
    @Size(max = 400, message = "Password must not exceed 400 characters\n")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}", message = "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, and one number.\n")
    @Column(name = "password", length = 400)
    private String password;

    @NotBlank(message = "Username is required")
    @Size(max = 200, message = "Username must not exceed 100 characters")
    @Column(name = "username", length = 200, nullable = false)
    private String userName;

    @NotBlank(message = "Biography is required")
    @Size(max = 100, message = "Biography must not exceed 1000 characters")
    @Column(name = "bio", length = 1000, nullable = true)
    private String bio;

    @Column(name = "user_image", length = 255)
    private String userImage;


}


