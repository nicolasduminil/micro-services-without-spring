package fr.simplex_software.micro_services_without_spring.customers.service;

import fr.simplex_software.micro_services_without_spring.customers.model.*;
import lombok.extern.slf4j.*;
import org.eclipse.microprofile.openapi.annotations.*;
import org.eclipse.microprofile.openapi.annotations.enums.*;
import org.eclipse.microprofile.openapi.annotations.media.*;
import org.eclipse.microprofile.openapi.annotations.parameters.*;
import org.eclipse.microprofile.openapi.annotations.responses.*;

import javax.inject.*;
import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/customers")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Slf4j
public class CustomerResource
{
  @Inject
  private CustomerService customerService;
  @Inject
  private UriBuilder uriBuilder;

  @GET
  @Operation(operationId = "getCustomers", description = "Get the customers list")
  @APIResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Customer.class)))
  public Response getCustomers()
  {
    return Response.ok(customerService.getCustomers()).build();
  }

  @GET
  @Path("ref/{ref}")
  @Operation(operationId = "getCustomerByRef", description = "Get a customer by its reference")
  @APIResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Customer.class)))
  public Response getCustomerByRef (@Parameter(description = "ref") @PathParam("ref") final String ref)
  {
    return Response.ok(customerService.getCustomerByRef(ref)).build();
  }

  @GET
  @Path("id/{ref}")
  @Operation(operationId = "getCustomerIdByRef", description = "Get a customer ID by its reference")
  @APIResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Long.class)))
  public Response getCustomerIdByRef (@Parameter(description = "ref") @PathParam("ref") final String ref)
  {
    return Response.ok(Long.toString(customerService.getCustomerIdByRef(ref))).build();
  }

  @GET
  @Path("{id}")
  @Operation(operationId = "getCustomer", description = "Get a customer by its ID")
  @APIResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Customer.class)))
  public Response getCustomer (@Parameter(description = "id") @PathParam("id") final Long id)
  {
    return Response.ok(customerService.getCustomer(id)).build();
  }

  @POST
  @Operation(operationId = "creteCustomer", description = "Create a new customer")
  @RequestBody(name = "customer", required = true, content = @Content(mediaType = MediaType.APPLICATION_XML,
    schema = @Schema(implementation = Customer.class, type = SchemaType.STRING)))
  @APIResponse(responseCode = "201", description = "Customer created")
  @APIResponse(responseCode = "400", description = "Invalid request")
  public Response createCustomer (@Valid Customer customer)
  {
    Long id = new Random().nextLong();
    customerService.createCustomer(id, customer);
    return Response.created(uriBuilder.path("/customers/{id}").build(id)).build();
  }

  @PUT
  @Path("{id}")
  @Operation(operationId = "updateCustomer", description = "Update a customer by ID")
  @RequestBody(name = "customer", required = true, content = @Content(mediaType = MediaType.APPLICATION_XML,
    schema = @Schema(implementation = Customer.class, type = SchemaType.STRING)))
  @APIResponse(responseCode = "204", description = "Customer updated")
  @APIResponse(responseCode = "400", description = "Invalid request")
  public Response updateCustomer (@Parameter(description = "id") @PathParam("id")Long id, Customer customer)
  {
    customerService.updateCustomer(id, customer);
    return Response.created(uriBuilder.path("/customers/{id}").build(id)).build();
  }

  @DELETE
  @Path("{id}")
  @Operation(operationId = "removeCustomer", description = "Delete a customer by ID")
  @APIResponse(responseCode = "204", description = "Todo deleted")
  @APIResponse(responseCode = "404", description = "Todo with given id does not exist")
  @APIResponse(responseCode = "500", description = "Internal server error")
  public Response removeCustomer (@Parameter(description = "id") @PathParam("id") Long id)
  {
    customerService.removeCustomer(id);
    return Response.ok().build();
  }
}
