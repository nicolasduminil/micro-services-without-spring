package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.entities.*;
import fr.simplex_software.micro_services_without_spring.customers.model.mappers.*;
import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;
import org.junit.*;
import org.junit.experimental.categories.*;

import static org.assertj.core.api.Assertions.*;

import java.io.*;

@Category(fr.simplex_software.micro_services_without_spring.customers.tests.ProfileServer.class)
public class TestCustomerMapper extends TestBase implements ProfileServer
{
  @Test
  public void testCustomerMapper()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer.xml"));
    assertThat(customer).isNotNull();
    CustomerEntity customerEntity = CustomerMapper.INSTANCE.fromCustomer(customer);
    assertThat(customerEntity).isNotNull();
    assertThat(customerEntity.getCustomerRef()).isEqualTo("Customer1");
    customer = CustomerMapper.INSTANCE.fromEntity(customerEntity);
    assertThat(customer).isNotNull();
    assertThat(customer.getCustomerRef()).isEqualTo("Customer1");
  }
}
