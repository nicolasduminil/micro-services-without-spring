package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.*;
import groovy.util.logging.*;
import io.cucumber.java.en.*;
import io.restassured.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;

import javax.ws.rs.core.*;
import javax.xml.bind.*;
import java.io.*;
import java.net.*;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class CustomersCucumberSteps
{
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
  private io.restassured.response.Response response;
  private Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer.xml"));

  static
  {
    wildfly.start();
  }

  @Given("URI is initialized")
  public void uriIsInitialized()
  {
    baseUri = UriBuilder.fromPath("customers")
      .scheme("http")
      .host(wildfly.getHost())
      .port(wildfly.getFirstMappedPort())
      .build();
    finalUri = UriBuilder.fromUri(baseUri).path("test").path("customers").build();
  }

  @When("user wants to create customer")
  public void userWantsToCreateCustomer()
  {
    log.info(">>> CustomersCucumberSteps.userWantsToCreateCustomer(): finalUri {}", finalUri);
    response =
      RestAssured.given()
        .accept(MediaType.APPLICATION_XML)
        .contentType(MediaType.APPLICATION_XML)
        .body(customer)
        .when()
        .post(finalUri);
  }

  @Then("customer is successfully created")
  public void customerIsSuccessfullyCreated()
  {
    response.then()
      .statusCode(HttpStatus.SC_CREATED);
  }

  @When("user wants to get customers list")
  public void userWantsToGetCustomersList()
  {
    response = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .get(finalUri);
  }

  @Then("customers list is returned")
  public void customersListIsReturned()
  {
    response.then()
      .statusCode(HttpStatus.SC_OK);
  }

  @When("user wants to get customer associated with id")
  public void userWantsToGetCustomerAssociatedWithId()
  {
    id = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .get(UriBuilder.fromUri(finalUri).path("id").path("{ref}").build(customer.getCustomerRef()))
      .then()
      .statusCode(HttpStatus.SC_OK)
      .extract()
      .response()
      .body()
      .asString();
    assertThat(id).isNotNull();
    response = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .get(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong(id)));
  }

  @Then("customer is returned")
  public void customerIsReturned()
  {
    response.then()
      .statusCode(HttpStatus.SC_OK);
  }

  @When("user wants to update customer with id")
  public void userWantsToUpdateCustomerWithId()
  {
    response = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .put(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong(id)));
  }

  @Then("customer is updated")
  public void customerIsUpdated()
  {
    response.then()
      .statusCode(HttpStatus.SC_CREATED);
  }

  @When("user wants to remove customer with id")
  public void userWantsToRemoveCustomerWithId()
  {
    response = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .delete(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong(id)));
  }

  @Then("customer is removed")
  public void customerIsRemoved()
  {
    response.then()
      .statusCode(HttpStatus.SC_OK);
  }

  private Customer unmarshalXmlFileToCustomer(File xml)
  {
    Customer customer = null;
    try
    {
      customer = (Customer) JAXBContext.newInstance(Customer.class).createUnmarshaller().unmarshal(xml);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return customer;
  }

}
