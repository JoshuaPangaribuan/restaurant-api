package com.joshua.restaurantapi.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.joshua.restaurantapi.common.QRGenerator.QRGenerator;
import com.joshua.restaurantapi.common.QRGenerator.ZXingQRCode;
import com.joshua.restaurantapi.common.validation.HttpResponse;
import com.joshua.restaurantapi.component.ApplicationContextProvider;
import com.joshua.restaurantapi.entity.RestaurantAccount;
import com.joshua.restaurantapi.entity.RestaurantTables;
import com.joshua.restaurantapi.repository.RestaurantAccountRepository;
import com.joshua.restaurantapi.repository.RestaurantTablesRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/restaurant/tables")
public class RestaurantTablesController {

    public final ApplicationContextProvider contextProvider;
    public final RestaurantTablesRepository tablesRepository;

    public final RestaurantAccountRepository accountRepository;

    public QRGenerator qrGenerator;

    public RestaurantTablesController(RestaurantTablesRepository tablesRepository,
            RestaurantAccountRepository accountRepository,
            ApplicationContextProvider contextProvider) {
        this.tablesRepository = tablesRepository;
        this.accountRepository = accountRepository;
        this.contextProvider = contextProvider;
    }

    @PostConstruct
    public void init() {
        this.qrGenerator = ApplicationContextProvider.getApplicationContext().getBean(ZXingQRCode.class);
    }

    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createRestaurantTable(
            @RequestBody TablesRequest table) {
        Optional<RestaurantAccount> account = accountRepository.findById(table.getRestaurantID());
        if (account.isPresent()) {
            table.getTable().setRestaurantAccount(account.get());
            tablesRepository.save(table.getTable());

            return new ResponseEntity<>(new HttpResponse(String.valueOf(HttpStatus.CREATED.value()),
                    "Successfully"), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new HttpResponse(String.valueOf(HttpStatus.NOT_FOUND.value()),
                "Successfully"), HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/bulk-create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createRestaurantTables(
            @RequestBody BulkRequest tables) {
        Optional<RestaurantAccount> account = accountRepository.findById(tables.getRestaurantID());
        if (account.isPresent()) {
            tables.getTable().forEach(table -> table.setRestaurantAccount(account.get()));
            tablesRepository.saveAll(tables.getTable());

            return new ResponseEntity<>(new HttpResponse(String.valueOf(HttpStatus.CREATED.value()),
                    "Successfully"), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new HttpResponse(String.valueOf(HttpStatus.NOT_FOUND.value()),
                "Successfully"), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> deleteRestaurantTable(
            @RequestBody TablesRequest table) throws Exception {
        Optional<RestaurantTables> restaurantTable = tablesRepository.findById(table.getTable().getTableID());
        if (restaurantTable.isPresent()) {
            qrGenerator.deleteQRCode(restaurantTable.get().getQrPath());
            tablesRepository.delete(restaurantTable.get());
            return new ResponseEntity<>(new HttpResponse(String.valueOf(HttpStatus.OK.value()),
                    "Successfully"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new HttpResponse(String.valueOf(HttpStatus.NOT_FOUND.value()),
                "Successfully"), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/bulk-delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> deleteRestaurantTables(
            @RequestBody BulkRequest tables) throws Exception {
        List<RestaurantTables> restaurantTables = tablesRepository
                .findAllById(tables.getTable().stream().map(RestaurantTables::getTableID).toList());

        if (!restaurantTables.isEmpty()) {

            qrGenerator.deleteBulkQRCode(restaurantTables
                    .stream().map(RestaurantTables::getQrPath)
                    .toArray(String[]::new));

            tablesRepository.deleteAll(restaurantTables);

            return new ResponseEntity<>(new HttpResponse(String.valueOf(HttpStatus.OK.value()),
                    "Successfully"), HttpStatus.OK);
        }

        return new ResponseEntity<>(new HttpResponse(String.valueOf(HttpStatus.NOT_FOUND.value()),
                "Successfully"), HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/get-all/{restaurantID}", produces = "application/json")
    public ResponseEntity<?> getRestaurantTables(
            @PathVariable("restaurantID") String restaurantID) {
        Optional<RestaurantAccount> account = accountRepository.findById(restaurantID);
        if (account.isPresent()) {
            return new ResponseEntity<>(account.get().getRestaurantTables(), HttpStatus.OK);
        }

        return new ResponseEntity<>(new HttpResponse(String.valueOf(HttpStatus.NOT_FOUND.value()),
                "Successfully"), HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/view-qr")
    public ResponseEntity<?> viewQRCode(
            @RequestParam("qrPath") String qrPath) {
        HttpHeaders headers = new HttpHeaders();
        try {
            byte[] qrBytes = this.qrGenerator.viewQRCode(qrPath);
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(qrBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(new HttpResponse(String.valueOf(HttpStatus.NOT_FOUND.value()),
                    "QR Code not found."), headers, HttpStatus.NOT_FOUND);
        }
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class TablesRequest {
    @JsonProperty("restaurant_id")
    private String restaurantID;

    @JsonProperty("table")
    private RestaurantTables table;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class BulkRequest {
    @JsonProperty("restaurant_id")
    private String restaurantID;

    @JsonProperty("tables")
    private List<RestaurantTables> table;
}
