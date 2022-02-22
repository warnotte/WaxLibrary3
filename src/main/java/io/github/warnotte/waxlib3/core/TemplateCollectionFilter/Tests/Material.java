package io.github.warnotte.waxlib3.core.TemplateCollectionFilter.Tests;

public class Material
{
	//1	2.1E+05	355	0.3	7850	S355				index, Young modulus [Mpa], yield stress [Mpa], Poisson coefficient, specific weight [kg/m3], label
	long ID;
	double YoungModulus=2.1E+05;
	double YieldStress=355;
	double PoissonCoefficient=0.3;
	double SpecificWeight=7850; // kg/m3
	String Label = "FerailleRouill�e"; // Oui Oui de le FERaille Jd ;D
	
	
	@Override
	public String toString()
	{
		return "Material [ID=" + ID + ", Label=" + Label + ", PoissonCoefficient=" + PoissonCoefficient + ", SpecificWeight=" + SpecificWeight + ", YieldStress=" + YieldStress + ", YoungModulus=" + YoungModulus + "]";
	}

	public Material(long iD, double youngModulus, double yieldStress, double poissonCoefficient, double specificWeight, String label)
	{
		super();
		ID = iD;
		YoungModulus = youngModulus;
		YieldStress = yieldStress;
		PoissonCoefficient = poissonCoefficient;
		SpecificWeight = specificWeight;
		Label = label;
	}

	public synchronized double getYoungModulus()
	{
		return YoungModulus;
	}

	public synchronized double getYieldStress()
	{
		return YieldStress;
	}

	public synchronized double getPoissonCoefficient()
	{
		return PoissonCoefficient;
	}

	public synchronized double getSpecificWeight()
	{
		return SpecificWeight;
	}

	public synchronized String getLabel()
	{
		return Label;
	}

	public synchronized void setYoungModulus(double youngModulus)
	{
		YoungModulus = youngModulus;
	}

	public synchronized void setYieldStress(double yieldStress)
	{
		YieldStress = yieldStress;
	}

	public synchronized void setPoissonCoefficient(double poissonCoefficient)
	{
		PoissonCoefficient = poissonCoefficient;
	}

	public synchronized void setSpecificWeight(double specificWeight)
	{
		SpecificWeight = specificWeight;
	}

	public synchronized void setLabel(String label)
	{
		Label = label;
	}
	
	

}
