FROM quay.io/wildfly/wildfly:33.0.0.Final-jdk17

ENV DB_HOST=postgres \
    DB_PORT=5432 \
    DB_NAME=person_management \
    DB_USER=person_app \
    DB_PASSWORD=change_me_in_production

USER root

# Télécharger le driver PostgreSQL
ADD https://jdbc.postgresql.org/download/postgresql-42.7.3.jar /opt/jboss/wildfly/modules/org/postgresql/main/postgresql-42.7.3.jar

# Créer le module PostgreSQL
RUN mkdir -p $JBOSS_HOME/modules/org/postgresql/main && \
    echo '<?xml version="1.0" encoding="UTF-8"?>' > $JBOSS_HOME/modules/org/postgresql/main/module.xml && \
    echo '<module xmlns="urn:jboss:module:1.1" name="org.postgresql">' >> $JBOSS_HOME/modules/org/postgresql/main/module.xml && \
    echo '    <resources>' >> $JBOSS_HOME/modules/org/postgresql/main/module.xml && \
    echo '        <resource-root path="postgresql-42.7.3.jar"/>' >> $JBOSS_HOME/modules/org/postgresql/main/module.xml && \
    echo '    </resources>' >> $JBOSS_HOME/modules/org/postgresql/main/module.xml && \
    echo '    <dependencies>' >> $JBOSS_HOME/modules/org/postgresql/main/module.xml && \
    echo '        <module name="javax.api"/>' >> $JBOSS_HOME/modules/org/postgresql/main/module.xml && \
    echo '        <module name="javax.transaction.api"/>' >> $JBOSS_HOME/modules/org/postgresql/main/module.xml && \
    echo '        <module name="java.sql"/>' >> $JBOSS_HOME/modules/org/postgresql/main/module.xml && \
    echo '    </dependencies>' >> $JBOSS_HOME/modules/org/postgresql/main/module.xml && \
    echo '</module>' >> $JBOSS_HOME/modules/org/postgresql/main/module.xml

# Copier les scripts
COPY configure-wildfly.cli /tmp/configure-wildfly.cli
COPY start.sh /tmp/start.sh

RUN chmod +x /tmp/start.sh && \
    chown -R jboss:jboss /opt/jboss/wildfly/modules/org/postgresql /tmp/configure-wildfly.cli /tmp/start.sh

USER jboss

EXPOSE 8080 9990

CMD ["/tmp/start.sh"]