package de.htw.basketmicroservice.core.domain.service.dto;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BasketDTO {
    private String basketId;
    private BigDecimal totalPrice;
    private List<BasketItem> basketItems;
}
