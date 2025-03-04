package io.github.warnotte.waxlib3.core.Identifiable;

/**
 * Classe derivable pour certains systeme automatique (obj2gui2 par exemple ou les MINIDB). 
 * @author Renaud Warnotte
 *
 */
public interface Identifiable {
	public int getId();
	public void setId(int id);
}

