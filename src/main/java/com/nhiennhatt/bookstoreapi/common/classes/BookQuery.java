package com.nhiennhatt.bookstoreapi.common.classes;

import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Selection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class BookQuery {
    private List<Selection<?>> selections;
    private List<Predicate> predicates;
    private List<Predicate> havingPredicates;
    private List<Expression<?>> groupByExpressions;
}
