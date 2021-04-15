FROM mysql:5.6

MAINTAINER hyounjunkim <4whomtbts@gmail.com>

COPY my.cnf /etc/mysql/conf.d/
COPY init.sql /docker-entrypoint-initdb.d/

EXPOSE 3306

#VOLUME /var/lib/mysql
#VOLUME /etc/mysql/conf.d
