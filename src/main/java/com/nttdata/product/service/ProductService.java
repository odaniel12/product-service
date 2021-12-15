package com.nttdata.product.service;

import com.nttdata.product.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    public Flux<Product> findAllService();

    public Mono<Product> findByIdService(String id);

    public Mono<Product> saveProductService(Product product);

    public Flux<Product> allProductsByClient(String idClient);

    public Mono<Product> updateProductService(Product product);

}
