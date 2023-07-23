package me.phoenixra.atumodcore.json.jsonpath.internal.filter;

import me.phoenixra.atumodcore.json.jsonpath.Predicate;

public interface Evaluator {
    boolean evaluate(ValueNode left, ValueNode right, Predicate.PredicateContext ctx);
}