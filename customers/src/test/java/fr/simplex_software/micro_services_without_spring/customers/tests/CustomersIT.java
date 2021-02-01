package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.*;
import io.restassured.*;
import lombok.extern.slf4j.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;

import javax.mail.internet.*;
import javax.ws.rs.core.*;
import javax.xml.bind.*;
import java.io.*;
import java.net.*;

import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class CustomersIT
{
  private static CustomerAddress customerAddress = CustomerAddress.builder().number(35).street("Sawayn Brooks")
    .city("Kedham").zip("60018").country("America").build();
  private static CustomerContactDetails customerContactDetails;
  private static final GenericContainer<?> wildfly =
    new GenericContainer<>("customers:1.0-SNAPSHOT")
      .withExposedPorts(8080, 9990)
      .withNetwork(Network.newNetwork())
      .withNetworkAliases("wildfly-container-alias")
      .withLogConsumer(new Slf4jLogConsumer(log))
      .waitingFor(Wait.forLogMessage(".*WFLYSRV0051.*", 1));
  private static URI baseUri;
  private static URI finalUri;
  private static String id;

  static
  {
    try
    {
      customerContactDetails = CustomerContactDetails.builder().firstName("Emory").lastName("BARTON")
        .emailAddress(new InternetAddress("emory.barton@cocoks.com")).address(customerAddress).build();
    } catch (AddressException e)
    {
      e.printStackTrace();
    }
    wildfly.start();
  }

  private static Customer customer = Customer.builder().customerRef("Customer1").customerType(CustomerType.LOYAL)
    .contactDetails(customerContactDetails).build();

  @BeforeAll
  public static void beforeAll()
  {
    baseUri = UriBuilder.fromPath("customers")
      .scheme("http")
      .host(wildfly.getHost())
      .port(wildfly.getFirstMappedPort())
      .build();
    finalUri = UriBuilder.fromUri(baseUri).path("test").path("customers").build();
  }

  @Test
  @Order(1)
  public void createCustomerShouldReturn201()
  {
    RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .statusCode(HttpStatus.SC_CREATED);
  }

  @Test
  @Order(2)
  public void createCustomerShouldReturn405()
  {
    RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(new String())
      .when()
      .post(UriBuilder.fromUri(finalUri).path("none").build())
      .then()
      .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
  }

  @Test
  @Order(3)
  public void createCustomerShouldReturn404()
  {
    RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(new String())
      .when()
      .post(UriBuilder.fromUri(baseUri).path("none").build())
      .then()
      .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
  }

  @Test
  @Order(4)
  public void getCustomersShouldReturn200() throws JAXBException
  {
    String strCustomers = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .get(finalUri)
      .then()
      .statusCode(200)
      .extract()
      .response()
      .body()
      .asPrettyString();
    Customers customers = (Customers) JAXBContext.newInstance(Customers.class).createUnmarshaller()
      .unmarshal(new StringReader(strCustomers));
    assertThat(customers).isNotNull();
    assertThat(customers.getCustomers()).isNotNull();
    assertThat(customers.getCustomers().size()).isNotNull();
    assertThat(customers.getCustomers().size()).isEqualTo(1);
    Customer customer = customers.getCustomers().get(0);
    assertThat(customer).isNotNull();
    assertThat(customer.getContactDetails().getFirstName()).isEqualTo("Emory");
  }

  @Test
  @Order(5)
  public void getCustomerShouldReturn200() throws JAXBException
  {
    id = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .get(UriBuilder.fromUri(finalUri).path("id").path("{ref}").build(customer.getCustomerRef()))
      .then()
      .statusCode(200)
      .extract()
      .response()
      .body()
      .asString();
    assertThat(id).isNotNull();
    String strCustomer = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .get(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong(id)))
      .then()
      .statusCode(200)
      .extract()
      .response()
      .body()
      .asPrettyString();
    Customer customer = (Customer) JAXBContext.newInstance(Customer.class).createUnmarshaller()
      .unmarshal(new StringReader(strCustomer));
    assertThat(customer).isNotNull();
    assertThat(customer.getContactDetails().getFirstName()).isEqualTo("Emory");
  }

  @Test
  @Order(6)
  public void getCustomerShouldReturnEmpty()
  {
    String strCustomer = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .get(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong("0")))
      .then()
      .statusCode(200)
      .extract()
      .response()
      .body()
      .asPrettyString();
    assertThat(strCustomer).isNotNull();
    assertThat(strCustomer).isEmpty();
  }

  @Test
  @Order(7)
  public void removeCustomerShouldReturn200()
  {
    RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .delete(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong(id)))
      .then()
      .statusCode(200);
  }

  @Test
  @Order(8)
  public void removeCustomerShouldReturn404()
  {
    RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .delete(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong("0")))
      .then()
      .statusCode(200);
  }
}
