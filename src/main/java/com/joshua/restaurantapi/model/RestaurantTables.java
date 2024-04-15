package com.joshua.restaurantapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joshua.restaurantapi.common.QRGenerator.QRGenerator;
import com.joshua.restaurantapi.common.QRGenerator.ZXingQRCode;
import com.joshua.restaurantapi.component.ApplicationContextProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "restaurant_tables")
public class RestaurantTables {

    @Id
    @Column(name = "table_id")
    @GeneratedValue(generator = "IDGenerator")
    @GenericGenerator(name = "IDGenerator", strategy = "com.joshua.restaurantapi.common.RandomIDGenerator.IDGenerator")
    @JsonProperty("table_id")
    private String tableID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", updatable = false)
    @JsonBackReference
    private RestaurantAccount restaurantAccount;

    @Column(name = "qr_path")
    private String qrPath;

    @Column(name = "section")
    @JsonAlias("floor_level")
    private String section;

    @Column(name = "occupied")
    private Boolean occupied;

    @Column(name = "floor_level")
    @JsonProperty("floor_level")
    @JsonAlias("floor_level")
    private Integer floorLevel;

    @CreationTimestamp
    @Column(name = "created_at",insertable = false, updatable = false)
    private String createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private String updatedAt;

    @PrePersist
    private void prePersistAction() throws Exception{
        this.occupied = false;
        QRGenerator qrGenerator = ApplicationContextProvider.getApplicationContext().getBean(ZXingQRCode.class);
        this.qrPath = qrGenerator.generateQRCode(
                this.restaurantAccount.getRestaurantID() + "/" + this.tableID,
                500,500, "qr-" + this.tableID);
    }

}
