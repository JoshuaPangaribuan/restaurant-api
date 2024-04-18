package com.joshua.restaurantapi;

import com.joshua.restaurantapi.entity.RestaurantAccount;
import com.joshua.restaurantapi.repository.RestaurantAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class RestaurantAccountRepositoryTest {

    @Mock
    private RestaurantAccountRepository repository;

    @Mock
    private RestaurantAccount account;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return restaurant account when restaurant ID exists")
    public void shouldReturnRestaurantAccountWhenRestaurantIDExists() {
        Mockito.when(repository.findExistRestaurantByRestaurantID("123")).thenReturn(Optional.of(account));
        Optional<RestaurantAccount> result = repository.findExistRestaurantByRestaurantID("123");
        assertTrue(result.isPresent());
        Mockito.verify(repository, Mockito.times(1)).findExistRestaurantByRestaurantID("123");
    }
}
