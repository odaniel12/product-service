package com.nttda.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "product")
public class Product{

    @Id
    private String id;
    private String typeProduct;
    private String nameProduct;
    private String idClient;
    private Double amount;
    private Integer transactionAmount;
    private Double limitCredit;

}
