package de.htw.basketmicroservice.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemKey implements Serializable {

    private UUID basketId;
    private UUID basketItemId;

}
