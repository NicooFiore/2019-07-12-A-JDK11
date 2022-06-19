package it.polito.tdp.food.model;

public class ViciniCalorie implements Comparable<ViciniCalorie> {
private Food vicino;
private double calorie;
public ViciniCalorie(Food vicino, double calorie) {
	super();
	this.vicino = vicino;
	this.calorie = calorie;
}
public Food getVicino() {
	return vicino;
}
public void setVicino(Food vicino) {
	this.vicino = vicino;
}
public double getCalorie() {
	return calorie;
}
public void setCalorie(double calorie) {
	this.calorie = calorie;
}
@Override
public int compareTo(ViciniCalorie o) {
	
	return (int)(this.getCalorie()-o.getCalorie());
}


}
