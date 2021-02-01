package fr.simplex_software.micro_services_without_spring.customers;

import org.eclipse.microprofile.openapi.annotations.*;
import org.eclipse.microprofile.openapi.annotations.info.*;
import org.eclipse.microprofile.openapi.annotations.servers.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@ApplicationPath("/test")
@OpenAPIDefinition(info =
@Info(title = "Customers API", description = "Provides access to the API operations", version = "1.0.SNAPSHOT",
  license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")),
  servers = {
    @Server(url = "http://{host}:{port}", variables = {
      @ServerVariable(name = "host", defaultValue = "localhost"),
      @ServerVariable(name = "port", defaultValue = "8080")}
    ),
    @Server(url = "http://{host}:{port}/{context-path}", variables = {
      @ServerVariable(name = "host", defaultValue = "localhost"),
      @ServerVariable(name = "port", defaultValue = "8080"),
      @ServerVariable(name = "context-path", defaultValue = "customers")}
    )
  }
)
public class CustomerApp extends Application
{
}
