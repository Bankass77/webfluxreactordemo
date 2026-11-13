package com.service.webfluxreactordemo.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

// R2BC enties use @Table instead of @Entity
// keep it simple-R2DBC has fewer features than JPA
@Table("products")
@Data
public class Product {

    @Id
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer quantity;

}
