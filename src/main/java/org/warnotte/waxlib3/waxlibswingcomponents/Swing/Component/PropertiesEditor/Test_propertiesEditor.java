package org.warnotte.waxlib3.waxlibswingcomponents.Swing.Component.PropertiesEditor;


import java.util.Properties;



public class Test_propertiesEditor
{
	
	Properties properties = null;

	public Test_propertiesEditor()
    {
		properties = new Properties();
    	properties.put("Key1", "Valeur_1");
    	properties.put("Key2", "1234");
    	properties.put("Key3", "6541");
    	properties.put("Key4", "Valeur_2");
    }
	
    @Override
	public String toString()
    {
    	return "TestObject";
    }

	public Properties getProperties()
	{
		return properties;
	}
 }

//TextField, Slider, ComboBox, JList, DateChooser, ColorChooser, VectorViewer, [][]Viewer

