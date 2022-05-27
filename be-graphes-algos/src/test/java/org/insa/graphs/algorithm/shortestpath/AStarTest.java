package org.insa.graphs.algorithm.shortestpath;

public class AStarTest extends ShortestPathTest {
	@Override
	public ShortestPathAlgorithm setAlgorithm() {
		return new AStarAlgorithm(null);
	}

}
