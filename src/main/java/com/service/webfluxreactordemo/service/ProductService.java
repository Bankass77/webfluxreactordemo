package com.service.webfluxreactordemo.service;

import com.service.webfluxreactordemo.domain.Product;
import com.service.webfluxreactordemo.exception.NotFoundException;
import com.service.webfluxreactordemo.exception.ServiceExecption;
import com.service.webfluxreactordemo.repository.ProductRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    public Mono<Product> findById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found with id " + id)))
                .onErrorMap(DataAccessException.class, ex -> new ServiceExecption(ex.getMessage()));
    }

    public Mono<Product> save(Product product) {
        return productRepository.save(product).onErrorMap(
                DataAccessException.class,
                ex -> new ServiceExecption("Unable to save product : " + ex)
        );
    }

    public Flux<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public Flux<Product> findByPrice(BigDecimal price) {
        return productRepository.findByPrice(price);
    }

    public Flux<Product> findByPriceGreaterThan(BigDecimal price) {
        return productRepository.findByPriceGreaterThan(price);
    }

    public Flux<Product> findByQuantity(Integer quantity) {
        return productRepository.findByQuantity(quantity);
    }

    public Flux<Product> findByTreshold(BigDecimal treshold) {
        return productRepository.findByThreshold(treshold)
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found with threshold " + treshold)));
    }

    public Mono<Void> deleteById(Long id) {
        return productRepository.findById(id).switchIfEmpty(Mono.error(
                new NotFoundException("Product not found with id " + id)
        )).flatMap(productRepository::delete);
    }

    public Mono<Product> updateById(Long id, Product product) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Product not found with id " + id)))
                .flatMap(p -> {
                    p.setName(product.getName());
                    p.setPrice(product.getPrice());
                    p.setQuantity(product.getQuantity());
                    return productRepository.save(p);
                });
    }
}
