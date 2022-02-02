package io.github.warnotte.waxlib3.waxlib2.TemplateCollectionFilter.Tests;
import java.util.ArrayList;

import io.github.warnotte.waxlib3.waxlib2.TemplateCollectionFilter.BaseComparator;

public class EssaiMain extends BaseEssai<Material>
{

	public EssaiMain()
	{ 
		// Crée 3 materiaux foireux a partir du fichier.
		// 1	2.1E+05	355	0.3	7850	S355				index, Young modulus [Mpa], yield stress [Mpa], Poisson coefficient, specific weight [kg/m3], label
		// 2	2.1E+05	420	0.3	7850	S420				index, Young modulus [Mpa], yield stress [Mpa], Poisson coefficient, specific weight [kg/m3], label
		// 3	2.1E+05	460	0.3	7850	S460				index, Young modulus [Mpa], yield stress [Mpa], Poisson coefficient, specific weight [kg/m3], label
		put(0, new Material(1, 2.1E+05, 355, 0.3, 7850, "S355"));
		put(1, new Material(2, 2.1E+05, 420, 0.4, 7850, "S420"));
		put(2, new Material(3, 2.1E+05, 460, 0.5, 7850, "S460"));
		put(3, new Material(3, 2.1E+05, 460, 0.5, 7850, "D460"));
		put(4, new Material(3, 2.1E+05, 460, 0.5, 7850, "E460*"));
		put(5, new Material(3, 2.1E+05, 460, 0.5, 7850, "S460_1"));
		put(6, new Material(3, 2.1E+05, 460, 0.5, 7850, "S460x_1"));
		put(7, new Material(3, 2.1E+05, 460, 0.5, 7850, "S460y_1"));
		put(8, new Material(3, 2.1E+05, 460, 0.5, 7850, "S460z_2"));
		put(9, new Material(3, 2.1E+05, 460, 0.5, 7850, "S460_3"));
		
	}
	
	public static void main(String args[]) throws Exception
	{
		System.err.println("*1********");
		
		// Simple recherche d'equality sur un champ
		EssaiMain db = new EssaiMain();
		ArrayList<Material> list = db.getByFieldValue("Label", "S460");
		for (int i = 0; i < list.size(); i++)
		{
			Material m = list.get(i);
			System.err.println("Elemt "+i+" " +m);
		}
		
		
System.err.println("*2********");
		
		// Rercher plus compliqu�e sur plusieur champs si on veux.
		BaseComparator<Material> compareur = new BaseComparator<>(){
			@Override
			public boolean isInCriterias(Object o)
			{
				Material m = (Material) o;
				if (m.getPoissonCoefficient()<=0.4)
					if (m.getLabel().equals("S355")==true)
					return true;
				return false;
			}
		};
		
		ArrayList<Material> list2 = db.getByComparator(compareur);
		for (int i = 0; i < list2.size(); i++)
		{
			Material m = list2.get(i);
			System.err.println("Elemt "+i+" " +m);
		}
		
System.err.println("*3********");
		
		// Rercher plus compliqu�e sur plusieur champs si on veux.
		BaseComparator<Material> compareur2 = new BaseComparator<>(){
			@Override
			public boolean isInCriterias(Object o)
			{
				Material m = (Material) o;
					if (m.getLabel().matches("S460.*_?")==true)
					return true;
				return false;
			}
		};
		
		ArrayList<Material> list3 = db.getByComparator(compareur2);
		for (int i = 0; i < list3.size(); i++)
		{
			Material m = list3.get(i);
			System.err.println("Elemt "+i+" " +m);
		}
	}

	
}
