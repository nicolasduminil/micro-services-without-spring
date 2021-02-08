package fr.simplex_software.micro_services_without_spring.customers.tests;

import io.cucumber.junit.*;
import org.junit.runner.*;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources/it/features")
public class CustomersCucumberIT
{
}
