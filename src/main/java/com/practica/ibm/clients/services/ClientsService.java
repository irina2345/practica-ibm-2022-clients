package com.practica.ibm.clients.services;

import com.practica.ibm.clients.models.Client;
import com.practica.ibm.clients.models.ClientList;
import com.practica.ibm.clients.repositories.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClientsService {

    @Autowired
    ClientsRepository clientsRepository;

    public ResponseEntity addClients(ClientList clients) {
        if (clients == null) {
            return ResponseEntity.badRequest().build();
        }

        for (Client client : clients.getClients()) {
            clientsRepository.save(client);
        }
        return ResponseEntity.ok().build();
    }

}
