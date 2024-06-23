package dev.hobie.authorization_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

@Slf4j
// @Configuration
public class JsonConfiguration {

  private static JsonNode readJsonNode(JsonNode jsonNode, String field) {
    return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
  }

  @Bean
  ApplicationRunner parse() {
    return a -> {
      var gaList = new TypeReference<List<GrantedAuthority>>() {
      };
      var objectMapper = new ObjectMapper();
      SecurityJackson2Modules.getModules(ClassLoader.getSystemClassLoader())
          .forEach(objectMapper::registerModule);
      objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
      var json = """
              {"@class":"java.util.Collections$UnmodifiableMap","org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest":{"@class":"org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest","authorizationUri":"http://localhost:8080/oauth2/authorize","authorizationGrantType":{"value":"authorization_code"},"responseType":{"value":"code"},"clientId":"crm","redirectUri":"http://127.0.0.1:8082/login/oauth2/code/spring","scopes":["java.util.Collections$UnmodifiableSet",["user.read","openid"]],"state":"QjdbcbnM2uoxnwksbT1IooOOWxNbkdMVV0LDsptQuH4=","additionalParameters":{"@class":"java.util.Collections$UnmodifiableMap","nonce":"ryv3qPgr5IwFA6LYmLf1QkQY4fRtaZmg_ePB2rSJrqQ","continue":""},"authorizationRequestUri":"http://localhost:8080/oauth2/authorize?response_type=code&client_id=crm&scope=user.read%20openid&state=QjdbcbnM2uoxnwksbT1IooOOWxNbkdMVV0LDsptQuH4%3D&redirect_uri=http://127.0.0.1:8082/login/oauth2/code/spring&nonce=ryv3qPgr5IwFA6LYmLf1QkQY4fRtaZmg_ePB2rSJrqQ&continue=","attributes":{"@class":"java.util.Collections$UnmodifiableMap"}},"java.security.Principal":{"@class":"org.springframework.security.authentication.UsernamePasswordAuthenticationToken","authorities":["java.util.Collections$UnmodifiableRandomAccessList",[{"@class":"org.springframework.security.core.authority.SimpleGrantedAuthority","authority":"ROLE_USER"}]],"details":{"@class":"org.springframework.security.web.authentication.WebAuthenticationDetails","remoteAddress":"0:0:0:0:0:0:0:1","sessionId":"745F400BA9E8317369ECFD9B9E826695"},"authenticated":true,"principal":{"@class":"org.springframework.security.core.userdetails.User","password":null,"username":"jlong","authorities":["java.util.Collections$UnmodifiableSet",[{"@class":"org.springframework.security.core.authority.SimpleGrantedAuthority","authority":"ROLE_USER"}]],"accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true},"credentials":null}}
          """;
      var jsonNode = objectMapper.readTree(json);
      var authoritiesJsonNode = readJsonNode(jsonNode, "authorities").traverse(objectMapper);
      objectMapper
          .readValue(authoritiesJsonNode, gaList)
          .forEach(value -> log.info("Value {}", value));
    };
  }
}
