package com.nttda.productservice.model;

import lombok.Data;

@Data
public class Client {
    private String idClient;
    private String typeClient;
    private String name;
    private String lastName;
    private String dni;
    private String address;
    private String ruc;
}
