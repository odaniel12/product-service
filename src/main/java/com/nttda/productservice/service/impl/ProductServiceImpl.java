package com.nttda.productservice.service.impl;

import com.nttda.productservice.model.Client;
import com.nttda.productservice.model.Product;
import com.nttda.productservice.repository.ProductRepository;
import com.nttda.productservice.service.ProductService;
import com.nttda.productservice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Flux<Product> findAllService() {
	return productRepository.findAll();
    }

    @Override public Mono<Product> findByIdService(String id) {
	return productRepository.findById(id);
    }

    @Override
    public Mono<Product> saveProductService(Product product) {

	return findClientById(product.getIdClient())
			.flatMap(client -> {
			    return validateProduct(product, client).flatMap(valProd -> {
					if (product.getNameProduct().equals(Constant.NAME_PRODUCT_CREDIT) && product.getAmount().equals(null)) {
						product.setAmount(2000.0);
						product.setLimitCredit(2000.0);
					}
			        if (valProd) {
				    return validateCreateProduct(product, client).flatMap(createProduct -> {
					if (createProduct) {
					    return productRepository.save(product);
					}else {
					    return Mono.error(new Exception("No se pudo guardar el producto"));
					}
				    });
				}else {
				    return Mono.error(new Exception("No se pudo guardar el producto"));
				}

			    });
			}).switchIfEmpty(Mono.error(new Exception("El cliente no existe")));

    }

    @Override public Flux<Product> allProductsByClient(String idClient) {
	return productRepository.findByIdClient(idClient);
    }

    @Override public Mono<Product> updateProductService(Product product) {
	return productRepository.findById(product.getId()).flatMap(prod -> {
	    prod.setAmount(product.getAmount());
	    return productRepository.save(prod);
	}).switchIfEmpty(Mono.error(new Exception("Cliente no existe")));
    }

    public Mono<Boolean> validateCreateProduct(Product product, Client client) {

        if (client.getTypeClient().equals(Constant.TYPE_CLIENT_PERSONAL)) {
            if (product.getNameProduct().equals(Constant.NAME_PRODUCT_ACCOUNT)) {
                if (product.getTypeProduct().equals(Constant.TYPE_PRODUCT_VIP)) {
                    return productRepository.countByIdClientAndNameProductAndTypeProduct(client.getIdClient(), Constant.NAME_PRODUCT_CREDIT, Constant.TYPE_PRODUCT_CREDIT_CARD)
				    .flatMap(cant -> {
					System.out.println("cant: "+cant);
				       if (cant>0) {
				           return Mono.just(true);
				       }else {
					   return Mono.error(new Exception("No puede obtener la cuenta de ahorros VIP, tiene que tener un tarjeta de credito"));
				       }
				    });
		}
		return productRepository.existsByNameProductAndTypeProductAndIdClient(product.getNameProduct(), product.getTypeProduct(), product.getIdClient())
				.flatMap(value -> {
				    if (value) {
					return Mono.error(new Exception("El cliente PERSONAL ya tiene una "+product.getNameProduct()+ " de tipo " + product.getTypeProduct()));
				    }else {
					return Mono.just(true);
				    }
				});
	    }else if (product.getNameProduct().equals(Constant.NAME_PRODUCT_CREDIT) && product.getTypeProduct().equals(Constant.TYPE_PRODUCT_PERSONAL)) {
		return productRepository.existsByNameProductAndTypeProductAndIdClient(product.getNameProduct(), product.getTypeProduct(), product.getIdClient())
				.flatMap(value -> {
				   if (value) {
				       return Mono.error(new Exception("El cliente PERSONAL no puede tener mas de 1 credito Personal"));
				   }else {
				       return Mono.just(true);
				   }
				});
	    }
	}

        if (client.getTypeClient().equals(Constant.TYPE_CLIENT_BUSINESS)) {
	    if (product.getNameProduct().equals(Constant.NAME_PRODUCT_ACCOUNT)) {
	        if (product.getTypeProduct().equals(Constant.TYPE_PRODUCT_PYME)) {
		    return productRepository.countByIdClientAndNameProductAndTypeProduct(client.getIdClient(), Constant.NAME_PRODUCT_CREDIT, Constant.TYPE_PRODUCT_CREDIT_CARD)
				    .flatMap(cant -> {
					if (cant>0) {
					    return Mono.just(true);
					}else {
					    return Mono.error(new Exception("No puede obtener la cuenta de corriente PYME, tiene que tener un tarjeta de credito"));
					}
				    });
		}
	    }
	}
        return Mono.just(true);
    }

    public Mono<Boolean> validateProduct(Product product, Client client) {

        Boolean validate = false;

	if (product.getNameProduct().equals(Constant.NAME_PRODUCT_ACCOUNT)) {
	    if (client.getTypeClient().equals(Constant.TYPE_CLIENT_PERSONAL)) {
		switch (product.getTypeProduct()) {
		case Constant.TYPE_PRODUCT_SAVING:
		case Constant.TYPE_PRODUCT_CURRENT:
		case Constant.TYPE_PRODUCT_FIXED_TERM:
		case Constant.TYPE_PRODUCT_VIP:
		    validate = true;
		    break;
		}
	    }else if (client.getTypeClient().equals(Constant.TYPE_CLIENT_BUSINESS)) {
		switch (product.getTypeProduct()) {
		case Constant.TYPE_PRODUCT_CURRENT:
		case Constant.TYPE_PRODUCT_PYME:
		    validate = true;
		    break;
		}
	    }
	    if (validate.equals(false)) {
	        return Mono.error(new Exception("El cliente de es de tipo " + client.getTypeClient() + " y no puede guardar un(a) " + product.getNameProduct() + " de tipo " + product.getTypeProduct()));
	    }
	}else if (product.getNameProduct().equals(Constant.NAME_PRODUCT_CREDIT)) {
	    if (client.getTypeClient().equals(Constant.TYPE_CLIENT_PERSONAL)) {
		switch (product.getTypeProduct()) {
		case Constant.TYPE_PRODUCT_PERSONAL:
		case Constant.TYPE_PRODUCT_CREDIT_CARD:
		    validate = true;
		    break;
		}
	    }else if (client.getTypeClient().equals(Constant.TYPE_CLIENT_BUSINESS)) {
		switch (product.getTypeProduct()) {
		case Constant.TYPE_PRODUCT_BUSINESS:
		case Constant.TYPE_PRODUCT_CREDIT_CARD:
		    validate = true;
		    break;
		}
	    }
	    if (validate.equals(false)) {
		return Mono.error(new Exception("El cliente de es de tipo " + client.getTypeClient() + " y no puede guardar un(a) " + product.getNameProduct() + " de tipo " + product.getTypeProduct()));
	    }
	}else {
	    return Mono.error(new Exception("Nombre de producto no encontrado"));
	}

	return Mono.just(validate);
    }

    public Mono<Client> findClientById(String id) {

	String url = "http://localhost:8080/clientBank/findClient/" + id;

	return WebClient.create()
			.get()
			.uri(url)
			.retrieve()
			.bodyToMono(Client.class);
    }
}
