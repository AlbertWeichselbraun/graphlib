package net.semanticlab.lib.graph.pagerank;

import java.util.function.Function;

public enum PageRankInitValue {
	
	// @formatter:off
	ZERO( n -> 0.0),
	ONE ( n -> 1.0),
	FN  ( n -> 1/n),
	FN2 ( n -> 1/(n*n));
	// @formatter:on

	final Function<Double, Double> initFunction;
	
	private PageRankInitValue(Function<Double, Double> initFunction) {
		this.initFunction = initFunction;
	}
	
	public double getValue(int networkSize) {
		return initFunction.apply((double)networkSize);
	}
}
