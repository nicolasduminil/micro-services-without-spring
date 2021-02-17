FROM jboss/wildfly:20.0.1.Final
RUN $JBOSS_HOME/bin/add-user.sh admin admin --silent
COPY ./customization/com $JBOSS_HOME/modules/com
COPY ./customization/standalone-microprofile.xml $JBOSS_HOME/standalone/configuration/standalone-microprofile.xml
COPY ./target/customers.war $JBOSS_HOME/standalone/deployments
#HEALTHCHECK --start-period=10s --timeout=60s --retries=10 --interval=5s CMD curl -f http://localhost:9990/health/ready || exit 1
#ENV JAVA_OPTS="-Djboss.http.port=8080 -Djboss.management.http.port=9990 -Dapp.context.root=customers"
#EXPOSE 9990
#CMD ["/opt/jboss/wildfly/customization/execute.sh"]
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]