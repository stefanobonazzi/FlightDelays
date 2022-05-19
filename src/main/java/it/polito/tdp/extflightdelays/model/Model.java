package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Graph<Airport, DefaultEdge> graph;
	private Map<Integer, Airport> idMap;
	private ExtFlightDelaysDAO dao;
	
	public Model() {
		this.idMap = new HashMap<Integer, Airport>();
		this.dao = new ExtFlightDelaysDAO();
		this.dao.loadAllAirports(this.idMap);
	}
	
	public void creaGrafo(int x) {
		this.graph = new SimpleWeightedGraph<Airport, DefaultEdge>(DefaultEdge.class);
		
		//Inserisco i vertici
		Graphs.addAllVertices(this.graph, this.dao.getVertici(x, idMap));
		
		//Inserisco gli archi
		for(Rotta r: dao.getRotte(idMap)) {
			if(this.graph.containsVertex(r.getA1()) && this.graph.containsVertex(r.getA2())) {
				DefaultEdge edge = this.graph.getEdge(r.getA1(), r.getA2());
				if(edge == null) {
					Graphs.addEdge(this.graph, r.getA1(), r.getA2(), r.getnVoli());
				} else {
					int pesoVecchio = (int) this.graph.getEdgeWeight(edge);
					int pesoNuovo = pesoVecchio + r.getnVoli();
					
					this.graph.setEdgeWeight(edge, pesoNuovo);
				}
			}
		}
		
		System.out.println("#Vertici = "+this.graph.vertexSet().size());
		System.out.println("#Archi = "+this.graph.edgeSet().size());
	}
	
	public List<Airport> getAirports() {
		List<Airport> l = new LinkedList<>(this.idMap.values());
		Collections.sort(l);
		
		return l;
	}
}
