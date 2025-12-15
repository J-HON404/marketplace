package com.unicam.cs.progettoweb.marketplace.model.enums;

public enum OrderStatus {
    READY_TO_ELABORATING,    // se il customer ha creato un ordine
    SHIPPING_DETAILS_SET,    //se il seller ha impostato i dettagli ordine(track_code e data consegna) e ha spedito l'ordine
    REMIND_DELIVERY,         //se il customer se non ha ancora confermato la consegna, quindi è passata la data di consegna
    CONFIRMED_DELIVERED     // se il customer ha confermato la consegna
}
