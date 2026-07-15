#!/bin/bash

PG_HBA=/var/lib/pgsql/data/pg_hba.conf

if [ -f "$PG_HBA" ]; then
    # postgres 계정 줄이 아직 peer/ident가 아니고, md5로 되어 있다면
    # postgres 전용 peer 줄을 추가하고, 전체는 md5로 통일
    if ! grep -q "^local\s\+all\s\+postgres\s\+peer" "$PG_HBA"; then
        sudo sed -i '/^local\s\+all\s\+all\s\+md5/i local   all             postgres                                peer' "$PG_HBA"
    fi
fi
