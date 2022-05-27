package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.AlgorithmFactory;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
import org.insa.graphs.model.Arc;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;



public abstract class ShortestPathTest {
	public abstract ShortestPathAlgorithm setAlgorithm();
	
	private Graph graph;
	private Node origin, destination;
	private ShortestPathAlgorithm algo1;
	private ShortestPathAlgorithm algo3;
	private ShortestPathData data;
	private Path insaBikiniPath, insaAeroportPath, insaAeroportTimePath;
	private ShortestPathSolution insaBikini, insaAeroport, nullSolution, onePoint, insaAeroportTime;
	private ShortestPathSolution dijkstraSolution, aStarSolution, bellmanFordSolution;
	private ArcInspector arcAllowed;
	
	//Recupération de la solution path pour comparaison lors des tests
	
	public Path getPath(Graph graph, String adressePath) throws IOException {
		Path path;
		//va lire le fichier
		PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(adressePath))));
		path = pathReader.readPath(graph);
		return path;
	}
	
	//Calcul de notre solution
	
	public ShortestPathSolution getShortestPathSolution(ShortestPathAlgorithm algo, Graph graph, Node origin, Node destination, ArcInspector arcAllowed) throws Exception {
		ShortestPathSolution shortestPathSolution=null;
		// Init de ShortestPathData data pour appel de l'algo, recuperation de la carte, point de depart, arrivee, contraintes sur le chemin
		data = new ShortestPathData(graph, origin, destination, arcAllowed);
		
		//Instanciation algorithme
		if(algo instanceof AStarAlgorithm) {this.algo1 = (ShortestPathAlgorithm)AlgorithmFactory.createAlgorithm(AStarAlgorithm.class, data);}
		else if(algo instanceof DijkstraAlgorithm) {this.algo1 = (ShortestPathAlgorithm)AlgorithmFactory.createAlgorithm(DijkstraAlgorithm.class, data);}
		else if(algo instanceof BellmanFordAlgorithm) {this.algo1 = (ShortestPathAlgorithm)AlgorithmFactory.createAlgorithm(BellmanFordAlgorithm.class, data);}
		//Execution algo
		shortestPathSolution = this.algo1.doRun();
		return shortestPathSolution;
	}
	
	@Before
	public void initTest() throws Exception {
		//init algo
		this.algo1= setAlgorithm();
		
		String mapPath = "/home/brulez/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/haute-garonne.mapgr";
		
		//Chargement carte
		final GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapPath))));
		graph = reader.read();
		
		//Test pas de chemin 
		//init origin 90833
		origin = graph.get(90833);
		//init destination 70835
		destination = graph.get(70835);
		//Pas de restrictions sur les arcs
		arcAllowed = ArcInspectorFactory.getAllFilters().get(0);
		
		nullSolution = getShortestPathSolution(this.algo1, graph, origin, destination, arcAllowed);
		
		//Test INSA-Bikini
		//chargement du chemin
		String cheminPath = "/home/brulez/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31_insa_bikini_canal.path";
		insaBikiniPath = getPath(graph, cheminPath);
		//origin = 10991, destination 63104
		origin = graph.get(10991);
		destination = graph.get(63104);
		//Pas de restrictions sur les arcs
		arcAllowed = ArcInspectorFactory.getAllFilters().get(0);
		insaBikini = getShortestPathSolution(this.algo1, graph, origin, destination, arcAllowed);
		
		//Test Depart=Arrivee
		origin=graph.get(108254);
		destination=graph.get(108254);
		arcAllowed=ArcInspectorFactory.getAllFilters().get(0);
		onePoint = getShortestPathSolution(this.algo1, graph, origin, destination, arcAllowed);
	
	
		//Test INSA-Aeroport
		cheminPath = "/home/brulez/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31_insa_aeroport_length.path";
		insaAeroportPath = getPath(graph, cheminPath);
		origin=graph.get(10991);
		destination = graph.get(89149);
		arcAllowed = ArcInspectorFactory.getAllFilters().get(1);
		insaAeroport = getShortestPathSolution(this.algo1, graph, origin, destination, arcAllowed);
		
		
		
		//Test insa Aeroport Time (chemin plus rapide)
		cheminPath = "/home/brulez/Bureau/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31_insa_aeroport_time.path";
		insaAeroportTimePath = getPath(graph, cheminPath);
		origin = graph.get(10991);
		destination = graph.get(89149);
		arcAllowed = ArcInspectorFactory.getAllFilters().get(3);
		insaAeroportTime = getShortestPathSolution(this.algo1, graph, origin, destination, arcAllowed);
		
		this.algo3 = new BellmanFordAlgorithm(null);
		
		//Test Chemin long quelconque 1
		origin = graph.get(104212);
		destination = graph.get(105980);
		arcAllowed = ArcInspectorFactory.getAllFilters().get(0);
		//Dijkstra ou Astar
		dijkstraSolution = getShortestPathSolution(this.algo1, graph, origin, destination, arcAllowed);
		//BellmanFord
		bellmanFordSolution = getShortestPathSolution(this.algo3, graph, origin, destination, arcAllowed);
		
	}
		
	

	
	@Test
	public void testInsaBikiniPath() {
		Assume.assumeNotNull(this.algo1, insaBikini);
		assertTrue(insaBikini.getStatus() == Status.OPTIMAL);
		assertEquals(insaBikiniPath.getArcs(), insaBikini.getPath().getArcs());
	}
	
	@Test
	public void testNullSolution() {
		Assume.assumeNotNull(this.algo1, nullSolution);
		assertTrue(nullSolution.getStatus() == Status.INFEASIBLE);
	}
	
	@Test
	public void departEgalArriveeSolution() {
		List<Arc> Emptylist = Collections.<Arc>emptyList();
		Assume.assumeNotNull(this.algo1, onePoint);
		assertTrue(onePoint.getStatus() == Status.OPTIMAL);
		assertEquals(onePoint.getPath().getArcs(),Emptylist);
	}
	
	@Test
	public void testInsaAeroport() {
		Assume.assumeNotNull(this.algo1, insaAeroport);
		assertTrue(insaAeroport.getStatus() == Status.OPTIMAL);
		assertEquals(insaAeroport.getPath().getArcs(), insaAeroportPath.getArcs());
	}
	
	@Test
	public void testQuelconque1() {
		Assume.assumeNotNull(this.algo1, dijkstraSolution);
		Assume.assumeNotNull(this.algo3, bellmanFordSolution);
		assertTrue(dijkstraSolution.getStatus() == Status.OPTIMAL);
		assertTrue(bellmanFordSolution.getStatus() == Status.OPTIMAL);
		assertEquals(bellmanFordSolution.getPath().getArcs(), dijkstraSolution.getPath().getArcs());
	}
	
	@Test
	public void testInsaAeroportTime() {
		Assume.assumeNotNull(this.algo1, insaAeroportTime);
		assertTrue(insaAeroportTime.getStatus() == Status.OPTIMAL);
		assertEquals(insaAeroportTime.getPath().getArcs(), insaAeroportTimePath.getArcs());
	}
		
		
	
	
	
	
}


