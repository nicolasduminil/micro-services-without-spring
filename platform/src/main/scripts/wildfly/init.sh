#!/bin/bash
WILDFLY_HOME=/opt/jboss/wildfly
#JBOSS_CLI=$WILDFLY_HOME/bin/jboss-cli.sh
#echo $(date -u) "=> Deploy application"
#$JBOSS_CLI -c "deploy wildfly/customization/target/customers.war"
echo $(date -u) "=> Create user"
$WILDFLY_HOME/bin/add-user.sh admin Admin#70365 --silent