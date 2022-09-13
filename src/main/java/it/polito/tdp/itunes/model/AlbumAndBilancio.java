package it.polito.tdp.itunes.model;

public class AlbumAndBilancio implements Comparable<AlbumAndBilancio> {
	
	private Album album;
	private Double bilancio;
	
	public AlbumAndBilancio(Album album, Double bilancio) {
		super();
		this.album = album;
		this.bilancio = bilancio;
	}
	
	public Album getAlbum() {
		return album;
	}
	
	public void setAlbum(Album album) {
		this.album = album;
	}
	
	public Double getBilancio() {
		return bilancio;
	}
	
	public void setBilancio(Double bilancio) {
		this.bilancio = bilancio;
	}

	@Override
	public int compareTo(AlbumAndBilancio other) {
		return other.getBilancio().compareTo(this.getBilancio());
	}
	
	
}

