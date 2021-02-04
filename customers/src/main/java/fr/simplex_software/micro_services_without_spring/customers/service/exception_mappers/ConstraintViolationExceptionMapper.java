package fr.simplex_software.micro_services_without_spring.customers.service.exception_mappers;

import javax.validation.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException>
{
  @Override
  public Response toResponse(ConstraintViolationException e)
  {
    return Response.status(Response.Status.NOT_FOUND).entity(prepareMessage(e)).build();
  }

  private String prepareMessage(ConstraintViolationException exception)
  {
    String msg = "";
    for (ConstraintViolation<?> cv : exception.getConstraintViolations())
      msg += cv.getPropertyPath() + " " + cv.getMessage() + "\n";
    return msg;
  }
}
