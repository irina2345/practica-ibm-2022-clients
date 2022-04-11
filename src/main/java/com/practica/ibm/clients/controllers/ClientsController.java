package com.practica.ibm.clients.controllers;

import com.practica.ibm.clients.models.ClientList;
import com.practica.ibm.clients.services.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ClientsController {

    @Autowired
    ClientsService clientsService;

    @PostMapping("addClients")
    public ResponseEntity addClients(@RequestBody ClientList clients, Principal principal) {
        System.out.println("Request for adding clients received");

        return clientsService.addClients(clients);
    }

}
