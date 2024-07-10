package de.htw.basketmicroservice.port.consumer;

import lombok.Data;

@Data
public class OrderItem {

    private String productId;
    private String description;
    private long quantity;
    private String imageUrl;
    private Double price;

}
