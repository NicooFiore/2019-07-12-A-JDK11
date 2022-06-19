package it.polito.tdp.food.model;

public class Postazione {
	int id;
	boolean occupato;
	public Postazione(int id, boolean occupato) {
		super();
		this.id = id;
		this.occupato = occupato;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isOccupato() {
		return occupato;
	}
	public void setOccupato(boolean occupato) {
		this.occupato = occupato;
	}
	
	

}
