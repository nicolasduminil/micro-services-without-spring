package fr.simplex_software.micro_services_without_spring.customers.tests.common;

import com.github.database.rider.core.*;
import com.github.database.rider.core.api.configuration.*;
import com.github.database.rider.core.api.dataset.*;
import com.github.database.rider.core.util.*;
import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;
import fr.simplex_software.micro_services_without_spring.customers.service.*;
import fr.simplex_software.micro_services_without_spring.customers.tests.*;
import lombok.extern.slf4j.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;

import java.io.*;
import java.util.*;

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
  @DataSet(strategy = SeedStrategy.CLEAN_INSERT, cleanBefore = false)
  @ExpectedDataSet(value = "datasets/customer-create-expected.yml", ignoreCols = "CUSTOMER_ID")
  public void testCreateCustomer()
  {
    entityManagerProvider.getEm().getTransaction().begin();
    Customer customer = unmarshalXmlFileToCustomer(new File("src/test/resources/customer.xml"));
    customerService.createCustomer(null, customer);
    entityManagerProvider.getEm().getTransaction().commit();
  }
}
