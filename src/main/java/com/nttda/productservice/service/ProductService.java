package com.nttda.productservice.service;

import com.nttda.productservice.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    public Flux<Product> findAllService();

    public Mono<Product> findByIdService(String id);

    public Mono<Product> saveProductService(Product product);

    public Flux<Product> allProductsByClient(String idClient);

    public Mono<Product> updateProductService(Product product);

}
