package dev.hobie.authorization_service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.Principal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.SavedCookie;

@Slf4j
public class AotHints implements RuntimeHintsRegistrar {

  private static Collection<Class<?>> registerJacksonModuleDeps(Set<Class<?>> moduleClasses) {
    var set = new HashSet<Class<?>>();
    var classLoader = AotConfiguration.class.getClassLoader();
    var securityModules = new ArrayList<Module>();
    securityModules.addAll(SecurityJackson2Modules.getModules(classLoader));
    securityModules.addAll(
        moduleClasses.stream()
            .map(
                cn -> {
                  try {
                    for (var ctor : cn.getConstructors())
                      if (ctor.getParameterCount() == 0)
                        return (Module) ctor.newInstance();
                  } catch (InvocationTargetException
                      | IllegalAccessException
                      | InstantiationException t) {
                    log.error("couldn't construct and inspect module {}", cn.getName());
                  }
                  return null;
                })
            .collect(Collectors.toSet()));
    var om = new ObjectMapper();
    var sc = new AccumulatingSetupContext(om, set);
    for (var module : securityModules) {
      set.add(module.getClass());
      module.setupModule(sc);
      module.getDependencies().forEach(m -> set.add(m.getClass()));
    }

    return set;
  }

  private Set<Class<?>> subs(Reflections reflections, Class<?>... classesToFind) {
    var all = new HashSet<Class<?>>();
    for (var individualClass : classesToFind) {
      var subTypesOf = reflections.getSubTypesOf(individualClass);
      all.addAll(subTypesOf);
    }
    return all;
  }

  private Set<Class<?>> resolveJacksonTypes() {
    var all = new HashSet<Class<?>>();
    for (var pkg : Set.of("com.fasterxml", "org.springframework")) {
      var reflections = new Reflections(pkg);
      all.addAll(subs(reflections, JsonDeserializer.class, JsonSerializer.class, Module.class));
      all.addAll(reflections.getTypesAnnotatedWith(JsonTypeInfo.class));
      all.addAll(reflections.getTypesAnnotatedWith(JsonAutoDetect.class));
    }
    all.addAll(
        registerJacksonModuleDeps(
            all.stream().filter(Module.class::isAssignableFrom).collect(Collectors.toSet())));
    return all;
  }

  @Override
  public void registerHints(@NonNull RuntimeHints hints, ClassLoader classLoader) {

    var javaClasses = Set.of(
        ArrayList.class,
        Date.class,
        Duration.class,
        Instant.class,
        URL.class,
        TreeMap.class,
        HashMap.class,
        LinkedHashMap.class,
        List.class);

    var savedRequestClasses = Set.of(DefaultSavedRequest.class, SavedCookie.class);

    var oauth2CoreClasses = Set.of(
        SignatureAlgorithm.class,
        OAuth2AuthorizationResponseType.class,
        OAuth2AuthorizationRequest.class,
        AuthorizationGrantType.class,
        OAuth2TokenFormat.class,
        OAuth2Authorization.class,
        SecurityContextImpl.class);

    var securityClasses = Set.of(
        User.class,
        WebAuthenticationDetails.class,
        GrantedAuthority.class,
        Principal.class,
        SimpleGrantedAuthority.class,
        UsernamePasswordAuthenticationToken.class);

    var servletClasses = Set.of(Cookie.class);

    var jacksonTypes = new HashSet<>(resolveJacksonTypes());
    jacksonTypes.add(SecurityJackson2Modules.class);

    var classes = new ArrayList<Class<?>>();
    classes.addAll(jacksonTypes);
    classes.addAll(servletClasses);
    classes.addAll(oauth2CoreClasses);
    classes.addAll(savedRequestClasses);
    classes.addAll(javaClasses);
    classes.addAll(securityClasses);

    var stringClasses = Map.of(
        "java.util.", Set.of("Arrays$ArrayList"),
        "java.util.Collections$",
        Set.of(
            "UnmodifiableRandomAccessList",
            "EmptyList",
            "UnmodifiableMap",
            "EmptyMap",
            "SingletonList",
            "UnmodifiableSet"));

    var all = classes.stream().map(Class::getName).collect(Collectors.toCollection(HashSet::new));
    stringClasses.forEach((root, setOfClasses) -> setOfClasses.forEach(cn -> all.add(root + cn)));

    var memberCategories = MemberCategory.values();

    all.forEach(
        type -> {
          var typeReference = TypeReference.of(type);
          hints.reflection().registerType(typeReference, memberCategories);
          try {
            var clzz = Class.forName(typeReference.getName());
            if (Serializable.class.isAssignableFrom(clzz)) {
              hints.serialization().registerType(typeReference);
            }
          } catch (ClassNotFoundException t) {
            log.error(
                "couldn't register serialization hint for {}:{}",
                typeReference.getName(),
                t.getMessage());
          }
        });

    Set.of("data", "schema")
        .forEach(folder -> hints.resources().registerPattern("sql/" + folder + "/*sql"));

    Set.of("key", "pub")
        .forEach(
            suffix -> hints.resources().registerResource(new ClassPathResource("app." + suffix)));
  }
}
