#!/bin/sh

# Sostituisci le variabili di ambiente nel template
envsubst '${BACKEND_URL} ${HOST_BACKEND}' \
    < /etc/nginx/conf.d/default.conf.template \
    > /etc/nginx/conf.d/default.conf

# Avvia Nginx in primo piano
exec nginx -g 'daemon off;'
