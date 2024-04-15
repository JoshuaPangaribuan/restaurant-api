package com.joshua.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joshua.restaurantapi.common.RandomIDGenerator.APIKeyGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "restaurant_account")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestaurantAccount {

    @Id
    @Column(name = "restaurant_id")
    @GeneratedValue(generator = "IDGenerator")
    @GenericGenerator(name = "IDGenerator", strategy = "com.joshua.restaurantapi.common.RandomIDGenerator.IDGenerator")
    private String restaurantID;

    @Column(name = "name")
    @NotBlank(message = "Restaurant name cannot be blank")
    @JsonProperty("restaurant_name")
    private String restaurantName;

    @Column(name = "email")
    @Email(message = "Invalid Email")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(name = "api_key", updatable = false)
    private String apiKey;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    @JsonManagedReference
    private List<RestaurantTables> restaurantTables = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonAlias("created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonAlias("updated_at")
    private Timestamp updatedAt;

    @JsonIgnore
    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    private void prePersistAction() {
        this.apiKey = new APIKeyGenerator().generateRandomString(24);
        this.password = getSHA256(this.password);
    }

    public static String getSHA256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));

            while(hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
