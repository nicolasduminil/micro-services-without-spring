package fr.simplex_software.micro_services_without_spring.customers.producer;

import javax.annotation.*;
import javax.annotation.sql.*;
import javax.ejb.*;
import javax.enterprise.inject.*;
import javax.sql.*;

@Singleton
@DataSourceDefinition(
  name = "java:jboss/datasources/CustomersDS",
  className = "org.h2.jdbcx.JdbcDataSource",
  url = "jdbc:h2:mem:customers",
  user = "sa",
  password = "sa",
  databaseName = "customers")
public class DatasourceProducer
{
  @Resource(lookup = "java:jboss/datasources/CustomersDS")
  private DataSource ds;

  @Produces
  public DataSource getDatasource()
  {
    return ds;
  }
}
