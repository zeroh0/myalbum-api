#!/bin/bash

DB_EXISTS=$(sudo -u postgres psql -tAc "SELECT 1 FROM pg_database WHERE datname='myalbum'")

if [ "$DB_EXISTS" != "1" ]; then
    sudo -u postgres psql -c "CREATE DATABASE myalbum;"
    sudo -u postgres psql -c "CREATE USER zeroh0 WITH PASSWORD '${DB_PASSWORD}';"
    sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE myalbum TO zeroh0;"
fi
