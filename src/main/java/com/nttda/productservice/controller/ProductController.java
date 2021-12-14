package com.nttda.productservice.controller;

import com.nttda.productservice.model.Product;
import com.nttda.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/findAll")
    public Flux<Product> findAllController() {
	return productService.findAllService();
    }

    @PostMapping("/save")
    private Mono<Product> saveController(@RequestBody Product product) {

        return productService.saveProductService(product);
    }
}
