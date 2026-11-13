package com.service.webfluxreactordemo.controller;


import com.service.webfluxreactordemo.domain.Product;
import com.service.webfluxreactordemo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable Long id) {
        return productService.findById(id).map(ResponseEntity::ok);

    }

    public Flux<ResponseEntity<Product>> getAllProducts() {
        return productService.findAll().map(ResponseEntity::ok);
    }


    @PostMapping
    public Mono<ResponseEntity<Product>> saveProduct(@RequestBody @Validated Product product) {
        return productService.save(product).map( saveProduct->
                ResponseEntity.created(URI.create("/api/v1/products/"+saveProduct.getId())).body(saveProduct));
    }


    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateById(id, product).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
        return productService.deleteById(id).thenReturn(ResponseEntity.noContent().build());
    }

    @GetMapping("/by-name/{name}")
    public Flux<Product> getProductByName(
            @PathVariable String name) {
        return productService.findByName(name);
    }

    @GetMapping("/by-quantity/{quantity}")
    public Flux<Product> findByQuantity(
            @PathVariable Integer quantity) {
        return productService.findByQuantity(quantity);
    }

    @GetMapping("/by-threshold/{threshold}")
    public Flux<Product> findByThreshold(
            @PathVariable BigDecimal threshold) {
        return productService.findByTreshold(threshold);
    }

    @GetMapping("/search")
    public Flux<ResponseEntity<Product>> search(@RequestParam BigDecimal price, @RequestParam(defaultValue = "false") boolean greaterThan) {
        if (greaterThan) {
            return productService.findByPriceGreaterThan(price).map(ResponseEntity::ok);
        }
        return productService.findByPrice(price).map(ResponseEntity::ok);
    }
}
