#!/bin/bash

TABLE_EXISTS=$(sudo -u postgres psql -d myalbum -tAc "SELECT to_regclass('public.album')")

if [ "$TABLE_EXISTS" == "" ]; then
    echo "테이블이 없어 스키마를 생성합니다."

    JAR_PATH=$(find /var/app/current -maxdepth 1 -name "*.jar" | head -n 1)
    TEMP_SQL="/tmp/DDL.sql"

    unzip -p "$JAR_PATH" "BOOT-INF/classes/sql/DDL.sql" > "$TEMP_SQL"
    sudo -u postgres psql -d myalbum -f "$TEMP_SQL"

    rm -f "$TEMP_SQL"
else
    echo "테이블이 이미 존재하여 건너뜁니다."
fi
