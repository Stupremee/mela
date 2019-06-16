package com.github.stupremee.mela.query;

import com.github.stupremee.mela.query.criterias.BetweenCriteria;
import com.github.stupremee.mela.query.criterias.EqualsCriteria;
import com.github.stupremee.mela.query.criterias.GreaterThanCriteria;
import com.github.stupremee.mela.query.criterias.GreaterThanOrEqualCriteria;
import com.github.stupremee.mela.query.criterias.LessThanCriteria;
import com.github.stupremee.mela.query.criterias.LessThanOrEqualCriteria;
import com.github.stupremee.mela.query.criterias.LikeCriteria;
import com.github.stupremee.mela.query.criterias.NotEqualsCriteria;
import com.github.stupremee.mela.query.criterias.RegExCriteria;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

/**
 * https://github.com/Stupremee
 *
 * @author Stu
 * @since 10.06.19
 */
@SuppressWarnings("unused")
public final class Query {

  private final String key;
  private final List<Criteria> criteria;

  private Query(String key, List<Criteria> criteria) {
    this.key = key;
    this.criteria = criteria;
  }

  /**
   * Adds another {@link Criteria} to this {@link Query}.
   *
   * @param key The key of the new {@link Criteria}
   * @return The {@link CriteriaFactory} to create the {@link Criteria}
   */
  public CriteriaFactory and(String key) {
    Preconditions.checkNotNull(key, "key can't be null.");
    return new CriteriaFactory(this);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key, criteria);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }

    if (o == this) {
      return true;
    }

    if (!(o instanceof Query)) {
      return false;
    }

    var other = (Query) o;
    return Objects.equal(key, other.key)
        && Objects.equal(criteria, other.criteria);
  }

  @Override
  public String toString() {
    return criteria.toString();
  }

  /**
   * Creates a new {@link Query} with the given key as key.
   *
   * @param key The key
   * @return The {@link Query}
   */
  public static CriteriaFactory where(String key) {
    Preconditions.checkNotNull(key, "key can't be null.");

    Query query = new Query(key, new ArrayList<>());
    return new CriteriaFactory(query);
  }

  /**
   * This class is used to create {@link Criteria} objects and automatically add them to the given
   * {@link Query}.
   */
  public static final class CriteriaFactory {

    private final Query query;

    private CriteriaFactory(Query query) {
      this.query = query;
    }

    /**
     * Adds a {@link EqualsCriteria} to the {@link Query}.
     */
    @Nonnull
    public Query equalTo(Object value) {
      Preconditions.checkNotNull(value, "value can't be null.");
      query.criteria.add(EqualsCriteria.create(query.key, value));
      return query;
    }

    /**
     * Adds a {@link NotEqualsCriteria} to the {@link Query}.
     */
    @Nonnull
    public Query notEqualTo(Object value) {
      Preconditions.checkNotNull(value, "value can't be null.");
      query.criteria.add(NotEqualsCriteria.create(query.key, value));
      return query;
    }

    /**
     * Adds a {@link LessThanCriteria} to the {@link Query}.
     */
    @Nonnull
    public <T extends Comparable<T>> Query lessThan(T upperBound) {
      Preconditions.checkNotNull(upperBound, "value can't be null.");
      query.criteria.add(LessThanCriteria.create(query.key, upperBound));
      return query;
    }

    /**
     * Adds a {@link LessThanOrEqualCriteria} to the {@link Query}.
     */
    @Nonnull
    public <T extends Comparable<T>> Query lessThanOrEqual(T upperBound) {
      Preconditions.checkNotNull(upperBound, "value can't be null.");
      query.criteria.add(LessThanOrEqualCriteria.create(query.key, upperBound));
      return query;
    }

    /**
     * Adds a {@link GreaterThanCriteria} to the {@link Query}.
     */
    @Nonnull
    public <T extends Comparable<T>> Query greaterThan(T lowerBound) {
      Preconditions.checkNotNull(lowerBound, "lowerBound can't be null.");
      query.criteria.add(GreaterThanCriteria.create(query.key, lowerBound));
      return query;
    }

    /**
     * Adds a {@link GreaterThanOrEqualCriteria} to the {@link Query}.
     */
    @Nonnull
    public <T extends Comparable<T>> Query greaterThanOrEqual(T lowerBound) {
      Preconditions.checkNotNull(lowerBound, "value can't be null.");
      query.criteria.add(GreaterThanOrEqualCriteria.create(query.key, lowerBound));
      return query;
    }

    /**
     * Adds a {@link BetweenCriteria} to the {@link Query}. Warning: This only works for some
     * databases.
     */
    @Nonnull
    public <T extends Comparable<T>> Query between(T lowerBound, T upperBound) {
      Preconditions.checkNotNull(lowerBound, "lowerBound can't be null.");
      Preconditions.checkNotNull(upperBound, "upperBound can't be null.");
      query.criteria.add(BetweenCriteria.create(query.key, lowerBound, upperBound));
      return query;
    }

    /**
     * Adds a {@link BetweenCriteria} to the {@link Query}. Warning: This only works for some
     * databases.
     */
    @Nonnull
    public Query regex(Pattern pattern) {
      Preconditions.checkNotNull(pattern, "pattern can't be null.");
      query.criteria.add(RegExCriteria.create(query.key, pattern));
      return query;
    }

    /**
     * Adds a {@link BetweenCriteria} to the {@link Query}. Warning: This only works for some
     * databases.
     */
    @Nonnull
    public Query like(String pattern) {
      Preconditions.checkNotNull(pattern, "pattern can't be null.");
      query.criteria.add(LikeCriteria.create(query.key, pattern));
      return query;
    }
  }
}
