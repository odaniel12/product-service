package com.nttda.productservice.repository;

import com.nttda.productservice.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    public Mono<Boolean> existsByNameProductAndTypeProductAndIdClient(String nameProduct, String typeProduct, String idClient);

    public Mono<Long> countByIdClientAndNameProductAndTypeProduct(String idClient, String nameProduct, String typeProduct);

}
