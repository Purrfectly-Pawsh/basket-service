package de.htw.basketmicroservice.port.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMessage {

    private String productId;
    private String userId;
    private String name;
    private BigDecimal unitPrice;
    private String description;
    private String imageUrl;
    private int quantity;

}
