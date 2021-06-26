package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;
import fr.simplex_software.micro_services_without_spring.customers.service.*;
import fr.simplex_software.micro_services_without_spring.customers.service.exceptions.*;
import lombok.extern.slf4j.*;
import org.jboss.resteasy.specimpl.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import javax.ws.rs.core.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
public class TestCustomers
{
  private static final CustomerServiceDefault customerServiceDefault = new CustomerServiceDefault();
  @Mock
  private UriBuilder resteasyUriBuilder;
  @InjectMocks
  private CustomerResource customerResource;
  private static final CustomerAddress customerAddress = CustomerAddress.builder().number(26).street("allée des Sapins")
    .city("Soisy sous Montmorency").zip("95230").country("France").build();
  private static final CustomerContactDetails customerContactDetails = CustomerContactDetails.builder().firstName("Nicolas").lastName("DUMINIL")
    .emailAddress("nicolas.duminil@wanadoo.fr").address(customerAddress).build();

  private static final Customer customer = Customer.builder().customerRef("Customer1").customerType(CustomerType.LOYAL)
    .contactDetails(customerContactDetails).build();

  @BeforeEach
  public void setUp()
  {
    customerResource.setCustomerService(customerServiceDefault);
  }

  @Test
  @Order(1)
  public void testCreateCustomer()
  {
    when(resteasyUriBuilder.path("/customers/{id}")).thenReturn(new ResteasyUriBuilder().path("/customers/{id}"));
    Response response = customerResource.createCustomer(customer);
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
    assertThat(response.getLocation().toString()).startsWith("/customers");
  }

  @Test
  @Order(2)
  public void testUpdateCustomer()
  {
    when(resteasyUriBuilder.path("/customers/{id}")).thenReturn(new ResteasyUriBuilder().path("/customers/{id}"));
    Response response = customerResource.updateCustomer(1L, customer);
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
    assertThat(response.getLocation().toString()).startsWith("/customers");
  }

  @Test
  @Order(3)
  public void testGetCustomer()
  {
    Response response = customerResource.getCustomer(1L);
    assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    assertThat(response.readEntity(Customer.class)).isEqualTo(customer);
  }

  @Test
  @Order(4)
  public void testGetCustomers()
  {
    Response response = customerResource.getCustomers();
    assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    Customers customers = response.readEntity(Customers.class);
    assertThat(customers).isNotNull();
    assertThat(customers.getCustomers()).isNotNull();
    assertThat(customers.getCustomers().size()).isEqualTo(1);
    assertThat(customers.getCustomers().get(0)).isEqualTo(customer);
  }

  @Test
  @Order(7)
  public void testDeleteCustomer()
  {
    Response response = customerResource.removeCustomer(1L);
    assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
  }

  @Test
  @Order(5)
  public void testGetCustomerByRef()
  {
    Response response = customerResource.getCustomerByRef("Customer1");
    assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    assertThat(response.readEntity(Customer.class)).isEqualTo(customer);
  }

  @Test
  @Order(6)
  public void testGetCustomerIdByRef()
  {
    Response response = customerResource.getCustomerIdByRef("Customer1");
    assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    assertThat(response.readEntity(String.class)).isEqualTo("1");
  }

  @Test
  @Order(8)
  public void testCustomerService()
  {
    CustomerServiceDefault customerService1 = new CustomerServiceDefault();
    customerService1.createCustomer(123L, customer);
    assertThat(customerService1.getCustomers()).isNotNull();
    assertThat(customerService1.getCustomers().getCustomers()).isNotNull();
    assertThat(customerService1.getCustomers().getCustomers().size()).isNotZero();
    assertThat(customerService1.getCustomer(123L)).isNotNull();
    assertThat(customerService1.getCustomer(123L).getCustomerRef()).isEqualTo("Customer1");
    customer.setCustomerRef("Customer2");
    customerService1.updateCustomer(123L, customer);
    assertThat(customerService1.getCustomer(123L)).isNotNull();
    assertThat(customerService1.getCustomer(123L).getCustomerRef()).isEqualTo("Customer2");
    assertThat(customerService1.getCustomerByRef("Customer2")).isNotNull();
    assertThat(customerService1.getCustomerByRef("Customer2").getCustomerType()).isEqualTo(CustomerType.LOYAL);
    assertThat(customerService1.getCustomerIdByRef("Customer2")).isNotNull();
    assertThat(customerService1.getCustomerIdByRef("Customer2")).isEqualTo(Long.valueOf(123));
    customerService1.removeCustomer(123L);
    assertThat(customerService1.getCustomer(123L)).isNull();
    assertThatThrownBy(() -> customerService1.getCustomerIdByRef("toto")).isInstanceOf(CustomerReferenceNotFound.class);
  }
}
