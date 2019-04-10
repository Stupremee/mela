package com.github.stupremee.mela.mongo.query;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * https://github.com/Stupremee
 *
 * @author Stu
 * @since 10.04.2019
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class Queries {

  private Queries() {
  }

  /**
   * Creates a new {@link Query} that matches documents where the {@literal _id} matches the given
   * value.
   *
   * @param value The value that field should have
   * @param <ValueT> The type of the value
   * @return The {@link Query}
   */
  @Nonnull
  public static <ValueT> Query eq(@Nullable ValueT value) {
    return eq("_id", value);
  }

  /**
   * Creates a {@link Query} that matches documents where the value of the given field equals the
   * given value.
   *
   * @param field The name of the field as a {@link String}
   * @param value The value that field should have
   * @param <ValueT> The type of the value
   * @return The {@link Query}
   */
  @Nonnull
  public static <ValueT> Query eq(@Nonnull String field, @Nullable ValueT value) {
    Preconditions.checkNotNull(field, "field can't be null.");
    return new KeyValueQuery<>(field, value);
  }

  /**
   * Creates a {@link Query} that matches all documents where the value of a field is not equal to
   * the given value.
   *
   * @param field The field name
   * @param value The value
   * @param <ValueT> The value type
   * @return The {@link Query}
   */
  @Nonnull
  public static <ValueT> Query ne(@Nonnull String field, @Nullable ValueT value) {
    Preconditions.checkNotNull(field, "field can't be null.");
    return new OperatorQuery<>("$ne", field, value);
  }

  /**
   * Creates a {@link Query} that matches all documents where the value of the given field is
   * greater than the given value.
   *
   * @param field The field name
   * @param value The value
   * @param <ValueT> The value type
   * @return The filter
   */
  @Nonnull
  public static <ValueT> Query gt(@Nonnull String field, @Nullable ValueT value) {
    Preconditions.checkNotNull(field, "field can't be null.");
    return new OperatorQuery<>("$gt", field, value);
  }

  /**
   * Creates a {@link Query} that matches all documents where the value of the given field is
   * greater than or equal to the given value.
   *
   * @param field The field name
   * @param value The value
   * @param <ValueT> The value type
   * @return The filter
   */
  @Nonnull
  public static <ValueT> Query gte(@Nonnull String field, @Nullable ValueT value) {
    Preconditions.checkNotNull(field, "field can't be null.");
    return new OperatorQuery<>("$gte", field, value);
  }

  /**
   * Creates a {@link Query} that matches all documents where the value of a field equals any value
   * in the list of specified values.
   *
   * @param field The field name
   * @param values The list of values
   * @param <ValueT> The value type
   * @return The {@link Query}
   */
  @Nonnull
  @SafeVarargs
  public static <ValueT> Query in(@Nonnull String field, ValueT... values) {
    Preconditions.checkNotNull(field, "false can't be null.");
    return in(field, Arrays.asList(values));
  }

  /**
   * Creates a {@link Query} that matches all documents where the value of a field equals any value
   * in the list of specified values.
   *
   * @param field The field name
   * @param values The list of values
   * @param <ValueT> The value type
   * @return The {@link Query}
   */
  @Nonnull
  public static <ValueT> Query in(@Nonnull String field, @Nonnull Iterable<ValueT> values) {
    Preconditions.checkNotNull(field, "field can't be null.");
    Preconditions.checkNotNull(values, "values can't be null.");
    return new IterableOperatorQuery<>("$in", field, values);
  }

  /**
   * Creates a {@link Query} that matches all documents where the value of a field does not equal
   * any of the specified values or does not exist.
   *
   * @param field The field name
   * @param values The list of values
   * @param <ValueT> The value type
   * @return The {@link Query}
   */
  @Nonnull
  @SafeVarargs
  public static <ValueT> Query nin(@Nonnull String field, ValueT... values) {
    Preconditions.checkNotNull(field, "field can't be null.");
    return nin(field, Arrays.asList(values));
  }

  /**
   * Creates a {@link Query} that matches all documents where the value of a field does not equal
   * any of the specified values or does not exist.
   *
   * @param field The field name
   * @param values The list of values
   * @param <ValueT> The value type
   * @return The {@link Query}
   */
  @Nonnull
  public static <ValueT> Query nin(@Nonnull String field, @Nonnull Iterable<ValueT> values) {
    Preconditions.checkNotNull(field, "field can't be null.");
    Preconditions.checkNotNull(values, "values can't be null.");
    return new IterableOperatorQuery<>("$nin", field, values);
  }

  /**
   * Creates a {@link Query} that matches all documents where the value of a field is less than the
   * given value.
   *
   * @param field The field name
   * @param value The value
   * @param <ValueT> The value type
   * @return The {@link Query}
   */
  @Nonnull
  public static <ValueT> Query lt(@Nonnull String field, @Nullable ValueT value) {
    Preconditions.checkNotNull(field, "field can't be null.");
    return new OperatorQuery<>("$lt", field, value);
  }

  /**
   * Creates a {@link Query} that matches all documents where the value of a field is less than or
   * equal to the given value.
   *
   * @param field The field name
   * @param value The value
   * @param <ValueT> The value type
   * @return The {@link Query}
   */
  @Nonnull
  public static <ValueT> Query lte(@Nonnull String field, @Nullable ValueT value) {
    Preconditions.checkNotNull(field, "field can't be null.");
    return new OperatorQuery<>("$lte", field, value);
  }

  /**
   * Creates a {@link Query} that performs a logical AND of the provided list of queries.
   *
   * @param queries The list of {@link Query Queries}
   * @return The {@link Query}
   */
  @Nonnull
  public static Query and(@Nonnull Iterable<Query> queries) {
    Preconditions.checkNotNull(queries, "queries can't be null.");
    return new ConjunctionQuery(queries);
  }

  /**
   * Creates a {@link Query} that performs a logical AND of the provided list of queries.
   *
   * @param queries The list of {@link Query Queries}
   * @return The {@link Query}
   */
  @Nonnull
  public static Query and(@Nonnull Query... queries) {
    Preconditions.checkNotNull(queries, "queries can't be null.");
    return and(Arrays.asList(queries));
  }
}
