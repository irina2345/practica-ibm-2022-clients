package com.practica.ibm.clients.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("clients")
public class Client {

    private @Getter @Setter String adminEmail;

    private @Getter @Setter String name;

    private @Getter @Setter List<Contract> contracts;


}
