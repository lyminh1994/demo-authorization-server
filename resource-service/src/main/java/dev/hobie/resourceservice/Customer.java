package dev.hobie.resourceservice;

import org.springframework.data.annotation.Id;

public record Customer(@Id Integer id, String name, String email) {}
