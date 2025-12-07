package com.unicam.cs.progettoweb.marketplace.model.enums;

public enum TypeOrderNotice {
    READY_TO_ELABORATING,   // notifica dal customer verso seller per un ordine creato
    SHIPPING_DETAILS_SET,   //notifica al customer che sono stati impostati i dettagli (track_code e data consegna)
    CONFIRMED_DELIVERED ,    // notifica dal customer che l’ordine è stato consegnato
    ORDER_DELETED            //notifica al customer che l'ordine è stato cancellato
}
