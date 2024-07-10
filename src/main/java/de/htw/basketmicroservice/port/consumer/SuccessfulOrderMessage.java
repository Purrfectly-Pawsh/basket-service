package de.htw.basketmicroservice.port.consumer;

import lombok.Data;

import java.util.List;

@Data
public class SuccessfulOrderMessage {

    private String userId;
    private String email;
    private String address;
    private String invoiceUrl;
    private String totalCost;
    private List<OrderItem> items;

}
