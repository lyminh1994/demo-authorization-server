package dev.hobie.authorizationservice;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.KeyDeserializers;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccumulatingSetupContext implements Module.SetupContext {

  private final Collection<Class<?>> classesToRegister;

  private final ObjectMapper objectMapper;

  AccumulatingSetupContext(ObjectMapper objectMapper, Collection<Class<?>> classes) {
    this.objectMapper = objectMapper;
    this.classesToRegister = classes;
  }

  @Override
  public Version getMapperVersion() {
    return null;
  }

  @Override
  public <C extends ObjectCodec> C getOwner() {
    return (C) this.objectMapper;
  }

  @Override
  public TypeFactory getTypeFactory() {
    return null;
  }

  @Override
  public boolean isEnabled(MapperFeature f) {
    return false;
  }

  @Override
  public boolean isEnabled(DeserializationFeature f) {
    return false;
  }

  @Override
  public boolean isEnabled(SerializationFeature f) {
    return false;
  }

  @Override
  public boolean isEnabled(JsonFactory.Feature f) {
    return false;
  }

  @Override
  public boolean isEnabled(JsonParser.Feature f) {
    return false;
  }

  @Override
  public boolean isEnabled(JsonGenerator.Feature f) {
    return false;
  }

  @Override
  public MutableConfigOverride configOverride(Class<?> type) {
    this.classesToRegister.add(type);
    return null;
  }

  @Override
  public void addDeserializers(Deserializers d) {}

  @Override
  public void addKeyDeserializers(KeyDeserializers s) {}

  @Override
  public void addSerializers(Serializers s) {}

  @Override
  public void addKeySerializers(Serializers s) {}

  @Override
  public void addBeanDeserializerModifier(BeanDeserializerModifier mod) {}

  @Override
  public void addBeanSerializerModifier(BeanSerializerModifier mod) {}

  @Override
  public void addAbstractTypeResolver(AbstractTypeResolver resolver) {}

  @Override
  public void addTypeModifier(TypeModifier modifier) {}

  @Override
  public void addValueInstantiators(ValueInstantiators instantiators) {}

  @Override
  public void setClassIntrospector(ClassIntrospector ci) {}

  @Override
  public void insertAnnotationIntrospector(AnnotationIntrospector ai) {}

  @Override
  public void appendAnnotationIntrospector(AnnotationIntrospector ai) {}

  @Override
  public void registerSubtypes(Class<?>... subtypes) {
    this.classesToRegister.addAll(Stream.of(subtypes).collect(Collectors.toSet()));
  }

  @Override
  public void registerSubtypes(NamedType... subtypes) {
    this.classesToRegister.addAll(
        Stream.of(subtypes).map(NamedType::getType).collect(Collectors.toSet()));
  }

  @Override
  public void registerSubtypes(Collection<Class<?>> subtypes) {
    this.classesToRegister.addAll(subtypes);
  }

  @Override
  public void setMixInAnnotations(Class<?> target, Class<?> mixinSource) {
    this.classesToRegister.add(target);
    this.classesToRegister.add(mixinSource);
  }

  @Override
  public void addDeserializationProblemHandler(DeserializationProblemHandler handler) {}

  @Override
  public void setNamingStrategy(PropertyNamingStrategy naming) {}
}
