package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.*;
import io.restassured.*;
import io.restassured.response.*;
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
    wildfly.start();
  }

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
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer.xml"));
    log.info (">>> CustomersIT.createCustomerShouldReturn201(): New created customer has URL {}",
      RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .statusCode(HttpStatus.SC_CREATED)
      .extract()
      .header("Location"));
  }

  @Test
  @Order(2)
  public void createCustomerShouldReturnRefMustNotBeEmpty()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-reference.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("customerRef must not be empty")).isTrue();
  }

  @Test
  @Order(3)
  public void createCustomerShouldReturnRefHasInvalidLength()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-ref-too-long.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("Customer reference Customer1234567890 length must be between 1 and 10")).isTrue();
  }

  @Test
  @Order(4)
  public void createCustomerShouldReturnTypeMustNotBeNull()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-type.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("customerType must not be null")).isTrue();
  }

  @Test
  @Order(5)
  public void createCustomerShouldReturnContactDetailsMustNotBeNull()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-details.xml"));
    String response = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString();
    assertThat(response)
      .contains("contactDetails.address must not be null")
      .contains("contactDetails.lastName must not be empty")
      .contains("contactDetails.phoneNumber must not be null")
      .contains("contactDetails.firstName must not be empty")
      .contains("contactDetails.emailAddress must not be null");
  }

  @Test
  @Order(6)
  public void createCustomerShouldReturnContactDetailsFirstNameMustNotBeEmpty()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-first-name.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("contactDetails.firstName must not be empty")).isTrue();
  }

  @Test
  @Order(7)
  public void createCustomerShouldReturnContactDetailsFirstNameHasInvalidLength()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-first-name-too-long.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("Customer contact detail first name Emory12345678901234567890 length must be between 1 and 20")).isTrue();
  }

  @Test
  @Order(8)
  public void createCustomerShouldReturnContactDetailsLastNameMustNotBeEmpty()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-last-name.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("contactDetails.lastName must not be empty")).isTrue();
  }

  @Test
  @Order(9)
  public void createCustomerShouldReturnContactDetailsLastNameHasInvalidLength()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-last-name-too-long.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("Customer contact detail last name BARTON 1234567890 1234567890 1234567890 1234567890 length must be between 1 and 40")).isTrue();
  }

  @Test
  @Order(10)
  public void createCustomerShouldReturnContactDetailsAddressMustNotBeNull()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-address.xml"));
    String response = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString();
      assertThat(response)
        .contains("contactDetails.address.zip must not be empty")
        .contains("contactDetails.address.street must not be empty")
        .contains("contactDetails.address.province must not be empty")
        .contains("Customer address number 0 should be at least 1")
        .contains("contactDetails.address.country must not be empty")
        .contains("contactDetails.address.city must not be empty");
  }

  @Test
  @Order(11)
  public void createCustomerShouldReturnContactDetailsAddressNumberMustNotBeZero()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-number.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("Customer address number 0 should be at least 1")).isTrue();
  }

  @Test
  @Order(12)
  public void createCustomerShouldReturnContactDetailsAddressStreetMustNotBeEmpty()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-street.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("contactDetails.address.street must not be empty")).isTrue();
  }

  @Test
  @Order(13)
  public void createCustomerShouldReturnContactDetailsAddressStreetInvalidLength()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-street-too-long.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("Customer address street Sawayn Brooks123456789012345678901234567890 length must be between 1 and 30")).isTrue();
  }

  @Test
  @Order(14)
  public void createCustomerShouldReturnContactDetailsAddressCityMustNotBeEmpty()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-city.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("contactDetails.address.city must not be empty")).isTrue();
  }

  @Test
  @Order(15)
  public void createCustomerShouldReturnContactDetailsAddressCityInvalidLength()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-city-too-long.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("Customer address city Kedham12345678901234567890 length must be between 1 and 20")).isTrue();
  }

  @Test
  @Order(16)
  public void createCustomerShouldReturnContactDetailsAddressProvinceMustNotBeEmpty()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-province.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("contactDetails.address.province must not be empty")).isTrue();
  }

  @Test
  @Order(17)
  public void createCustomerShouldReturnContactDetailsAddressProvinceInvalidLength()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-province-too-long.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("Customer address province toto 1234567890 1234567890 length must be between 1 and 20")).isTrue();
  }

  @Test
  @Order(18)
  public void createCustomerShouldReturnContactDetailsAddressZipMustNotBeEmpty()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-zip.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("contactDetails.address.zip must not be empty")).isTrue();
  }

  @Test
  @Order(19)
  public void createCustomerShouldReturnContactDetailsAddressZipInvalidLength()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-zip-too-long.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("Customer address zip 600180000 length must be between 1 and 5")).isTrue();
  }

  @Test
  @Order(20)
  public void createCustomerShouldReturnContactDetailsAddressCountryMustNotBeEmpty()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-country.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("contactDetails.address.country must not be empty")).isTrue();
  }

  @Test
  @Order(21)
  public void createCustomerShouldReturnContactDetailsAddressCountryInvalidLength()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-country-too-long.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("Customer address country America12345678901234567890 length must be between 1 and 20")).isTrue();
  }

  @Test
  @Order(22)
  public void createCustomerShouldReturnContactDetailsAddressEmailMustNotBeNull()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-email-address.xml"));
    assertThat(RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("contactDetails.emailAddress must not be null")).isTrue();
  }

  @Test
  @Order(23)
  public void createCustomerShouldReturnContactDetailsPhoneNumberMustNotBeNull()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer-no-phone-number.xml"));
    RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .post(finalUri)
      .then()
      .extract()
      .response()
      .getBody()
      .asString()
      .contains("contactDetails.phoneNumber must not be null");
  }

  @Test
  @Order(24)
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
  @Order(25)
  public void getCustomersShouldReturn200() throws JAXBException
  {
    String strCustomers = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .get(finalUri)
      .then()
      .statusCode(HttpStatus.SC_OK)
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
  @Order(26)
  public void getCustomerShouldReturn200() throws JAXBException
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer.xml"));
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
    String strCustomer = RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .get(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong(id)))
      .then()
      .statusCode(HttpStatus.SC_OK)
      .extract()
      .response()
      .body()
      .asPrettyString();
    customer = (Customer) JAXBContext.newInstance(Customer.class).createUnmarshaller()
      .unmarshal(new StringReader(strCustomer));
    assertThat(customer).isNotNull();
    assertThat(customer.getContactDetails().getFirstName()).isEqualTo("Emory");
  }

  @Test
  @Order(27)
  public void getCustomerShouldReturn404()
  {
    RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .get(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong("0")))
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  @Order(28)
  public void updateCustomerSholudReturn201()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer.xml"));
    RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .put(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong(id)))
      .then()
      .statusCode(HttpStatus.SC_CREATED);
  }

  @Test
  @Order(29)
  public void updateCustomerSholudReturn404()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer.xml"));
    RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .body(customer)
      .when()
      .put(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong("0")))
      .then()
      .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  @Order(30)
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
  @Order(31)
  public void removeCustomerShouldReturn404()
  {
    RestAssured.given()
      .accept(MediaType.APPLICATION_XML)
      .contentType(MediaType.APPLICATION_XML)
      .when()
      .delete(UriBuilder.fromUri(finalUri).path("{id}").build(Long.parseLong("0")))
      .then()
      .statusCode(404);
  }

  private void marshalCustomerToXmlFile(Customer customer)
  {
    try
    {
      Marshaller marshaller = JAXBContext.newInstance(Customer.class).createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(customer, new File("customer.xml"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
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
