package fr.simplex_software.micro_services_without_spring.customers.service;

import fr.simplex_software.micro_services_without_spring.customers.*;
import lombok.extern.slf4j.*;
import org.eclipse.microprofile.config.inject.*;
import org.eclipse.microprofile.health.*;

import javax.enterprise.context.*;
import javax.inject.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.*;

@Readiness
@ApplicationScoped
@Slf4j
public class CustomerResourceHealthCheck implements HealthCheck
{
  @Inject
  @ConfigProperty(name = "default.http.port")
  private Integer port;
  @Inject
  @ConfigProperty(name = "default.ip.address")
  private String host;

  @Inject
  @ConfigProperty(name = "app.context.root")
  private String contextRoot;

  @Override
  public HealthCheckResponse call()
  {
    return HealthCheckResponse.up(">>> CustomerResourceHealthCheck.call(): Readiness health check");
  }

  private URI getResourceUri()
  {
    return UriBuilder.fromPath(contextRoot)
      .path(CustomerApp.class.getAnnotation(ApplicationPath.class).value())
      .path(CustomerResource.class.getAnnotation(Path.class).value())
      .scheme("http")
      .host(host)
      .port(port)
      .build();
  }
}
