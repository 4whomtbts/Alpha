FROM mysql:5.6

MAINTAINER hyounjunkim <4whomtbts@gmail.com>

ENV MYSQL_DATABASE rna_db
ENV MYSQL_ROOT_PASSWORD 1234
ENV MYSQL_USER rna_dba
ENV MYSQL_PASSWORD 1234
ADD genesis_schema.sql /docker-entrypoint-initdb.d

#EXPOSE 33065

#VOLUME /var/lib/mysql
#VOLUME /etc/mysql/conf.d