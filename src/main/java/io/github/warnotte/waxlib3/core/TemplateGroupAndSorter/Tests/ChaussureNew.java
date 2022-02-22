package io.github.warnotte.waxlib3.core.TemplateGroupAndSorter.Tests;

public class ChaussureNew {

	String marque = "NIKE";
	String modele = "AIR_MAX";
	int qt = 1;
	Couleur couleur = Couleur.BROUGE;
	int pointure = 40;
	double prix = 12.99f;

	public ChaussureNew(String marque, String modele, int qt, Couleur couleur, int pointure, double prix) {
		super();
		this.marque = marque;
		this.modele = modele;
		this.qt = qt;
		this.couleur = couleur;
		this.pointure = pointure;
		this.prix = prix;
	}

	public String getMarque() {
		return marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}

	public String getModele() {
		return modele;
	}

	public void setModele(String modele) {
		this.modele = modele;
	}

	public int getQt() {
		return qt;
	}

	public void setQt(int qt) {
		this.qt = qt;
	}

	public Couleur getCouleur() {
		return couleur;
	}

	public void setCouleur(Couleur couleur) {
		this.couleur = couleur;
	}

	public int getPointure() {
		return pointure;
	}

	public void setPointure(int pointure) {
		this.pointure = pointure;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	@Override
	public String toString() {
		return String.format("%10s;%s;%d,%f,%10s,%d",marque , modele, pointure, prix, couleur, qt    );
	}

}
