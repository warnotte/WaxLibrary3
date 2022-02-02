package org.warnotte.waxlib3.waxlibswingcomponents.Utils.Curve;

public class Vca_Preset {

	float [] valeurs = {0f,1f};
	String named = "VCAPRESET_DEFAULT";
	public Vca_Preset(float[] valeurs, String named) {
		super();
		this.valeurs = valeurs;
		this.named = named;
	}
	public synchronized float[] getValeurs() {
		return valeurs;
	}
	public synchronized void setValeurs(float[] valeurs) {
		this.valeurs = valeurs;
	}
	public synchronized String getNamed() {
		return named;
	}
	public synchronized void setNamed(String named) {
		this.named = named;
	}
	
}
