package io.github.warnotte.waxlib3.W2D.PanelGraphique;


import javax.swing.JFrame;

import io.github.warnotte.waxlib3.OBJ2GUI.ParseurAnnotations;
import io.github.warnotte.waxlib3.OBJ2GUI.Annotations.GUI_CLASS;
import io.github.warnotte.waxlib3.OBJ2GUI.Annotations.GUI_FIELD_TYPE;
import io.github.warnotte.waxlib3.OBJ2GUI.Events.MyEventListener;
import io.github.warnotte.waxlib3.waxlibswingcomponents.Dialog.DialogDivers;

@GUI_CLASS(type = GUI_CLASS.Type.BoxLayout, BoxLayout_property = GUI_CLASS.Type_BoxLayout.Y)
public class ConfigurationGeneral
{

	
	@GUI_FIELD_TYPE(type = GUI_FIELD_TYPE.Type.CHECKBOX)
	protected boolean	DrawHelpLines					= true;
	//@GUI_FIELD_TYPE(type = GUI_FIELD_TYPE.Type.CHECKBOX)
	protected boolean	EnableGridding					= true;
	@GUI_FIELD_TYPE(type = GUI_FIELD_TYPE.Type.TEXTFIELD)
	protected double	GRID_SIZE						= 1.0d; // Metres
	
	@GUI_FIELD_TYPE(type = GUI_FIELD_TYPE.Type.CHECKBOX)
	protected boolean	DrawGrid						= true;

	//@GUI_FIELD_TYPE(type = GUI_FIELD_TYPE.Type.TEXTFIELD)
	protected static double	MouseDetectionArea			= 1;		// Taille du delta de la
														// detection des
														// interaction avec la
														// souris (pour cliquer
														// plus ou moins pres
														// des elements);
	
	public JFrame createFrame(MyEventListener list)
	{
		JFrame frame;
		try
		{
			frame = ParseurAnnotations.CreateFrameFromObject("Configuration vue 2D", this, list);
			frame.setSize(320,400);
			return frame;
		} catch (Exception e)
		{
		//	Logs.getLogger().fatal(e);
			DialogDivers.Show_dialog(e, "");
			e.printStackTrace();
		}
		return null;
	}

	public synchronized boolean isDrawHelpLines()
	{
		return DrawHelpLines;
	}

	public synchronized boolean isEnableGridding()
	{
		return EnableGridding;
	}

	public synchronized double getGRID_SIZE()
	{
		return GRID_SIZE;
	}

	public synchronized boolean isDrawGrid()
	{
		return DrawGrid;
	}

	public static synchronized double getMouseDetectionArea()
	{
		return MouseDetectionArea;
	}

	public synchronized void setDrawHelpLines(boolean drawHelpLines)
	{
		DrawHelpLines = drawHelpLines;
	}

	public synchronized void setEnableGridding(boolean enableGridding)
	{
		EnableGridding = enableGridding;
	}

	public synchronized void setGRID_SIZE(double gRIDSIZE)
	{
		GRID_SIZE = gRIDSIZE;
	}

	public synchronized void setDrawGrid(boolean drawGrid)
	{
		DrawGrid = drawGrid;
	}

	public static synchronized void setMouseDetectionArea(double mouseDetectionArea)
	{
		MouseDetectionArea = mouseDetectionArea;
	}



	

}
