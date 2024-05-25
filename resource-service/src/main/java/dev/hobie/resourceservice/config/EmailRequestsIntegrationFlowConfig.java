package dev.hobie.resourceservice.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.messaging.MessageChannel;

@Configuration
public class EmailRequestsIntegrationFlowConfig {

  @Bean
  public IntegrationFlow emailRequestsIntegrationFlow(
      MessageChannel requests, AmqpTemplate template) {
    String destinationName = "emails";
    var outboundAmqpAdapter = Amqp.outboundAdapter(template).routingKey(destinationName);

    return IntegrationFlow.from(requests)
        .transform(new ObjectToJsonTransformer())
        .handle(outboundAmqpAdapter)
        .get();
  }

  @Bean
  public DirectChannelSpec requests() {
    return MessageChannels.direct();
  }
}
