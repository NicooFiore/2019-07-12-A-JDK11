package it.polito.tdp.food.model;

public class Evento implements Comparable<Evento> {
	public enum EventType{
		INIZIO,
		FINE,
	}
	EventType type;
	double time;
	Postazione p;
	Food f;
	public Evento(EventType type,double time, Postazione p, Food f) {
		this.type=type;
		this.time = time;
		this.p = p;
		this.f = f;
	}
	
	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public Postazione getP() {
		return p;
	}
	public void setP(Postazione p) {
		this.p = p;
	}
	public Food getF() {
		return f;
	}
	public void setF(Food f) {
		this.f = f;
	}
	@Override
	public int compareTo(Evento o) {
		if(this.time-o.getTime()<0)
		return -1;
		else return 1;
	}
	
 
}
