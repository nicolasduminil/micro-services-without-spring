package fr.simplex_software.micro_services_without_spring.customers.producer;

import javax.annotation.*;
import javax.enterprise.context.*;
import javax.enterprise.inject.*;
import javax.sql.*;

//@ApplicationScoped
public class DataSourceProducer
{
  //@Resource(lookup = "java:jboss/datasources/CustomersDS")
  private DataSource dataSource;

  //@Produces
  public DataSource getDataSource()
  {
    return dataSource;
  }
}
