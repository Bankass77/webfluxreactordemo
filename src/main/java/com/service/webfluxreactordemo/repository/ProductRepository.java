package com.service.webfluxreactordemo.repository;

import com.service.webfluxreactordemo.domain.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;


// Extends ReactiveCrudRepository instead of JpaRepository
//All methods return mono or flux
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    // Derived query methods work like spring data jpa
    Flux<Product> findByName(String name);

    Flux<Product> findByPrice(BigDecimal price);

    Flux<Product> findByQuantity(Integer quantity);

    Flux<Product> findByPriceGreaterThan(BigDecimal price);

    //custom Query with @Query annotation
    //use native SQL-R2DBC doesn't support JPQL
    @Query("SELECT * FROM products WHERE quantity <:threshold")
    Flux<Product> findByThreshold(@Param("threshold") BigDecimal threshold);
}
