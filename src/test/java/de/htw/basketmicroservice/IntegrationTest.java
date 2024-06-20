package de.htw.basketmicroservice;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.model.BasketItemKey;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.*;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.offset;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class IntegrationTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private IBasketRepository basketRepository;

    @BeforeAll
    static void startPostgresContainer(){
        postgreSQLContainer.start();
    }

    @AfterAll
    static void stopPostgresContainer(){
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        fillDatabase();
    }

    private void fillDatabase() {
        basketRepository.save(BasketItem.builder()
                .basketItemId(BASKET_ITEM_ID_1)
                .basketId(BASKET_ID_ALICE)
                .name("item1")
                .unitPrice(BigDecimal.valueOf(10.00))
                .imageUrl("http://dummy.url")
                .quantity(1)
                .build());

        basketRepository.save(BasketItem.builder()
                .basketItemId(BASKET_ITEM_ID_2)
                .basketId(BASKET_ID_ALICE)
                .name("item2")
                .unitPrice(BigDecimal.valueOf(20.00))
                .imageUrl("http://dummy.url2")
                .quantity(2)
                .build());

        basketRepository.save(BasketItem.builder()
                .basketItemId(BASKET_ITEM_ID_3)
                .basketId(BASKET_ID_ALICE)
                .name("item3")
                .unitPrice(BigDecimal.valueOf(30.00))
                .imageUrl("http://dummy.url3")
                .quantity(3)
                .build());

        basketRepository.save(BasketItem.builder()
                .basketItemId(BASKET_ITEM_ID_3)
                .basketId(BASKET_ID_BOB)
                .name("item3")
                .unitPrice(BigDecimal.valueOf(30.00))
                .imageUrl("http://dummy.url3")
                .quantity(3)
                .build());

    }

    private static final UUID BASKET_ID_ALICE = new UUID(10L, 10L);
    private static final UUID BASKET_ID_ALICE_LOGIN = new UUID(20L, 20L);
    private static final UUID BASKET_ID_BOB = new UUID(11L, 11L);
    private static final UUID BASKET_ITEM_ID_1 = new UUID(1L, 1L);
    private static final UUID BASKET_ITEM_ID_2 = new UUID(2L, 1L);
    private static final UUID BASKET_ITEM_ID_3 = new UUID(3L, 1L);



    @AfterEach
    void clean() {
        basketRepository.deleteAll();
    }

    @Test
    void connectionEstablished() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @Test
    void shouldReturnAllBasketItemsFromCorrectBasket() {
        given()
                .contentType(ContentType.JSON)
                .get("/v1/baskets/" + BASKET_ID_ALICE)
                .then()
                .statusCode(both(greaterThanOrEqualTo(200)).and(lessThan(300)))
                .body("basketItems.basketItemId",
                        hasItems(
                                BASKET_ITEM_ID_1.toString(),
                                BASKET_ITEM_ID_2.toString()
                        )
                );
    }

    @Test
    void shouldReturnEmptyBasketWhenLastItemIsDeleted() {
        given()
                .contentType(ContentType.JSON)
                .delete("v1/baskets/" + BASKET_ID_BOB + "/items/" + BASKET_ITEM_ID_3)
                .then()
                .statusCode(both(greaterThanOrEqualTo(200)).and(lessThan(300)))
                .body("basketId", equalTo(BASKET_ID_BOB.toString()))
                .and()
                .body("totalPrice", equalTo(0))
                .and()
                .body("basketItems", hasSize(0));
    }

    @Test
    void shouldRemoveCorrectItemFromDatabaseWhenItIsDeleted() {
        given()
                .contentType(ContentType.JSON)
                .delete("v1/baskets/" + BASKET_ID_BOB + "/items/" + BASKET_ITEM_ID_3);

        assertThat(basketRepository.findById(new BasketItemKey(BASKET_ID_ALICE, BASKET_ITEM_ID_1))).isNotEmpty();
        assertThat(basketRepository.findById(new BasketItemKey(BASKET_ID_ALICE, BASKET_ITEM_ID_2))).isNotEmpty();
        assertThat(basketRepository.findById(new BasketItemKey(BASKET_ID_ALICE, BASKET_ITEM_ID_3))).isNotEmpty();
        assertThat(basketRepository.findById(new BasketItemKey(BASKET_ID_BOB, BASKET_ITEM_ID_3))).isEmpty();
    }

    @Test
    void shouldReturnCorrectlyUpdatedBasketWhenItemQuantityIsChanged() {
        given()
                .contentType(ContentType.JSON)
                .body(BasketItem.builder()
                        .basketItemId(BASKET_ITEM_ID_3)
                        .basketId(BASKET_ID_BOB)
                        .name("item3")
                        .unitPrice(BigDecimal.valueOf(30.00))
                        .imageUrl("http://dummy.url3")
                        .quantity(5)
                        .build())
                .put("v1/baskets/" + BASKET_ID_BOB + "/items/" + BASKET_ITEM_ID_3)
                .then()
                .statusCode(both(greaterThanOrEqualTo(200)).and(lessThan(300)))
                .body("basketId", equalTo(BASKET_ID_BOB.toString()))
                .and()
                .body("totalPrice", equalTo(150F))
                .and()
                .body("basketItems", hasSize(1))
                .and()
                .body("basketItems[0].quantity", equalTo(5));
    }

    @Test
    void shouldOnlyContainTheItemsFromGuestBasketInUserBasketAfterTransferOnLogin() {
        given()
                .contentType(ContentType.JSON)
                .body(BASKET_ID_ALICE_LOGIN)
                .put("v1/baskets/" + BASKET_ID_ALICE)
                .then()
                .statusCode(both(greaterThanOrEqualTo(200)).and(lessThan(300)));

        List<BasketItem> aliceGuestBasket = basketRepository.getItemsByBasketId(BASKET_ID_ALICE);
        List<BasketItem> aliceLoginBasket = basketRepository.getItemsByBasketId(BASKET_ID_ALICE_LOGIN);

        assertThat(aliceLoginBasket.size()).isEqualTo(3);
        assertThat(aliceGuestBasket.isEmpty()).isTrue();
    }



}
