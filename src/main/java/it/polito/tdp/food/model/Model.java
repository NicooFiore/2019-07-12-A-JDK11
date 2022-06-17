package it.polito.tdp.food.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	private Graph<Food,DefaultWeightedEdge> grafo;
	private List<Food> cibi;
	private Map<Integer,Food> idMap;
	FoodDao dao;
	public Model() {
		this.cibi =new LinkedList<>();
		dao=new FoodDao();
		idMap=new HashMap<>();
	}
	public List<Food> listaCibi(int max){
		dao.creaIdMap(max, idMap);
		cibi.addAll(idMap.values());
		 return cibi;
	}
	public void creaGrafo(int max) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, this.listaCibi(max));
		System.out.println(grafo.vertexSet().size());
		for(Coppia c: dao.getAllCoppie(max, idMap)) {
			Graphs.addEdgeWithVertices(grafo,c.getF1(), c.getF2(), c.getPeso());
		}
		System.out.println(grafo.edgeSet().size());
	}
}
