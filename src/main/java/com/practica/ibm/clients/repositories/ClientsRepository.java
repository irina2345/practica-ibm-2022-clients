package com.practica.ibm.clients.repositories;

import com.practica.ibm.clients.models.Account;
import com.practica.ibm.clients.models.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientsRepository extends MongoRepository<Client, String> {


}
