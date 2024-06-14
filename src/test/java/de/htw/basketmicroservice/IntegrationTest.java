package de.htw.basketmicroservice;

import de.htw.basketmicroservice.core.domain.model.BasketItem;
import de.htw.basketmicroservice.core.domain.service.inferfaces.IBasketRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class IntegrationTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12");

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
                .basketItemId(new UUID(1L, 1L))
                .basketId(BASKET_ID_1)
                .name("item1")
                .unitPrice(BigDecimal.valueOf(19.99))
                .imageUrl("http://dummy.url")
                .quantity(1)
                .build());

        basketRepository.save(BasketItem.builder()
                .basketItemId(new UUID(2L, 1L))
                .basketId(BASKET_ID_1)
                .name("item2")
                .unitPrice(BigDecimal.valueOf(29.99))
                .imageUrl("http://dummy.url2")
                .quantity(2)
                .build());

        basketRepository.save(BasketItem.builder()
                .basketItemId(new UUID(3L, 1L))
                .basketId(BASKET_ID_2)
                .name("item3")
                .unitPrice(BigDecimal.valueOf(39.99))
                .imageUrl("http://dummy.url3")
                .quantity(3)
                .build());

    }

    private static final UUID BASKET_ID_1 = new UUID(10L, 10L);
    private static final UUID BASKET_ID_2 = new UUID(11L, 11L);


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
    void shouldReturnAllBasketItemsFromBasket() {

        given()
                .contentType(ContentType.JSON)
                .get("/v1/baskets/" + BASKET_ID_1)
                .then()
                .statusCode(200);
                //figure out how to check body content

    }


}
