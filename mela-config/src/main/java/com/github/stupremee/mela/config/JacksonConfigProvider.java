package com.github.stupremee.mela.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stupremee.mela.config.annotations.ConfigMapper;
import com.github.stupremee.mela.config.annotations.ConfigSource;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import java.io.IOException;
import java.io.InputStream;

/**
 * https://github.com/Stupremee
 *
 * @author Stu
 * @since 21.06.19
 */
final class JacksonConfigProvider implements Provider<Config> {

  private final ObjectMapper mapper;
  private final InputStream input;

  @Inject
  JacksonConfigProvider(@ConfigMapper ObjectMapper mapper, @ConfigSource InputStream input) {
    this.mapper = mapper;
    this.input = input;
  }

  @Override
  public Config get() {
    try {
      JsonNode node = mapper.readTree(input);
      return new JacksonConfig(node);
    } catch (IOException cause) {
      throw new ProvisionException("Failed to get Config.", cause);
    }
  }
}