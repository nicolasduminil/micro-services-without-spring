package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.producer.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;

import javax.ws.rs.core.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestUriBuilderProducer
{
  @InjectMocks
  @Spy
  private UriBuilderProducer uriBuilderProducer;
  @Mock
  private UriBuilder uriBuilder;

  @Test
  public void testUriBuilderProducer()
  {
    when (uriBuilderProducer.getUriBuilder()).thenReturn(uriBuilder);
    assertThat (uriBuilderProducer.getUriBuilder()).isEqualTo(uriBuilder);
  }
}
