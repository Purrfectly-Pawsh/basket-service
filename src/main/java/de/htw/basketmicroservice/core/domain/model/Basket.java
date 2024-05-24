package de.htw.basketmicroservice.core.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Basket {

    @Id
    private Long id;
    @OneToMany
    private List<BasketItem> basketItems;

    public Basket(Long userId) {
        this.id = userId;
        this.basketItems = new ArrayList<>();
    }

    public void addBasketItem(BasketItem basketItem) {
        basketItems.add(basketItem);
    }

}
