package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private Map<String, String> idMap;
	
	public Model() {
		dao = new FoodDao();
		
	}
	
	public List<String> getTypePortions(){
		return dao.listAllTypePortions();
	}
	
	public void creaGrafo(double c) {
		idMap = new TreeMap<>();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		dao.getVertici(idMap, c);
		Graphs.addAllVertices(grafo, idMap.values());
		
		List<Adiacenza> res = dao.getArchi(idMap);
		for(Adiacenza a: res ) {
			Graphs.addEdge(grafo, a.getP1(), a.getP2(), a.getPeso());
		}
	}
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public Set<String> getVertici(){
		if(grafo != null) {
			return grafo.vertexSet();
		}
		
		return null;
		
	}
	
	public List<Adiacenza> getCorrelate(String vertice){
		List<Adiacenza> result = new ArrayList<>();
		
		for(DefaultWeightedEdge de: grafo.edgesOf(vertice)){
			Adiacenza a = new Adiacenza(vertice, Graphs.getOppositeVertex(grafo, de, vertice), grafo.getEdgeWeight(de));
			result.add(a);
		}
		
		return result;
	}
	
	private List<String> camminoMinimo;
	private double pesoMassimo = 0;
	
	public List<String> trovaCammino(String primo, int N){
		
		camminoMinimo = new ArrayList<>();
		List<String> parziale = new ArrayList<>();
		parziale.add(primo);
		
		
		cerca(parziale, N, 0);
		
		return camminoMinimo;
	}
	
	private void cerca(List<String> parziale, int N, double sommaPesi) {
		if(parziale.size() == N) {
			
			if(sommaPesi > pesoMassimo) {
				camminoMinimo = new ArrayList<>(parziale);
				pesoMassimo = sommaPesi;
			}
			return;
		}
		
		for(DefaultWeightedEdge de: grafo.edgesOf(parziale.get(parziale.size()-1))) {
			
			String opposto = Graphs.getOppositeVertex(grafo, de, parziale.get(parziale.size()-1));
			
			if(!parziale.contains(opposto)) {
				parziale.add(opposto);
				sommaPesi += grafo.getEdgeWeight(de);
				
				cerca(parziale, N, sommaPesi);
				
				parziale.remove(opposto);
				sommaPesi -= grafo.getEdgeWeight(de);
			}
		}
	}
	
	public double getPesoMassimo() {
		return this.pesoMassimo;
	}
}
