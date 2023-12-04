package me.phoenixra.atumodcore.api.misc.crunch;

import me.phoenixra.atumodcore.api.misc.crunch.token.TokenType;
import me.phoenixra.atumodcore.api.misc.crunch.token.Value;

public class Variable implements Value {
	
	private int index;
	protected CompiledExpression expression;
	
	public Variable(CompiledExpression expression, int index) {
		this.expression = expression;
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	@Override
	public double getValue() {
		return expression.variableValues[index];
	}
	
	@Override
	public TokenType getType() {
		return TokenType.VARIABLE;
	}
	
	public String toString() {
		return "$" + (index + 1);
	}
	
	public Variable getClone() {
		return new Variable(expression, index);
	}
	
}
