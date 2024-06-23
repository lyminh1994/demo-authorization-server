package dev.hobie.resource_service.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@UtilityClass
public class ClientCredentialsUtils {

  public String getJwtToken(RestTemplate restTemplate, String clientId, String clientSecret) {
    var headers = new HttpHeaders();
    headers.setBasicAuth(clientId, clientSecret);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    var body = new LinkedMultiValueMap<String, String>();
    body.add("grant_type", "client_credentials");
    body.add("scope", "user.read");

    var entity = new HttpEntity<>(body, headers);
    var url = "http://localhost:8080/oauth2/token";
    var response = restTemplate.postForEntity(url, entity, JsonNode.class);
    Assert.state(response.getStatusCode().is2xxSuccessful(), "the response needs to be 200x");
    JsonNode responseBody = response.getBody();
    Assert.notNull(responseBody, "response body should not be null");
    return responseBody.get("access_token").asText();

  }
}
