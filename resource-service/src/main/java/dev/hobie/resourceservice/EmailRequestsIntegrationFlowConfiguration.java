package dev.hobie.resourceservice;

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
public class EmailRequestsIntegrationFlowConfiguration {

  @Bean
  public IntegrationFlow emailRequestsIntegrationFlow(
      MessageChannel requests, AmqpTemplate template) {
    String destinationName = "emails";
    var outboundAmqpAdapter = Amqp.outboundAdapter(template).routingKey(destinationName); // <1>

    return IntegrationFlow.from(requests) // <2>
        .transform(new ObjectToJsonTransformer()) // <3>
        .handle(outboundAmqpAdapter) // <4>
        .get();
  }

  @Bean
  public DirectChannelSpec requests() {
    return MessageChannels.direct();
  }
}
