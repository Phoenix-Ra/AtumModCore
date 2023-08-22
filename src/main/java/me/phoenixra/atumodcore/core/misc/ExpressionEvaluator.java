package me.phoenixra.atumodcore.core.misc;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.misc.crunch.Crunch;
import me.phoenixra.atumodcore.api.misc.crunch.functional.EvaluationEnvironment;
import me.phoenixra.atumodcore.api.misc.crunch.functional.Function;
import me.phoenixra.atumodcore.api.placeholders.PlaceholderManager;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;

public class ExpressionEvaluator {
    private EvaluationEnvironment environment = new EvaluationEnvironment();
    public ExpressionEvaluator() {
        environment.addFunction(
            new Function(
                "min",
                2,
                    it -> Math.min(it[0], it[1])
            )
        );
    }
    public double evaluate(AtumMod atumMod, String expression, PlaceholderContext context) {
        return Crunch.compileExpression(PlaceholderManager.translatePlaceholders(atumMod,expression,context), environment).evaluate();
    }
}
