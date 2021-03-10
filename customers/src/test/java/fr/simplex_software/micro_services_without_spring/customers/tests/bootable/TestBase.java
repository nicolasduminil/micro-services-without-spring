package fr.simplex_software.micro_services_without_spring.customers.tests.bootable;

import fr.simplex_software.micro_services_without_spring.customers.tests.*;
import lombok.extern.slf4j.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.images.builder.*;
import org.testcontainers.junit.jupiter.Container;

import java.io.*;

@Slf4j
public class TestBase extends TestCommon
{
  @Container
  public static final GenericContainer<?> wildfly = new GenericContainer(
    new ImageFromDockerfile().withDockerfileFromBuilder(builder -> builder
      .from("adoptopenjdk/openjdk11")
      .run("apt-get install curl")
      .add("target/customers-bootable.jar", "/opt/customers-bootable.jar")
      .env("JAVA_OPTS", "-Djboss.bind.address=0.0.0.0 -Djboss.bind.address.management=0.0.0.0 -Djboss.http.port=8080 -Djboss.management.http.port=9990")
      .entryPoint("exec java -jar $JAVA_OPTS /opt/customers-bootable.jar")
      .build())
      .withFileFromFile("target/customers-bootable.jar", new File("target/customers-bootable.jar")))
    .withExposedPorts(8080, 9990)
    .withNetwork(Network.newNetwork())
    .withNetworkAliases("wildfly-container-alias")
    .withLogConsumer(new Slf4jLogConsumer(log));
}
