package fr.simplex_software.micro_services_without_spring.customers.tests.standalone;

import io.restassured.*;
import org.hamcrest.*;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.*;

import javax.ws.rs.core.*;

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
        //.path("ready")
        .build())
      .then()
      .contentType(MediaType.APPLICATION_JSON)
      .statusCode(Response.Status.OK.getStatusCode())
      .body("status", Matchers.equalTo("UP"))
      .body("checks.size()", Matchers.equalTo(4))
      .body("checks.name[3]", Matchers.contains("Readiness health check"));
  }
}
