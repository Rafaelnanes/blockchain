package test.com.rbn.blockchain.util;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Set;

@Configuration
public class ConfigTests {

  @Bean
  public HttpServletRequest getHttpServletRequest() {
    return new MockHttpServletRequest();
  }

  @Bean
  public RestTemplate getRestTemplate() {
    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    Mockito.when(restTemplate.postForEntity(Mockito.any(URI.class), Mockito.any(Set.class), Mockito.any()))
           .thenReturn(responseEntity);
    return restTemplate;
  }

}
