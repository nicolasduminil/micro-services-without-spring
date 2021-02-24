package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;
import fr.simplex_software.micro_services_without_spring.customers.service.exception_mappers.*;
import org.junit.*;
import org.junit.experimental.categories.*;

import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@Category(fr.simplex_software.micro_services_without_spring.customers.tests.ProfileServer.class)
public class TestConstraintViolationExceptionMapper implements ProfileServer
{
  @Test
  public void toResponse()
  {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();
    Customer customer = new Customer();
    Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);
    ConstraintViolationException exception = new ConstraintViolationException(constraintViolations);

    ConstraintViolationExceptionMapper constraintViolationExceptionMapper = new ConstraintViolationExceptionMapper();
    Response response = constraintViolationExceptionMapper.toResponse(exception);
    assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
  }
}
