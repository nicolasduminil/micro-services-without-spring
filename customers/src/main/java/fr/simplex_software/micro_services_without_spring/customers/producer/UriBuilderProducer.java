package fr.simplex_software.micro_services_without_spring.customers.producer;

import javax.enterprise.inject.*;
import javax.ws.rs.core.*;

public class UriBuilderProducer
{
  @Produces
  public UriBuilder getUriBuilder()
  {
    return UriBuilder.fromPath("customers").scheme("http").host("localhost").port(8080);
  }
}
