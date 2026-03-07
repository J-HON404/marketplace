#!/bin/sh

# Sostituisci le variabili di ambiente nel template
envsubst '${GATEWAY_URL} ${HOST_GATEWAY}' \
    < /etc/nginx/conf.d/default.conf.template \
    > /etc/nginx/conf.d/default.conf

# Avvia Nginx in primo piano
exec nginx -g 'daemon off;'
