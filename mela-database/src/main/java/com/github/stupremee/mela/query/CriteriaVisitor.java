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

/**
 * @author Stu (https://github.com/Stupremee)
 * @since 10.06.19
 */
public interface CriteriaVisitor {

  void visitEquals(EqualsCriteria criteria);

  void visitNotEquals(NotEqualsCriteria criteria);

  void visitLessThan(LessThanCriteria criteria);

  void visitLessThanOrEqual(LessThanOrEqualCriteria criteria);

  void visitGreaterThan(GreaterThanCriteria criteria);

  void visitGreaterThanOrEqual(GreaterThanOrEqualCriteria criteria);

  void visitBetween(BetweenCriteria criteria);

  void visitRegEx(RegExCriteria criteria);

  void visitLike(LikeCriteria criteria);

}
