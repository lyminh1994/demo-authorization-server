package dev.hobie.authorization_service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(AotHints.class)
public class AotConfiguration {
}
