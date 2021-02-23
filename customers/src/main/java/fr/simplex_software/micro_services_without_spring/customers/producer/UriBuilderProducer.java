package fr.simplex_software.micro_services_without_spring.customers.producer;

import javax.enterprise.context.*;
import javax.enterprise.inject.*;
import javax.ws.rs.core.*;

@Dependent
public class UriBuilderProducer
{
  @Produces
  @RequestScoped
  public UriBuilder getUriBuilder()
  {
    return UriBuilder.fromPath("customers").scheme("http").host("localhost").port(8080);
  }
}
