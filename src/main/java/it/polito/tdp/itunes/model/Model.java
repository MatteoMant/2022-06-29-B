package it.polito.tdp.itunes.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private ItunesDAO dao;
	private Graph<Album, DefaultWeightedEdge> grafo;
	private Map<Album, Double> albumDurata;
	
	public Model() {
		dao = new ItunesDAO();
		albumDurata = new HashMap<>();
	}
	
	public void creaGrafo(int secondi) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiunta dei vertici
		Graphs.addAllVertices(this.grafo, dao.getAllAlbumsWithSeconds(secondi));
		
		// Aggiunta degli archi
		for (Album a : this.grafo.vertexSet()) {
			double durata = dao.getDurataAlbum(a);
			albumDurata.put(a, durata);
		}
		
		for (Album a1 : this.grafo.vertexSet()) {
			for (Album a2 : this.grafo.vertexSet()) {
				if ((albumDurata.get(a1) != albumDurata.get(a2)) 
						&& (albumDurata.get(a1) + albumDurata.get(a2)) > 4*secondi) {
					if (this.grafo.getEdge(a1, a2) == null) {					
						if (albumDurata.get(a1) > albumDurata.get(a2)) {
							Graphs.addEdge(this.grafo, a2, a1, albumDurata.get(a1) + albumDurata.get(a2));
						} else if (albumDurata.get(a1) < albumDurata.get(a2)) {
							Graphs.addEdge(this.grafo, a1, a2, albumDurata.get(a1) + albumDurata.get(a2));
						}
					} else {
						// non faccio nulla perchè l'arco esiste già
					}
				}
			}
		}
	}
	
	public List<AlbumAndBilancio> getAllSuccessori(Album a1){
		List<AlbumAndBilancio> result = new LinkedList<>();
		
		for (Album a : Graphs.successorListOf(this.grafo, a1)) {
			double bilancio = calcolaBilancio(a);
			result.add(new AlbumAndBilancio(a, bilancio));
		} 
		
		if (result != null)
			Collections.sort(result);
		return result;
	}
	
	public int calcolaBilancio(Album a) {
		int bilancio = 0;
		
		int sommaEntranti = 0;
		for (DefaultWeightedEdge edge : this.grafo.incomingEdgesOf(a)) {
			sommaEntranti += this.grafo.getEdgeWeight(edge);
		}
		
		int sommaUscenti = 0;
		for (DefaultWeightedEdge edge : this.grafo.outgoingEdgesOf(a)) {
			sommaUscenti += this.grafo.getEdgeWeight(edge);
		}
		
		bilancio = sommaEntranti - sommaUscenti;
		return bilancio;
	}
	 
	public List<Album> getAllAlbums(){
		List<Album> albums = new LinkedList<>(this.grafo.vertexSet());
		Collections.sort(albums);
		return albums;
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
}
