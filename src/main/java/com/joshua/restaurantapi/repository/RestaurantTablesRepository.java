package com.joshua.restaurantapi.repository;

import com.joshua.restaurantapi.model.RestaurantTables;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTablesRepository extends JpaRepository<RestaurantTables, String> {
}
