package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph <ArtObject, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		dao=new ArtsmiaDAO();
		idMap=new HashMap<Integer,ArtObject>();
	}
	
	public void creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo Vertici
		dao.listObjects(idMap);
		Graphs.addAllVertices(this.grafo, idMap.values());
		//aggiungo Archi
		
		for(Adiacenza a: this.dao.getAdiacenze(idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(),a.getA2(),a.getPeso());
		}
		System.out.println("Grafo creato");
		System.out.println("VERTICI: "+this.grafo.vertexSet().size());
		System.out.println("ARCHI: "+this.grafo.edgeSet().size());
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public ArtObject getObject(int objectId) {
		return idMap.get(objectId);
	}

	
	public int getComponenteConnessa(ArtObject vertice) {
		Set <ArtObject> visitati= new HashSet<>();
		//A partire dal vertice, visito tutti i suoi vicini
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> it=
				new DepthFirstIterator<ArtObject, DefaultWeightedEdge>(this.grafo,vertice);
		while(it.hasNext())
			visitati.add(it.next());
		return visitati.size();
	}
	
	
	
}
