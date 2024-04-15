package com.joshua.restaurantapi.common.RandomIDGenerator;

public class APIKeyGenerator extends IDGenerator{
    public String generateRandomString(int length) {
        return super.generateRandomString(length).toString().toUpperCase();
    }
}
