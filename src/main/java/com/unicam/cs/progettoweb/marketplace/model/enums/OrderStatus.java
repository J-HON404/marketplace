package com.unicam.cs.progettoweb.marketplace.model.enums;

public enum TypeOrderNotice {
    READY_TO_ELABORATING,    // notifica dal customer verso seller per un ordine creato
    SHIPPING_DETAILS_SET,    //notifica al customer che sono stati impostati i dettagli (track_code e data consegna)
    CONFIRMED_DELIVERED ,    // notifica dal customer che l’ordine è stato consegnato
    REMIND_DELIVERY,         //notifica al customer se non ha ancora confermato la consegna, quindi è passata la data di consegna
    ORDER_DELETED            //notifica al customer che l'ordine è stato cancellato
}
