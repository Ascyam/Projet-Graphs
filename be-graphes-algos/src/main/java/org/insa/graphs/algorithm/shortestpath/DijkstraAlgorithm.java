package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import org.insa.graphs.algorithm.utils.BinaryHeap;
import java.util.HashMap;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public class Label implements Comparable<Label> {
        Node sommetCourant;
        boolean marque;
        double cout;
        Arc pere;

        public Label(Node sommetCourant, double cout, Arc pere) {
            this.sommetCourant = sommetCourant;
            this.marque = false;
            this.cout = cout;
            this.pere = pere;
        }

        public Node getSommet() {
            return this.sommetCourant;
        }

        public void setSommet(Node sommetCourant){
            this.sommetCourant = sommetCourant;
        }

        public double getCost(){
            return this.cout;
        }

        public void setCost(double cout){
            this.cout = cout;
        }

        public Arc getPere() {
            return this.pere;
        }

        public void setPere(Arc pere){
            this.pere = pere;
        }

        public boolean getMarque() {
            return this.marque;
        }

        public void setMarque(){
            marque = true;
        }

        public int compareTo(Label other){
            return Double.compare(this.getTotalCost(), other.getTotalCost());
        }
        
        public double getTotalCost() {
        	return this.getCost();
        }

    }



    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        
    }
    public void InitLabels(Graph graph, HashMap<Node, Label> map, Node destination) {
    	//debut (mettre dans une fonction)
// commencer ici
        for (Node node : graph.getNodes()){
            map.put(node, new Label(node,Float.MAX_VALUE,null));
        }
        //fin    	
    }

    @Override
    protected ShortestPathSolution doRun() {

        final ShortestPathData data = getInputData();

        Node origine = data.getOrigin();
        Node destination = data.getDestination();
        Graph graph = data.getGraph();

        notifyOriginProcessed(origine);

        if (origine==destination) return new ShortestPathSolution (data, Status.OPTIMAL, new Path(graph, origine));
        
        BinaryHeap<Label> labels = new BinaryHeap<Label>();
        HashMap<Node, Label> map = new HashMap<Node, Label>();
        InitLabels(graph, map, destination);
        
        /*
        for (Node node : graph.getNodes()){
            map.put(node, new Label(node,Float.MAX_VALUE,null));
        } */
        //fin

        Label min = map.get(origine);
        min.setCost(0);
        labels.insert(min);
        Node nodeMin;

        ShortestPathSolution solution = null;
        // TODO:
        while(true){
        	System.out.println("Test");
            min =labels.deleteMin();
            nodeMin = min.getSommet();
            min.setMarque();
            notifyNodeMarked(nodeMin);
            if (nodeMin.equals(destination)) break;
            for(Arc arc : nodeMin.getSuccessors()){
                if(!data.isAllowed(arc)) continue;
                Node currentDestination = arc.getDestination();
                Label currentlabel = map.get(currentDestination);
                if(!currentlabel.getMarque()){
                    double cost = min.getCost() + data.getCost(arc);
                    if(currentlabel.getCost() > cost){
                        notifyNodeReached(currentDestination);
                        if(currentlabel.getCost() != Float.MAX_VALUE) {labels.remove(currentlabel);}
                        currentlabel.setCost(cost);
                        labels.insert(currentlabel);
                        currentlabel.setPere(arc);
                    }
                } 
            }

            if (labels.isEmpty()) return new ShortestPathSolution (data, Status.INFEASIBLE);

        }
        notifyDestinationReached(destination);
        ArrayList<Arc> shortestpath = new ArrayList<Arc>();
        //shortestpath.add();
        Arc pere;
        /*while((pere = shortestpath.get(shortestpath.get(shortestpath.size()-1)).getPere()) != origine){
            shortestpath.add(pere);
        } */
        Node currentNode = destination;
        while(currentNode != origine) {
        	Label labelcourant = map.get(currentNode);
        	shortestpath.add(labelcourant.getPere());
        	currentNode = shortestpath.get(shortestpath.size()-1).getOrigin();
        }
        //shortestpath.add(origine);
        Collections.reverse(shortestpath);
        //Path pluscourtchemin = Path.createShortestPathFromNodes(graph, shortestpath);
        //solution = new ShortestPathSolution(data, Status.OPTIMAL, pluscourtchemin);
        solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, shortestpath));




        return solution;
    }

}
