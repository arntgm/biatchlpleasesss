package mst;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Graph{
	
	
	public static class Vertex<T extends Comparable<T>> implements Comparable<Vertex<T>>{
		private int[] coords;
		private Color color;
		private T value;
		private List<Edge<T>> edges = new ArrayList<Edge<T>>();
//		private int value;
		
		public int[] getCoords() {
			return coords;
		}

		public void setCoords(int[] coords) {
			this.coords = coords;
		}
		
		public void addEdge(Edge<T> edge){
			this.edges.add(edge);
		}
		
		public List<Edge<T>> getEdges(){
			return this.edges;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public Vertex(T val, int x, int y, Color c){
			this.coords = new int[2];
			this.coords[0] = x;
			this.coords[1] = y;
			this.color = c;
			this.value = val;
		}
		
		@Override
        public int hashCode() {
            //final int code = this.value.hashCode() + this.edges.size();
//            return 31 * code;
			return Objects.hash(this.value, this.color, this.coords);
		}

		@Override
		public int compareTo(Vertex<T> o) {
	            final int valueComp = this.value.compareTo(o.value);
	            if (valueComp != 0)
	                return valueComp;
			return 0;
		}

		public T getValue() {
			// TODO Auto-generated method stub
			return this.value;
		}
	}
	
	public static class Edge<T extends Comparable<T>> implements Comparable<Edge<T>> {
		private Vertex<T> fromVertex;
		private Vertex<T> toVertex;
		private double cost;

		
		public Edge(double c,Vertex<T> from, Vertex<T> to){
			this.fromVertex = from;
			this.toVertex = to;
			this.cost = c;
		}

		public Vertex<T> getFromVertex() {
			return fromVertex;
		}

		public void setFromVertex(Vertex<T> fromVertex) {
			this.fromVertex = fromVertex;
		}

		public Vertex<T> getToVertex() {
			return toVertex;
		}

		public void setToVertex(Vertex<T> toVertex) {
			this.toVertex = toVertex;
		}

		public double getCost() {
			return cost;
		}

		public void setCost(double cost) {
			this.cost = cost;
		}

		@Override
		public int compareTo(Edge<T> e) {
			if (this.cost < e.cost)
                return -1;
            if (this.cost > e.cost)
                return 1;
            return 0;
		}
		
		@Override
        public int hashCode() {
            //final int code = this.value.hashCode() + this.edges.size();
//            return 31 * code;
			return Objects.hash(this.fromVertex, this.toVertex, this.cost);
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.fromVertex.getValue()+" -> "+this.toVertex.getValue()+" | "+this.cost;
		}
		
		
		
	}
	
	
}