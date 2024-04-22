package dev.hobie.authorizationservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(AotHints.class)
public class AotConfiguration {}
