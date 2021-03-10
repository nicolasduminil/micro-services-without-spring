echo "************************************************************"
echo "Waiting for the Oracle database server to start on port $ORACLE_PORT"
echo "************************************************************"
while ! $(nc -z oracle $ORACLE_PORT ); do sleep 3; done
sleep 20
echo ">>>>>>>>>>>> The Oracle database server has started on port $ORACLE_PORT"
echo "********************************************************"
echo "Starting the Wildfly application server on port $WILDFLY_PORT"
echo "********************************************************"
/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 -c standalone-microprofile.xml