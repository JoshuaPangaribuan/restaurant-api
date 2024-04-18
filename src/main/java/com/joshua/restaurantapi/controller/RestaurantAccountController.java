package com.joshua.restaurantapi.controller;

import com.joshua.restaurantapi.common.validation.HttpResponse;
import com.joshua.restaurantapi.common.validation.ValidationErrorResponse;
import jakarta.validation.Valid;
import com.joshua.restaurantapi.entity.RestaurantAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.joshua.restaurantapi.repository.RestaurantAccountRepository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/v1/restaurant")
public class RestaurantAccountController {
        private final RestaurantAccountRepository repository;

        public RestaurantAccountController(RestaurantAccountRepository repository) {
                this.repository = repository;
        }

        @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
        public ResponseEntity<?> registerRestaurantAccount(
                        @Valid @RequestBody RestaurantAccount restaurantAccount,
                        BindingResult result) {
                if (result.hasErrors()) {
                        String message = Optional.ofNullable(result.getFieldError().getDefaultMessage()).orElse("");
                        return new ResponseEntity<>(new ValidationErrorResponse(message), HttpStatus.BAD_REQUEST);
                }

                repository.save(restaurantAccount);
                return new ResponseEntity<>(restaurantAccount, HttpStatus.CREATED);
        }

        @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
        public ResponseEntity<?> loginRestaurantAccount(
                        @RequestBody RestaurantAccount restaurant) {
                RestaurantAccount account = repository
                                .findExistRestaurantByEmail(restaurant.getEmail())
                                .orElse(null);

                if (account == null) {
                        return new ResponseEntity<>(
                                        new ValidationErrorResponse("Restaurant account not found"),
                                        HttpStatus.NOT_FOUND);
                }

                if (!RestaurantAccount
                                .getSHA256(restaurant.getPassword())
                                .equals(account.getPassword())) {
                        return new ResponseEntity<>(
                                        new ValidationErrorResponse("Invalid password"),
                                        HttpStatus.UNAUTHORIZED);
                }

                return new ResponseEntity<>(new HttpResponse(
                                String.valueOf(HttpStatus.OK.value()), "Success"), HttpStatus.OK);
        }

        @DeleteMapping(value = "/close-account", consumes = "application/json", produces = "application/json")
        public ResponseEntity<?> closeRestaurantAccount(
                        @RequestBody RestaurantAccount restaurant) {
                RestaurantAccount account = repository
                                .findExistRestaurantByRestaurantID(restaurant.getRestaurantID())
                                .orElse(null);

                if (account == null) {
                        return new ResponseEntity<>(
                                        new ValidationErrorResponse("Restaurant account not found or already deleted"),
                                        HttpStatus.NOT_FOUND);
                }

                account.setDeletedAt(new Timestamp(new Date().getTime()));
                repository.save(account);

                return new ResponseEntity<>(new HttpResponse(
                                String.valueOf(HttpStatus.OK.value()), "Success"), HttpStatus.OK);
        }
}
