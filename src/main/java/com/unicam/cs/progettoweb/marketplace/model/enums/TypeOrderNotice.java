package com.unicam.cs.progettoweb.marketplace.model.enums;

public enum TypeOrderNotice {
    READY_TO_ELABORATING,       // notifica al seller che deve iniziare a processare
    SHIPPING_DETAILS_SET,        //notifica al customer che sono stati impostati i dettagli tra cui track_code e data consegna
    CONFIRMED_DELIVERED        // notifica al customer che l’ordine è stato consegnato
}
