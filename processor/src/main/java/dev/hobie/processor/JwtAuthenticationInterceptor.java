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

  private final JwtAuthenticationProvider authenticationProvider;

  private final String headerName;

  @Override
  public Message<?> preSend(Message<?> message, @NonNull MessageChannel channel) {
    var token = (String) message.getHeaders().get(headerName);
    Assert.hasText(token, "the token must be non-empty!");

    var authentication =
        this.authenticationProvider.authenticate(new BearerTokenAuthenticationToken(token));

    if (authentication != null && authentication.isAuthenticated()) {
      var upt =
          UsernamePasswordAuthenticationToken.authenticated(
              authentication.getName(), null, AuthorityUtils.NO_AUTHORITIES);
      return MessageBuilder.fromMessage(message).setHeader(headerName, upt).build();
    }

    return MessageBuilder.fromMessage(message).setHeader(headerName, null).build();
  }
}
