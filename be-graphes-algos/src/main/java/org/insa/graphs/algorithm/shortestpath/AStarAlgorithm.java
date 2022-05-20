package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm.Label;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AStarAlgorithm extends DijkstraAlgorithm {


    public class LabelAStar extends Label{

        Node destination;
        double trajetDirect;
         

        LabelAStar(Node sommetCourant, double cout, Arc pere, Node destination) {
            super(sommetCourant, cout, pere);
            this.marque=false;
            this.destination = destination ;
            this.trajetDirect = Point.distance(this.sommetCourant.getPoint(), this.destination.getPoint()) ;
        }
    
        Node getDestination() {
            return this.destination ;
        }
    
        void setDestination(Node destination) {
            this.destination = destination ;
        }
        @Override
		public
        double getTotalCost(){
        	
            return this.getCost() + this.trajetDirect ;
        }
    
        double gettrajetDirect(){
            return this.trajetDirect ;
        }
    
        void settrajetDirect(double trajetDirect) {
            this.trajetDirect = trajetDirect;
        }
    
        /*@Override
        public int compareTo(Label other) {
            return Double.compare(this.getTotalCost(), ((LabelAStar)other).getTotalCost());
        }*/
        
    }

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

   
   /*protected List<Label> FastLabelList(){

    List<Label> labelList = new ArrayList<Label>();

    Node destination = getInputData().getDestination() ;
   }  */
   @Override
   public void InitLabels(Graph graph, HashMap<Node, Label> map, Node destination) {
	   for (Node node : graph.getNodes()){
           map.put(node, new LabelAStar(node,Float.MAX_VALUE,null, destination));
       }
	   
   }
   
   
}