package org.warnotte.waxlib3.waxlib2.Updater;

public class mainUpdater
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Updater update = new Updater();
		Version.set(0,0,5,"MonkeyDude");
		
		//\Projets\WaxLib\WaxLib\WaxLibrary\Updater\program.xml
		
		//File f = new File("WaxLib/WaxLibrary/Updater/program.xml");
		String url = "http://renaud.warnotte.be/Projects/Electribulator/maj_electribulator.xml";
		if (update.isNewVersionAivalable(url.toString(),Version.getShortVersionString())==true)
			update.update(url.toString(),Version.getShortVersionString());
		else
			System.err.println("Nothing new");
		
	}
	

}
