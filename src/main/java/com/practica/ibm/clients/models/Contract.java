package com.practica.ibm.clients.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

public class Contract {

    private @Getter @Setter int contractNumber;

    private @Getter @Setter double contractAmount;

    private @Getter @Setter String effectiveFrom;

    private @Getter @Setter int periodInDays;

}
