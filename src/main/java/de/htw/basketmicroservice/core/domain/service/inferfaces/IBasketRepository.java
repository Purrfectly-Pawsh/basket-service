package de.htw.basketmicroservice.core.domain.service.inferfaces;

import de.htw.basketmicroservice.core.domain.model.Basket;
import org.springframework.data.repository.CrudRepository;

public interface IBasketRepository extends CrudRepository<Basket, Long> {
}
