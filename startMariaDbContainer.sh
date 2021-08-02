#!/bin/bash
# Script to document / start the MariaDB docker container start
#  * official mariadb/server image is used
#  * no config / storage volumes linked to /var/lib/mysql / /etc/mysql/conf.d

MARIADB_IMAGE=mariadb/server:10.3
CONTAINER_NAME="mariadb_reactive"
PORT="3308"
HOST="127.0.0.1"
USERNAME="reactive"
PASSWORD="changeme"
DB_NAME="reactive"
ROOT_PASSWORD="SuperSecret1234"

function run_sql_query_user {
    local _QUERY=$1
    mysql --port=$PORT --host=$HOST --user=$USERNAME --password=$PASSWORD -e "$_QUERY"
}
function run_sql_query_root {
    local _QUERY=$1
    mysql --port=$PORT --host=$HOST --user=root --password=$ROOT_PASSWORD -e "$_QUERY"
}
function wait_for_db_service {
    local _MAX_COUNT=30
    echo -e "[i] Wait for database (timeout: ${_MAX_COUNT}s)"
    while [ "$_MAX_COUNT" -gt 0 ]
    do
        _DB_ACCESS_STATUS=$(run_sql_query_root "SHOW DATABASES;" 2>/dev/null)
        if [ ! -z "$_DB_ACCESS_STATUS" ] ; then
            return
        fi
        sleep 1;
        (( _MAX_COUNT-- ))
    done
    echo -e "[!] Database did not show up after ${_MAX_COUNT}s"
}

CONTAINER_FOUND=$(docker container list | grep $CONTAINER_NAME -c )
if [ "$CONTAINER_FOUND" -ne 0 ] ; then
    echo -e "[!] Container named'$CONTAINER_NAME' found, stop (if running) and remove or rename it"
    docker container list --all
    exit 1
fi
echo -e "[i] Pull docker image $MARIADB_IMAGE"
docker pull "$MARIADB_IMAGE"
echo -e "[i] Run container"
docker run --restart unless-stopped \
           --tty=false \
           --name $CONTAINER_NAME \
           --env MYSQL_ROOT_PASSWORD="$ROOT_PASSWORD" \
           --env MYSQL_USER="$USERNAME" \
           --env MYSQL_PASSWORD="$PASSWORD" \
           --publish "$PORT":3306 \
           --detach \
           "$MARIADB_IMAGE"
DOCKER_RUN=$?
if [ $DOCKER_RUN -ne 0 ]; then
    echo -e "[!] Failed to run docker container (error code: $DOCKER_RUN)"
    exit 1
fi
wait_for_db_service
# List all databases
DATABASES=$(run_sql_query_root "SHOW DATABASES;")
echo -e "[i] All databases:"
echo -e "$DATABASES" | grep -vi "database"
# Create database if not existing
DB_EXISTS=$(echo "$DATABASES" | grep -c "$DB_NAME")
if [ "$DB_EXISTS" -eq 0 ] ; then
    run_sql_query_root "CREATE DATABASE $DB_NAME;"
fi
# Update user privileges in any case
run_sql_query_root "GRANT ALL PRIVILEGES ON *.* TO '$USERNAME'@'%' IDENTIFIED BY '$PASSWORD'; FLUSH PRIVILEGES;"
# List user databases
DATABASES=$(run_sql_query_user "SHOW DATABASES;")
echo -e "[i] User databases:"
echo -e "$DATABASES" | grep -vi "database"
echo -e "[i] done"
