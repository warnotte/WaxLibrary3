package io.github.warnotte.waxlib3.core.Updater;

//import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane; 

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * Cette classe permet de faire une mise à jour de votre application
 * 
 * @author Wichtounet
 */
public class Updater
{
	// Document xml
	private Document	xmlDocument		= null;

	// Variable contenant le nom du repertoire courant
	private final String		currentFolder	= System.getProperty("user.dir");

	private int	percentage;

	private int	old_percentage;

	public boolean isNewVersionAivalable(String xmlPath, String actualVersion)
	{
		ArrayList<String> versions = getVersions(xmlPath);

		// Si la version est nulle
		if (versions.size() == 0)
		{
			//JOptionPane.showMessageDialog(null, "Impossible de se connecter au service, vérifiez votre " + "connection internet");
		} else
		{
			// Si la dernière version n'est pas la même que l'actuelle

			String lastVersionOnServer = versions.get(versions.size() - 1);			
			
			if(!lastVersionOnServer.equals(actualVersion) && (isClientVersionNeedUptade(lastVersionOnServer,actualVersion )))

			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Cette méthode permet de mettre à jour votre programme, elle va chercher
	 * sur internet la dernière version disponible et effectue la mise à jour
	 * selon le consentement de l'utilisateur
	 */
	public boolean update(String xmlPath, String actualVersion)
	{
		ArrayList<String> versions = getVersions(xmlPath);

		// Si la version est nulle
		if (versions.size() == 0)
		{
			JOptionPane.showMessageDialog(null, "Impossible de se connecter au service, vérifiez votre " + "connection internet");
		} else
		{
			// Si la dernière version n'est pas la même que l'actuelle

			String lastVersionOnServer = versions.get(versions.size() - 1);
			

			if (!lastVersionOnServer.equals(actualVersion) && (isClientVersionNeedUptade(lastVersionOnServer, actualVersion)))

			{

				String versionChoisie = (String) JOptionPane.showInputDialog(null, "Actual version is " + actualVersion, "Versions disponibles", JOptionPane.QUESTION_MESSAGE, null, versions.toArray(), versions.get(versions.size() - 1));

				if (versionChoisie == null)
				{
					JOptionPane.showMessageDialog(null, "Canceled");
					return false;
				}

				// S'il veut la télécharger
				if (versionChoisie != "")
				{
					Element racine = xmlDocument.getRootElement();

					// On liste toutes les versions
					List<?> listVersions = racine.getChildren("version");
					Iterator<?> iteratorVersions = listVersions.iterator();

					String destination = "";
					String method = "";

					// On parcourt toutes les versions
					while (iteratorVersions.hasNext())
					{
						Element version = (Element) iteratorVersions.next();

						Element elementNom = version.getChild("nom");

						// Si c'est la bonne version, on télécharge tous ses fichiers
						if (elementNom.getText().equals(versionChoisie))
						{
							// Element elementFiles = version.getChild("files");

							// On liste tous les fichiers d'une version
							// List listFiles =
							// elementFiles.getChildren("file");
							// Iterator iteratorFiles = listFiles.iterator();

							// On parcours chaque fichier de la version
							// while(iteratorFiles.hasNext()){
							// Element file = (Element)iteratorFiles.next();

							method = version.getChildText("method");
							destination = version.getChildText("destination");

							String url = version.getChildText("url");
							url = url.replace("\t", "");
							url = url.replace("\n", "");
							url = url.replace("\r", "");
							System.err.println("Download file from " + url);
							// On télécharge le fichier

							try
							{
								downloadFile(url, currentFolder + File.separator + destination);
							} catch (Exception e)
							{
								JOptionPane.showMessageDialog(null, "Impossible de trouver l'update sur le serveur : " + e);
								// e.printStackTrace();
								return false;
							}
							// }

							break;
						}
					}

					if (method.contains("installer"))
					{
						// File lanceur = new File(lanceurPath);
						// On lance le lanceur
						JOptionPane.showMessageDialog(null, "La nouvelle version a été téléchargée dans " + destination + " lancement de l'installation");
						openURL(destination);
						// On quitte le programme
						System.exit(0);
					} else if (method.contains("zip"))
					{
						JOptionPane.showMessageDialog(null, "La nouvelle version a été téléchargée dans " + destination);
						openURL(destination);
						return true;
					}
					return true;
				}
			} else
			{
				JOptionPane.showMessageDialog(null, "Pas de nouvelles version disponible pour le moment");
				return false;
			}
		}
		return false;
	}

	public static void openURL(String urlText)
	{
	/*	if (Desktop.isDesktopSupported())
		{
			URI uri = URI.create(urlText);
			try
			{
				Desktop.getDesktop().browse(uri);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}*/
	}

	private static boolean isClientVersionNeedUptade(String lastVersionOnServer, String actualVersion)
	  /*{
	        int VernumrRef = (new Integer((new StringBuilder()).append(lastVersionOnServer.split("[.]")[0]).toString())).intValue();
	        int MajorRef = (new Integer((new StringBuilder()).append(lastVersionOnServer.split("[.]")[1]).toString())).intValue();
	        int MinorRef = (new Integer((new StringBuilder()).append(lastVersionOnServer.split("[.]")[2]).toString())).intValue();
	        int VernumrUser = (new Integer((new StringBuilder()).append(actualVersion.split("[.]")[0]).toString())).intValue();
	        int MajorUser = (new Integer((new StringBuilder()).append(actualVersion.split("[.]")[1]).toString())).intValue();
	        int MinorUser = (new Integer((new StringBuilder()).append(actualVersion.split("[.]")[2]).toString())).intValue();
	        if(VernumrUser > VernumrRef)
	            return false;
	        if(VernumrUser < VernumrRef)
	            return true;
	        if(MajorUser < MajorRef)
	            return true;
	        if(MinorUser > MinorRef)
	            return false;
	        return MinorRef > MinorUser;
	    }
//<<<<<<< Updater.java

	
//	private static boolean isClientVersionNeedUptade(String lastVersionOnServer, String actualVersion)
//=======
/*
	public static void main(String args[]) throws Exception
//>>>>>>> 1.4*/
	{
		
		int VernumrRef = Integer.parseInt(""+lastVersionOnServer.split("[.]")[0]);
		int MajorRef = Integer.parseInt(""+lastVersionOnServer.split("[.]")[1]);
		int MinorRef = Integer.parseInt(""+lastVersionOnServer.split("[.]")[2]);
		
		int VernumrUser = Integer.parseInt(""+actualVersion.split("[.]")[0]);
		int MajorUser = Integer.parseInt(""+actualVersion.split("[.]")[1]);
		int MinorUser = Integer.parseInt(""+actualVersion.split("[.]")[2]);
		
		
		int VUSER = VernumrUser * 0xFF00 + MajorUser * 0x00FF + MinorUser;
		int VREF = VernumrRef * 0xFF00 + MajorRef * 0x00FF + MinorRef;
		
		System.err.println("Version Refreence = "+VREF);
		System.err.println("Version User = "+VUSER);
		
		if (VUSER>VREF)
			return false;
		else
			return true;
		/*
		if (VernumrUser>VernumrRef)// Si la version du client est plus petit il faut upd8
			return false;
		
		if (VernumrUser<VernumrRef)// Si la version du client est plus petit il faut upd8
			return true;
		else 
		{
			if (MajorUser<MajorRef)
				return true;
			
			if (MinorUser>MinorRef)
				return false;
			if (MinorRef<MinorUser)
				return true;
		}
		/*
		else
		{
			if (MajorRef<MajorUser)
				return false;
			else
				if (MinorRef<MinorUser)
					return false;
		}
	
		return false;*/
	}
	
	public static void main(String args[])
	{
		System.err.println(""+Updater.isClientVersionNeedUptade("0.1.1", "0.1.0"));// vrai
		
		System.err.println(""+Updater.isClientVersionNeedUptade("0.5.0", "0.4.6"));// vrai
		System.err.println(""+Updater.isClientVersionNeedUptade("0.5.0", "1.4.6"));// faux
		System.err.println(""+Updater.isClientVersionNeedUptade("0.5.0", "1.0.0"));// faux
		System.err.println(""+Updater.isClientVersionNeedUptade("0.1.0", "0.5.0"));// faux
		System.err.println(""+Updater.isClientVersionNeedUptade("0.1.0", "0.5.1"));// faux
		
		System.err.println(""+Updater.isClientVersionNeedUptade("1.5.0", "0.4.6"));// vrai
		System.err.println(""+Updater.isClientVersionNeedUptade("1.4.0", "1.4.6"));// faux
		System.err.println(""+Updater.isClientVersionNeedUptade("0.5.0", "1.0.0"));// faux
		System.err.println(""+Updater.isClientVersionNeedUptade("1.5.0", "0.5.0"));// vrai
		System.err.println(""+Updater.isClientVersionNeedUptade("1.5.0", "0.5.1"));// vrai
		System.err.println(""+Updater.isClientVersionNeedUptade("1.5.0", "0.4.9"));// vrai
		

	}

	/**
	 * Cette méthode va chercher sur internet les versions disponibles pour
	 * l'application
	 * 
	 * @return les versions disponibles
	 */
	private ArrayList<String> getVersions(String xmlPath)
	{
		ArrayList<String> versions = new ArrayList<String>();

		try
		{
			URL xmlUrl = new URL(xmlPath);

			// On ouvre une connections ur la page
			URLConnection urlConnection = xmlUrl.openConnection();
			urlConnection.setUseCaches(false);

			// On se connecte sur cette page
			urlConnection.connect();

			// On récupère le fichier XML sous forme de flux
			InputStream stream = urlConnection.getInputStream();

			SAXBuilder sxb = new SAXBuilder();

			// On crée le document xml avec son flux
			try
			{
				xmlDocument = sxb.build(stream);
			} catch (JDOMException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}

			// On récupère la racine
			Element racine = xmlDocument.getRootElement();

			// On liste toutes les versions
			List<?> listVersions = racine.getChildren("version");
			Iterator<?> iteratorVersions = listVersions.iterator();

			// On parcourt toutes les versions
			while (iteratorVersions.hasNext())
			{
				Element version = (Element) iteratorVersions.next();

				Element elementNom = version.getChild("nom");

				versions.add(elementNom.getText());
			}

			// On trie la liste
			Collections.sort(versions);

		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return versions;
	}

	/**
	 * Cette méthode télécharge une fichier sur internet et le stocke en local
	 * 
	 * @param filePath chemin du fichier à télécharger
	 * @param destination chemin du fichier en local
	 * @throws Exception
	 */
	private void downloadFile(String filePath, String destination) throws Exception
	{
		DownloadProgressFrame dpf = new DownloadProgressFrame();
		dpf.setVisible(true);
		
		long start = System.currentTimeMillis();
			URLConnection connection = null;
			InputStream is = null;
			BufferedOutputStream destinationFile = null;
	
			try
			{
				// On crée l'URL
				URL url = new URL(filePath);
	
				// On crée une connection vers cet URL
				connection = url.openConnection();
	
				if (connection == null)
					throw new Exception("Not a valid url " + filePath);
				// On récupère la taille du fichier
				int length = connection.getContentLength();
	
				// Si le fichier est inexistant, on lance une exception
				if (length == -1)
				{
					throw new IOException("Fichier vide");
				}
	
				// On récupère le stream du fichier
				is = new BufferedInputStream(connection.getInputStream());
	/*
				if (is == null)
					throw new Exception("Not a valid url " + filePath);
				// On prépare le tableau de bits pour les données du fichier
				*/
				
				File f = new File("downloads");
				f.mkdir();
	
				
				
				
				
				
				// On crée un stream sortant vers la destination
			//	destinationFile = ;
				
				destinationFile = new BufferedOutputStream(new FileOutputStream(destination));
	
				byte[] data = null;
				
				data = new byte[1]; // si ut mets pas un buffer de 1 ca foire il ecrit trop.
	
				// On déclare les variables pour se retrouver dans la lecture du
				// fichier
				int currentBit = 0;
				int deplacement = 0;
	
				// Tant que l'on n'est pas à la fin du fichier, on récupère des
				// données
				int transfertrate = 0;  
				
				dpf.setProgress(0, deplacement, length,transfertrate, 9999999);
				
				while (deplacement < length)
				{
					
					currentBit = is.read(data, 0, data.length);
					if (currentBit == -1)
						break;
					// On écrit les données du fichier dans ce stream
					destinationFile.write(data);
					
					deplacement += currentBit;
					
					long now = System.currentTimeMillis();
					long elapsed = now-start; // Temps deja ecoule
					float achievement = (float)deplacement /(float)length; // Accompli (de 0 a 1).
					long timeevaluatedtobe = (long) (elapsed*1/achievement);
					long remaining = timeevaluatedtobe-elapsed;
					
					
					percentage = (int) (achievement*100);
					if (percentage!=old_percentage)
					//	if (cpt++%500000==0)
					{
						
						dpf.setProgress(percentage, deplacement, length, (int)(deplacement/elapsed),(int)remaining);
						if (percentage%10==0)
						System.err.printf("%5d %3d %% ETA %5d %5d\r\n",(int)elapsed ,(int)(achievement*100),(int)timeevaluatedtobe,remaining);
						old_percentage = percentage;
					}
				//	System.err.println(elapsed+ " "+(achievement*100)+" % ETA "+timeevaluatedtobe);
				}
				dpf.setProgress((100), deplacement, length, 0,0);
	
				// Si on est pas arrivé à la fin du fichier, on lance une exception
				if (deplacement != length)
				{
					throw new IOException("Le fichier n'a pas été lu en entier (seulement " + deplacement + " sur " + length + ")");
				}
	
	
	
				// On vide le tampon et on ferme le stream
				destinationFile.flush();
				is.close();
				destinationFile.close();
				dpf.setVisible(false);
				dpf.dispose();
	
			} 
			catch (OutOfMemoryError e)
			{
				e.printStackTrace();
				throw new Exception("File too large to fit in memory " + filePath);
			} 
			catch (MalformedURLException e)
			{
				System.err.println("Problème avec l'URL : " + filePath);
			}
			catch (IndexOutOfBoundsException e)
			{
				e.printStackTrace();
				throw new Exception("Not a valid url " + filePath);
			}
			
			catch (IOException e)
			{
				e.printStackTrace();
				throw new Exception("Not a valid url " + filePath);
			} 
			finally
			{
				try
				{
					is.close();
					destinationFile.close();
					dpf.setVisible(false);
					dpf.dispose();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

}