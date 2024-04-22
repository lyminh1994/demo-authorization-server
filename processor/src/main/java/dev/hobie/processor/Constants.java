package dev.hobie.processor;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

  public static final String REQUESTS_MESSAGE_CHANNEL = "requests";

  public static final String RABBITMQ_DESTINATION_NAME = "emails";

  public static final String AUTHORIZATION_HEADER_NAME = "jwt";
}
