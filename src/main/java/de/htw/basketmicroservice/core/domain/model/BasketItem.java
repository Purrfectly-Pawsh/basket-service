package de.htw.basketmicroservice.core.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(BasketItemKey.class)
public class BasketItem {

    @Id
    private UUID basketItemId;
    @Id
    private UUID basketId;
    @Column(length = 500)
    private String name;
    private BigDecimal unitPrice;
    @Column(length = 1000)
    private String imageUrl;
    private int quantity;

    public BasketItemKey getKey() {
         return new BasketItemKey(basketId, basketItemId);
    }

}
