package dev.hobie.gatewayservice;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

  @Bean
  public RouteLocator gateway(RouteLocatorBuilder rlb) {
    var apiPrefix = "/api/";
    return rlb.routes()
        .route(
            rs ->
                rs.path(apiPrefix + "**")
                    .filters(
                        f ->
                            f.tokenRelay()
                                .rewritePath(apiPrefix + "(?<segment>.*)", "/$\\{segment}"))
                    .uri("http://localhost:8081")) // <1>
        .route(rs -> rs.path("/**").uri("http://localhost:8020")) // <2>
        .build();
  }
}
