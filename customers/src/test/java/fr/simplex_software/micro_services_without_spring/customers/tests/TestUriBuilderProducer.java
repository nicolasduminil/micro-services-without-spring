package fr.simplex_software.micro_services_without_spring.customers.tests;

import fr.simplex_software.micro_services_without_spring.customers.producer.*;
import org.junit.*;
import org.junit.experimental.categories.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;

import javax.ws.rs.core.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Category(fr.simplex_software.micro_services_without_spring.customers.tests.ProfileServer.class)
public class TestUriBuilderProducer implements ProfileServer
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
