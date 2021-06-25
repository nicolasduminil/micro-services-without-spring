package fr.simplex_software.micro_services_without_spring.customers.tests.standalone;

import io.restassured.*;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.*;

import javax.ws.rs.core.*;

import static org.hamcrest.Matchers.*;

@Testcontainers
public class CustomerHealthCheckIT extends TestBase
{
  @Test
  public void checkHealth()
  {
    RestAssured.given()
      .accept(MediaType.APPLICATION_JSON)
      .when()
      .get(UriBuilder.fromPath("health")
        .scheme("http")
        .host(wildfly.getHost())
        .port(wildfly.getMappedPort(9990))
        .build())
      .then()
      .assertThat().body("checks.name", anyOf(hasItem(">>> CustomerResourceHealthCheck.call(): Readiness health check")));
  }
}
