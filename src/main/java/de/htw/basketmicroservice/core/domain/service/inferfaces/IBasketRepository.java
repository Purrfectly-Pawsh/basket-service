package de.htw.basketmicroservice.core.domain.service.inferfaces;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.model.BasketItemKey;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.UUID;


public interface IBasketRepository extends CrudRepository<BasketItem, BasketItemKey> {

    List<BasketItem> getItemsByBasketId(UUID basketId);

}
