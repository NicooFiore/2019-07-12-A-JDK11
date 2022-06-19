package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;
import it.polito.tdp.food.model.Evento.EventType;

public class Model {
	private Graph<Food,DefaultWeightedEdge> grafo;
	private List<Food> cibi;
	private Map<Integer,Food> idMap;
	FoodDao dao;
	public Model() {
		
		dao=new FoodDao();
		idMap=new HashMap<>();
	}
	public List<Food> listaCibi(int max){
		this.cibi =new LinkedList<>();
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
	List<ViciniCalorie> vicini;
	public String calorie(Food f) {
		vicini=new ArrayList<>();
		for(Food ff:Graphs.neighborListOf(grafo, f)) {
			DefaultWeightedEdge e=grafo.getEdge(ff, f);
			double peso=grafo.getEdgeWeight(e);
			vicini.add(new ViciniCalorie(ff,peso));
		}
		Collections.sort(vicini);
		Collections.reverse(vicini);
		String res="";
		for(int i=0;i<5 && i<Graphs.neighborListOf(grafo, f).size();i++) {
		res+=vicini.get(i).getVicino().getDisplay_name()+" : "+vicini.get(i).getCalorie()+"\n";	
		}
		return res;
	}
	public List<ViciniCalorie> listaVicini(Food f){
		List<ViciniCalorie> vv=new LinkedList<>();
		this.calorie(f);
		Collections.sort(vicini);
		Collections.reverse(vicini);
		for(ViciniCalorie vc: vicini) {
			if(!cibiPreparati.contains(vc.getVicino()))
				vv.add(vc);
		}
		return vv;
	}
	private Food food;
	private int K;
	private List<Postazione> postazioni;
	private List<Food> cibiPreparati;
	private PriorityQueue<Evento> coda;
	double t;
	int num;
	public void init(int K,Food food) {
		this.food=food;
		this.K=K;
		postazioni=new ArrayList<>();
		cibiPreparati=new ArrayList<>();
		coda=new PriorityQueue<>();
		t=0;
		num=0;
		for(int i=1;i<=K;i++) {
			postazioni.add(new Postazione(i,false));
		}
		Evento e=new Evento(EventType.FINE,0, postazioni.get(0), food);
		cibiPreparati.add(food);
		coda.add(e);
		postazioni.get(0).setOccupato(true);
		for(ViciniCalorie f : this.listaVicini(food)) {
			for(Postazione p:postazioni) {
				if(p.isOccupato()==false) {
					p.setOccupato(true);
					Evento ee= new Evento(EventType.FINE,f.getCalorie(),p,f.getVicino());
					cibiPreparati.add(f.getVicino());
					coda.add(ee);
					break;
				}
					
			}
		}
	}
	public void simula() {
		while(!coda.isEmpty()) {
			Evento e=coda.poll();
			switch(e.getType()) {
			case INIZIO:
				for(ViciniCalorie f : this.listaVicini(e.getF())) {
							Evento ee= new Evento(EventType.FINE,e.getTime()+f.getCalorie(),e.getP(),f.getVicino());
							cibiPreparati.add(f.getVicino());
							coda.add(ee);
							break;
				}
				break;
			case FINE:
				Evento ee=new Evento(EventType.INIZIO,e.getTime(),e.getP(),e.getF());
				coda.add(ee);
				e.getP().setOccupato(false);
				break;
			}
			t=e.getTime();
			num=cibiPreparati.size();
		}
	}
	public double getT() {
		return t;
	}
	public int getNum() {
		return num;
	}
	
}
