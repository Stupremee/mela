package com.github.stupremee.mela.configuration;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import javax.annotation.Nonnull;

/**
 * https://github.com/Stupremee
 *
 * @author Stu
 * @since 23.03.2019
 */
public interface ConfigurationFactory {

  /**
   * Creates and returns a new {@link ConfigurationFactory} that parses yaml files.
   *
   * @return The {@link ConfigurationFactory} that can parse yaml files
   */
  @Nonnull
  static ConfigurationFactory yaml() {
    return new YamlConfigurationFactory();
  }

  /**
   * Creates and returns a new {@link ConfigurationFactory} that parses hocon files.
   *
   * @return The {@link ConfigurationFactory} that can parse hocon files
   */
  @Nonnull
  static ConfigurationFactory hocon() {
    return new HoconConfigurationFactory();
  }

  /**
   * Adds a default value to the Configuration.
   *
   * @param path The dotted path
   * @param value The default value
   * @return This instance.
   */
  @Nonnull
  ConfigurationFactory addDefault(@Nonnull String path, @Nonnull Object value);

  /**
   * Overwrites the old setDefaults with the new one.
   *
   * @param defaults The new setDefaults
   * @return This instance.
   */
  @Nonnull
  ConfigurationFactory setDefaults(@Nonnull Configuration defaults);

  /**
   * Loads a {@link Configuration} from raw content.
   *
   * @param content The raw toml source.
   * @return The loaded {@link Configuration}
   * @throws IOException When something went wrong
   */
  @Nonnull
  default Configuration load(@Nonnull String content) throws IOException {
    return load(new StringReader(content));
  }

  /**
   * Loads a {@link Configuration} from a path as {@link Path}.
   *
   * @param path The path as {@link Path}
   * @return The loaded {@link Configuration}
   * @throws IOException When something went wrong
   */
  @Nonnull
  default Configuration load(@Nonnull Path path) throws IOException {
    return load(path.toFile());
  }

  /**
   * Loads a {@link Configuration} from a file as {@link File}.
   *
   * @param file The file as {@link File}
   * @return The loaded {@link Configuration}
   * @throws IOException When something went wrong
   */
  @Nonnull
  Configuration load(@Nonnull File file) throws IOException;

  /**
   * Loads a {@link Configuration} from a {@link Reader}.
   *
   * @param reader The {@link Reader}
   * @return The loaded {@link Configuration}
   * @throws IOException When something went wrong
   */
  @Nonnull
  Configuration load(@Nonnull Reader reader) throws IOException;
}