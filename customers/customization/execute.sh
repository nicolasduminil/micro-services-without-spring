#!/bin/bash
echo "=> Executing Customization script"
JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh

function wait_for_server()
{
  until $($JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running)
  do
    sleep 5
  done
}

echo "=> Starting WildFly server"
echo "JBOSS_HOME  : " $JBOSS_HOME
echo "JBOSS_CLI   : " $JBOSS_CLI
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &
echo "=> Waiting for the server to boot"
wait_for_server
echo "=> The server has booted. Adding Oracle module & driver"
$JBOSS_CLI -c << EOF
batch
connect
module add --name=com.oracle --resources=$JBOSS_HOME/customization/ojdbc6.jar --dependencies=javax.api,javax.transaction.api
/subsystem=datasources/jdbc-driver=oracle:add(driver-name="oracle",driver-module-name="com.oracle",driver-class-name=oracle.jdbc.driver.OracleDriver)
data-source add --name=CustomersDS --driver-name=oracle --jndi-name=java:jboss/datasources/CustomersDS --connection-url=jdbc:oracle:thin:@192.168.96.3:1521/xe --user-name=nicolas --password=password1 --use-ccm=false --max-pool-size=25 --blocking-timeout-wait-millis=5000 --enabled=true
run-batch
EOF
echo "=> Shutting down WildFly"
$JBOSS_CLI -c ":shutdown"
echo "=> Restarting WildFly"
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
