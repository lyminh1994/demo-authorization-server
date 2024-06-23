package dev.hobie.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.util.Assert;

@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements ChannelInterceptor {

  private final JwtAuthenticationProvider authenticationProvider; // <1>

  private final String headerName; // <2>

  @Override
  public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
    var token = (String) message.getHeaders().get(headerName); // <3>
    Assert.hasText(token, "the token must be non-empty!");

    var authentication = this.authenticationProvider.authenticate(new BearerTokenAuthenticationToken(token)); // <4>

    if (authentication != null && authentication.isAuthenticated()) { // <5>
      var upt = UsernamePasswordAuthenticationToken.authenticated(
          authentication.getName(), null, AuthorityUtils.NO_AUTHORITIES);
      return MessageBuilder.fromMessage(message).setHeader(headerName, upt).build();
    }

    return MessageBuilder.fromMessage(message).setHeader(headerName, null).build(); // <6>
  }
}
