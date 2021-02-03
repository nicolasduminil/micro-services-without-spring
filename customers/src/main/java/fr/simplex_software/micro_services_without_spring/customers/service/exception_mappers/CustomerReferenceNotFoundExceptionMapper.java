package fr.simplex_software.micro_services_without_spring.customers.service.exception_mappers;

import fr.simplex_software.micro_services_without_spring.customers.service.exceptions.*;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;

@Provider
public class CustomerReferenceNotFoundExceptionMapper implements ExceptionMapper<CustomerReferenceNotFound>
{
  @Override
  public Response toResponse(CustomerReferenceNotFound customerReferenceNotFound)
  {
    return Response.status(Response.Status.NOT_FOUND)
      .entity(customerReferenceNotFound.getMessage())
      .type(MediaType.TEXT_PLAIN_TYPE).build();
  }
}
