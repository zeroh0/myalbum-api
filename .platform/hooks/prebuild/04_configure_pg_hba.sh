#!/bin/bash

PG_HBA=/var/lib/pgsql/data/pg_hba.conf

if [ -f "$PG_HBA" ]; then
    sudo sed -i 's/ident$/md5/g; s/peer$/md5/g' "$PG_HBA"
fi
