
package io.github.warnotte.waxlib3.core.Updater;

public class Version {

    static int VersionNumber = 0;
    static int MajorRevisionNumber = 4;
    static int MinorRevisionNumber = 6;
    static String CodeName = "Gamma";
   
    public static String getVersionString()
    {
    	return getShortVersionString()+" Codename ["+CodeName+"]";
    }

    public static synchronized int getVersionNumber() {
        return VersionNumber;
    }

    public static synchronized void setVersionNumber(int versionNumber) {
        VersionNumber = versionNumber;
    }

    public static synchronized int getMajorRevisionNumber() {
        return MajorRevisionNumber;
    }

    public static synchronized void setMajorRevisionNumber(int majorRevisionNumber) {
        MajorRevisionNumber = majorRevisionNumber;
    }

    public static synchronized int getMinorRevisionNumber() {
        return MinorRevisionNumber;
    }

    public static synchronized void setMinorRevisionNumber(int minorRevisionNumber) {
        MinorRevisionNumber = minorRevisionNumber;
    }

	
    public static synchronized String getCodeName()
	{
		return CodeName;
	}
    

	public static synchronized void setCodeName(String codeName)
	{
		CodeName = codeName;
	}
	
	/**
	 * REcupere le numero de version sans le codename;
	 * @return
	 */
	public static String getShortVersionString()
	{
		return VersionNumber+"."+MajorRevisionNumber+"."+MinorRevisionNumber;
	}

	public static void set(int version, int majornum, int minornum, String codename)
	{
		setVersionNumber(version);
		setMajorRevisionNumber(majornum);
		setMinorRevisionNumber(minornum);
		setCodeName(codename);
	}
    
}
