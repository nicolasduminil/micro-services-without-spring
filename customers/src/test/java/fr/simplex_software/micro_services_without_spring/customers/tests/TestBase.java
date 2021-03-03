package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;
import org.testcontainers.images.builder.*;
import org.testcontainers.junit.jupiter.Container;

import javax.xml.bind.*;
import java.io.*;

@Slf4j
public class TestBase extends TestCommon
{
  @Container
  public static final GenericContainer<?> wildfly = new GenericContainer(
    new ImageFromDockerfile().withDockerfileFromBuilder(builder -> builder
      .from("jboss/wildfly:21.0.0.Final")
      .run("/opt/jboss/wildfly/bin/add-user.sh admin admin --silent")
      .add("target/customers.war", "/opt/jboss/wildfly/standalone/deployments")
      .entryPoint("exec /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0")
      .build())
      .withFileFromFile("target/customers.war", new File("target/customers.war")))
    .withExposedPorts(8080, 9990)
    .withNetwork(Network.newNetwork())
    .withNetworkAliases("wildfly-container-alias")
    .withLogConsumer(new Slf4jLogConsumer(log))
    .waitingFor(Wait.forLogMessage(".*WFLYSRV0051.*", 1));
}
