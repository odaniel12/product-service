package com.nttdata.product.repository;

import com.nttdata.product.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    public Flux<Product> findByIdClient(String idClient);

    public Mono<Boolean> existsByNameProductAndTypeProductAndIdClient(String nameProduct, String typeProduct, String idClient);

    public Mono<Long> countByIdClientAndNameProductAndTypeProduct(String idClient, String nameProduct, String typeProduct);

}
