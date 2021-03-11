package fr.simplex_software.micro_services_without_spring.customers.tests.common;

import com.github.database.rider.core.*;
import com.github.database.rider.core.api.configuration.*;
import com.github.database.rider.core.api.dataset.*;
import com.github.database.rider.core.util.*;
import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;
import fr.simplex_software.micro_services_without_spring.customers.service.*;
import fr.simplex_software.micro_services_without_spring.customers.tests.*;
import javassist.*;
import lombok.extern.slf4j.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;

import javax.persistence.*;
import java.io.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@RunWith(JUnit4.class)
@DBUnit(caseSensitiveTableNames = true, escapePattern = "\"?\"")
@Slf4j
public class CustomerServiceIT extends TestCommon
{
  @ClassRule
  public static GenericContainer database = new GenericContainer<>("oracleinanutshell/oracle-xe-11g:latest")
    .withExposedPorts(1521)
    .withEnv ("ORACLE_ALLOW_REMOTE", "true")
    .withEnv("ORACLE_DISABLE_ASYNCH_IO", "true")
    .withClasspathResourceMapping("scripts/oracle", "/docker-entrypoint-initdb.d", BindMode.READ_WRITE)
    .withLogConsumer(new Slf4jLogConsumer(log))
    .waitingFor(Wait.forLogMessage(".*SQL>.*", 1));

  private static Map<String, String> entityManagerProviderProperties = new HashMap<>();

  @BeforeClass
  public static void setUpDatabase()
  {
    entityManagerProviderProperties.put("javax.persistence.jdbc.url", String.format("jdbc:oracle:thin:@localhost:%d:xe", database.getFirstMappedPort()));
  }

  @Rule
  public EntityManagerProvider entityManagerProvider = EntityManagerProvider.instance("customers-oracle", entityManagerProviderProperties);

  @Rule
  public DBUnitRule dbUnitRule = DBUnitRule.instance(entityManagerProvider.connection());

  private CustomerService customerService;

  @Before
  public void setUp()
  {
    customerService = new CustomerServiceJpa(entityManagerProvider.getEm());
  }

  @Test
  @DataSet(value = "datasets/customers.yml", strategy = SeedStrategy.CLEAN_INSERT)
  @ExpectedDataSet(value = "datasets/customer-create-expected.yml", ignoreCols = "CUSTOMER_ID")
  public void testCreateCustomer()
  {
    entityManagerProvider.getEm().getTransaction().begin();
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer.xml"));
    customerService.createCustomer(null, customer);
    entityManagerProvider.getEm().getTransaction().commit();
  }

  @Test
  @DataSet(value = "datasets/customers.yml", strategy = SeedStrategy.CLEAN_INSERT)
  public void testGetCustomers()
  {
    assertThat(customerService.getCustomers().getCustomers()).hasSize(5);
  }

  @Test
  @DataSet(value = "datasets/customers.yml", strategy = SeedStrategy.CLEAN_INSERT)
  public void testGetCustomerByRef()
  {
    Customer customer = customerService.getCustomerByRef("Customer1");
    assertThat(customer).isNotNull();
  }

  @Test
  @DataSet(value = "datasets/customers.yml", strategy = SeedStrategy.CLEAN_INSERT)
  public void getCustomerById()
  {
    Long id = customerService.getCustomerIdByRef("Customer1");
    assertThat(id).isNotNull();
    Customer customer = customerService.getCustomer(id);
    assertThat(customer).isNotNull();
    assertThat(customer.getCustomerRef()).isEqualTo("Customer1");
  }

  @Test
  @DataSet(value = "datasets/customers.yml", strategy = SeedStrategy.CLEAN_INSERT)
  @ExpectedDataSet(value = "datasets/customer-update-expected.yml", ignoreCols = "CUSTOMER_ID")
  public void updateCustomer()
  {
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer.xml"));
    customer.setCustomerRef("Customer20");
    Long id = customerService.getCustomerIdByRef("Customer1");
    assertThat(id).isNotNull();
    entityManagerProvider.getEm().getTransaction().begin();
    customerService.updateCustomer(id, customer);
    entityManagerProvider.getEm().getTransaction().commit();
    assertThat(customerService.getCustomerIdByRef("Customer20")).isEqualTo(id);
  }

  @Test
  @DataSet(value = "datasets/customers.yml", strategy = SeedStrategy.CLEAN_INSERT)
  @ExpectedDataSet(value = "datasets/customer-delete-expected.yml", ignoreCols = "CUSTOMER_ID")
  public void removeCustomer()
  {
    entityManagerProvider.getEm().getTransaction().begin();
    customerService.removeCustomer(customerService.getCustomerIdByRef("Customer1"));
    entityManagerProvider.getEm().getTransaction().commit();
    Long id = null;
    try
    {
      id = customerService.getCustomerIdByRef("Customer1");
    }
    catch (NoResultException ex) {}
    assertThat(id).isNull();
  }
}
