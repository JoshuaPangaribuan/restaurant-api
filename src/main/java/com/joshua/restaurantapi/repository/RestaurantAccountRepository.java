package com.joshua.restaurantapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.joshua.restaurantapi.entity.RestaurantAccount;

import java.util.Optional;

public interface RestaurantAccountRepository extends JpaRepository<RestaurantAccount, String> {
    @Query(value = "SELECT * FROM restaurant_account r WHERE r.restaurant_id = ?1 AND r.deleted_at IS NULL", nativeQuery = true)
    Optional<RestaurantAccount> findExistRestaurantByRestaurantID(String restaurantID);

    @Query(value = "SELECT * FROM restaurant_account r WHERE r.email = ?1 AND r.deleted_at IS NULL", nativeQuery = true)
    Optional<RestaurantAccount> findExistRestaurantByEmail(String email);
}
