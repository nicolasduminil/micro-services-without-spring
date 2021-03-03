package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.model.pojos.*;

import javax.xml.bind.*;
import java.io.*;

public class TestCommon
{
  public void marshalCustomerToXmlFile(Customer customer)
  {
    try
    {
      Marshaller marshaller = JAXBContext.newInstance(Customer.class).createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(customer, new File("customer.xml"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public Customer unmarshalXmlFileToCustomer(File xml)
  {
    Customer customer = null;
    try
    {
      customer = (Customer) JAXBContext.newInstance(Customer.class).createUnmarshaller().unmarshal(xml);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return customer;
  }
}
