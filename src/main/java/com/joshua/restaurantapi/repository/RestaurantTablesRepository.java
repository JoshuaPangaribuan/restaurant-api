package com.joshua.restaurantapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joshua.restaurantapi.entity.RestaurantTables;

public interface RestaurantTablesRepository extends JpaRepository<RestaurantTables, String> {
}
