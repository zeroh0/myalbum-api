#!/bin/bash

DATA_DIR=/var/lib/pgsql/data

# 이미 초기화된 경우 건너뛰기
if [ ! -f "$DATA_DIR/PG_VERSION" ]; then
    sudo chown -R postgres:postgres $DATA_DIR
    sudo postgresql-setup --initdb
fi
