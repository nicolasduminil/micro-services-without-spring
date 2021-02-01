package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.*;
import io.restassured.*;
import io.restassured.builder.*;
import io.restassured.specification.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;

import javax.mail.internet.*;
import javax.ws.rs.core.*;
import java.io.*;

@Slf4j
public class CustomersTI //extends AbstractIntegrationTest
{
  private static RequestSpecification requestSpecification;
  private static CustomerAddress customerAddress = CustomerAddress.builder().number(26).street("allée des Sapins")
    .city("Soisy sous Montmorency").zip("95230").country("France").build();
  private static CustomerContactDetails customerContactDetails;
  public static DockerComposeContainer dockerComposeContainer = new DockerComposeContainer(new File("src/test/resources/docker-compose.yaml"))
    .withLocalCompose(true)
    .withExposedService("wildfly", 8080)
    .withLogConsumer("wildfly", new Slf4jLogConsumer(log));

  static
  {
    try
    {
      customerContactDetails = CustomerContactDetails.builder().firstName("Nicolas").lastName("DUMINIL")
        .emailAddress(new InternetAddress("nicolas.duminil@wanadoo.fr")).address(customerAddress).build();
    }
    catch (AddressException e)
    {
      e.printStackTrace();
    }
    dockerComposeContainer.start();
  }

  private static Customer customer = Customer.builder().customerRef("Customer1").customerType(CustomerType.LOYAL)
    .contactDetails(customerContactDetails).build();

  @BeforeAll
  static void setUpUri()
  {
    System.out.println(">>> CustomerIT.setUpURI()");
    requestSpecification = new RequestSpecBuilder()
      .setPort(dockerComposeContainer.getServicePort("wildfly", 8080))
      .build();
    log.info(">>> CustomerIT.setUpURI(): have initialized the RequestSpecification");
    /*RestAssured.given(requestSpecification)
      .contentType(MediaType.APPLICATION_JSON)
      .body(customer)
      .when()
      .post("/customers")
      .then()
      .statusCode(Response.Status.CREATED.getStatusCode());*/
  }

  @Test
  public void testGetCustomers()
  {
    log.info(">>> CustomersIT.testGetCustomers(): requestSpecification {}", requestSpecification);
    /*Response response = RestAssured.given(requestSpecification)
    .accept(MediaType.APPLICATION_JSON)
    .when()
    .get("/customers")
    .then()
    .extract()
    .as(Response.class);
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    List<Customer> customers = response.readEntity(new GenericType<List<Customer>>() {});
    assertThat(customers).isNotNull();
    assertThat(customers.size()).isEqualTo(1);
    assertThat(customers.get(0)).isEqualTo(customer);*/
  }

  /*@Test
  public void testGetCustomer()
  {
    Response response = RestAssured.given(requestSpecification)
      .accept(MediaType.APPLICATION_JSON)
      .pathParam("ref", customer.getCustomerRef())
      .when()
      .get("/customers/id/{ref}")
      .then()
      .extract()
      .as(Response.class);
    Long id = response.readEntity(Long.class);
    assertThat(id).isNotNull();
    response = RestAssured.given(requestSpecification)
      .accept(MediaType.APPLICATION_JSON)
      .pathParam("id", id)
      .when()
      .get("/customers/{id}")
      .then()
      .extract()
      .as(Response.class);
    Customer customer1 = response.readEntity(Customer.class);
    assertThat(customer1).isNotNull();
    assertThat(customer1).isEqualTo(customer);
  }*/
}
