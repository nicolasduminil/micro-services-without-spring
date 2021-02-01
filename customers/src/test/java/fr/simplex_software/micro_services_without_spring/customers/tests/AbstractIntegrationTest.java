package fr.simplex_software.micro_services_without_spring.customers.tests;

import lombok.extern.slf4j.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;

import java.io.*;
import java.util.function.*;

@Slf4j
public class AbstractIntegrationTest
{
  protected static final DockerComposeContainer dockerComposeContainer =
    new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
      .withExposedService("wildfly", 8080)
      .withExposedService("wildfly", 9990);

  static
  {
    dockerComposeContainer.start();
  }
}
