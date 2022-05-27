package org.insa.graphs.algorithm.shortestpath;

public class DijkstraTest extends ShortestPathTest {
	@Override
	public ShortestPathAlgorithm setAlgorithm() {
		return new DijkstraAlgorithm(null);
	}	
	
}
