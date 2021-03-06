package com.nttdata.product.controller;

import com.nttdata.product.model.Product;
import com.nttdata.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/findById/{id}")
    public Mono<Product> findByIdController(@PathVariable("id") String id) {
        return productService.findByIdService(id);
    }

    @GetMapping("/findByIdClient/{id}")
    public Flux<Product> allProductsByClient(@PathVariable("id") String idClient) {
        return productService.allProductsByClient(idClient);
    }

    @PostMapping("/save")
    private Mono<Product> saveController(@RequestBody Product product) {
        return productService.saveProductService(product);
    }

    @PutMapping("/update")
    private Mono<Product> updateController(@RequestBody Product product) {
        return productService.updateProductService(product);
    }
}
