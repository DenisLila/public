#!/bin/bash

# Key and self-signed cert generated with:
# openssl req -new -x509 -days 365 -sha256 -newkey rsa:2048 -nodes -keyout server.key -out server.crt -subj '/O=Company/OU=Department/CN=50.131.170.68'

#TODO(dlila): argument for selecting between external and localhost setup.
python manage.py runmodwsgi \
    --setup-only \
    --server-name 50.131.170.68 \
    --https-only \
    --https-port 9000 \
    --ssl-certificate-file /home/dlila/crypto_for_web_server/realip/server.crt \
    --ssl-certificate-key-file /home/dlila/crypto_for_web_server/realip/server.key

