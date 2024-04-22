package dev.hobie.processor;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {

  @Bean
  public Queue queue() {
    return QueueBuilder.durable(Constants.RABBITMQ_DESTINATION_NAME).build();
  }

  @Bean
  public Exchange exchange() {
    return ExchangeBuilder.directExchange(Constants.RABBITMQ_DESTINATION_NAME).build();
  }

  @Bean
  public Binding binding() {
    return BindingBuilder.bind(queue())
        .to(exchange())
        .with(Constants.RABBITMQ_DESTINATION_NAME)
        .noargs();
  }
}
