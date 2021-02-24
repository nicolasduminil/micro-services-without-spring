package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;
import lombok.extern.slf4j.*;
import org.junit.experimental.categories.*;
import org.testcontainers.containers.*;
import org.testcontainers.containers.output.*;
import org.testcontainers.containers.wait.strategy.*;
import org.testcontainers.images.builder.*;
import org.testcontainers.junit.jupiter.Container;

import javax.xml.bind.*;
import java.io.*;

@Slf4j
@Category(fr.simplex_software.micro_services_without_spring.customers.tests.ProfileServer.class)
public class TestBase implements ProfileServer
{
  @Container
  public static final GenericContainer<?> wildfly = new GenericContainer(
    new ImageFromDockerfile().withDockerfileFromBuilder(builder -> builder
      .from("jboss/wildfly:21.0.0.Final")
      .run("/opt/jboss/wildfly/bin/add-user.sh admin admin --silent")
      .add("target/customers.war", "/opt/jboss/wildfly/standalone/deployments")
      .entryPoint("exec /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0")
      .build())
      .withFileFromFile("target/customers.war", new File("target/customers.war")))
    .withExposedPorts(8080, 9990)
    .withNetwork(Network.newNetwork())
    .withNetworkAliases("wildfly-container-alias")
    .withLogConsumer(new Slf4jLogConsumer(log))
    .waitingFor(Wait.forLogMessage(".*WFLYSRV0051.*", 1));

  public void marshalCustomerToXmlFile(Customer customer)
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

  public Customer unmarshalXmlFileToCustomer(File xml)
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
