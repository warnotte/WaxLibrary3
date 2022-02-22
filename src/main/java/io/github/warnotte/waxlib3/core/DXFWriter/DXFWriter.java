package io.github.warnotte.waxlib3.core.DXFWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class DXFWriter extends PrintStreamWriter {

	public int Layer = 0;
	private double ZoomX = 1000;
	private double ZoomY = 1000;
	double translateX = 0;
	double translateY = 0;
	private int LayerOffset = 0;
	
	/*public synchronized int getLayer() {
		return Layer;
	}*/

	public synchronized void setLayer(int layer) {
		Layer = layer+getLayerOffset();
	}

	public static void main(String[] args) throws Exception {
		
		DXFWriter writer = new DXFWriter();
		writer.createFile("test.dxf");
		writer.createSection("BLOCKS");
		writer.CreateBlock("WAXBLOCK");
		writer.DrawLine(0, 0, 100, 0, 1, false);
		writer.DrawLine(100, 0, 100, 100, 2, false);
		writer.DrawLine(0, 100, 100, 100, 3, false);
		writer.DrawLine(0, 100, 0, 0, 4, false);	
		writer.DrawLine(0, 100, 100, 0, 5, true);
		writer.DrawLine(100, 100, 0, 0, 6, true);
		writer.EndBlock();
		
		//writer.CreateBlock("BLOCK_PS");
		//writer.includeDXFEntity("Datas\\Models\\DXFPlans\\DXFBlocksDetails\\2_P_S_BLOCK.dxf");
	//	writer.EndBlock();
		
		writer.EndSection();
		
		writer.createSection("ENTITIES");
		writer.includeDXFEntity("Datas\\Models\\DXFPlans\\fond_plan_A.dxf");
		
		writer.setLayer(2);
		writer.DrawCircle(50, 50, 50, 7, true);
		writer.DrawCircle(50, 50, 25, 7, false);
		writer.DrawText(50, 125, "Wax", 40, 8);
		writer.DrawRectangle(100, 100, 200, 200, 7, false);
		writer.DrawRectangle(120, 120, 180, 180, 3, true);
		
		writer.setLayer(3);
		writer.Insert(10, 10, "WAXBLOCK");
		
		writer.setLayer(4);
		writer.Insert(10, -100, "WAXBLOCK");
		
		//writer.setLayer(8);
		//writer.Insert(50,-50, "BLOCK_PS");
		
		writer.EndSection();
		writer.closeFile();
	}

	
	public void DrawLine(double x1, double y1, double x2, double y2, int col, boolean dashed) {
		x1+=translateX;
		y1+=translateY;
		x2+=translateX;
		y2+=translateY;
		x1*=getZoomX();
		x2*=getZoomX();
		y1*=getZoomY();
		y2*=getZoomY();
		y2=-y2;
		y1=-y1;
		ps.println("LINE");
		ps.println("5\r\n0");
		ps.printf("10\r\n%f\r\n20\r\n%f\r\n", x1, y1);
		ps.printf("11\r\n%f\r\n21\r\n%f\r\n", x2, y2);
		ps.printf("62\r\n%d\r\n", col);
		writeLayerStuff();
	
		if (dashed==true)
		{
			ps.println("6");
			  ps.println("DASHED");
		}
		ps.println("0");
	}

	


	public void DrawText(double x1, double y1, String text, double fontSize, int col) {
		DrawText(x1, y1, text, fontSize, col, 0);
	}
	
	
	public void DrawText(double x1, double y1, String text, double fontSize, int col, float angle) {
		x1+=translateX;
		y1+=translateY;
		x1*=getZoomX();
		y1*=getZoomY();
		fontSize*=getZoomX();
		y1=-y1;
		ps.println("TEXT");
		ps.println("100\r\n0");
		ps.printf("10\r\n%f\r\n20\r\n%f\r\n", x1, y1);
		ps.printf("40\r\n%f\r\n", fontSize);
		ps.printf("1\r\n%s\r\n", text);
		ps.printf("62\r\n%d\r\n", col);
		ps.printf("50\r\n%f\r\n", angle);
		ps.printf("72\r\n%d\r\n", 0);
		writeLayerStuff();
		ps.println("0");
	}

	
	public void DrawCircle(double x1, double y1, double radius, int col, boolean dashed) {
		x1+=translateX;
		y1+=translateY;
		x1*=getZoomX();
		y1*=getZoomY();
		y1=-y1;
		ps.println("CIRCLE");
		ps.println("5\r\n0");
		ps.printf("10\r\n%f\r\n20\r\n%f\r\n", x1, y1);
		ps.printf("40\r\n%f\r\n", radius);
		ps.printf("62\r\n%d\r\n", col);
		writeLayerStuff();
		if (dashed == true)
		{
			ps.println("6");
			ps.println("DASHED");
		}
		ps.println("0");
	}

	
	public void DrawRectangle(double x1, double y1, double x2, double y2, int col, boolean dashed) {
		
	/*	
		x1*=Zoom;
		y1*=Zoom;
		x2*=Zoom;
		y2*=Zoom;
		y1=-y1;
		y2=-y2;*/
		
		// TODO : Tenter la polyline.
		DrawLine(x1, y1, x2, y1, col, dashed);
		DrawLine(x2, y1, x2, y2, col, dashed);
		DrawLine(x2, y2, x1, y2, col, dashed);
		DrawLine(x1, y2, x1, y1, col, dashed);
		
		
	}
	
	
	public void FillRectangle(double x1, double y1, double x2, double y2, int col, boolean dashed) {
		x1+=translateX;
		y1+=translateY;
		x2+=translateX;
		y2+=translateY;
		x1*=getZoomX();
		y1*=getZoomY();
		x2*=getZoomX();
		y2*=getZoomY();
		y1=-y1;
		y2=-y2;
		ps.println("SOLID");
		ps.println("5\r\n0");
		writeLayerStuff();
		ps.printf("10\r\n%f\r\n20\r\n%f\r\n", x1, y1);
		ps.printf("11\r\n%f\r\n21\r\n%f\r\n", x1, y2);
		ps.printf("12\r\n%f\r\n22\r\n%f\r\n", x2, y1);
		ps.printf("13\r\n%f\r\n23\r\n%f\r\n", x2, y2);
		ps.printf("62\r\n%d\r\n", col);
		ps.println("0");
	}
	
	public void Insert(double x1, double y1, String BlockName)
	{
		
		Insert(x1, y1, 1,1,0, BlockName);
	}
	
	
	public void Insert(double x1, double y1, double scaleX, double scaleY, double rotation, String BlockName)
	{
		x1+=translateX;
		y1+=translateY;
		x1*=getZoomX();
		y1*=getZoomY();
		y1=-y1;
		ps.println("INSERT");
		ps.println("5\r\n0");
		ps.println("2\r\n"+BlockName);
		ps.printf("10\r\n%f\r\n20\r\n%f\r\n", x1, y1);
		writeLayerStuff();
		ps.printf("41\r\n%f\r\n", scaleX);
		ps.printf("42\r\n%f\r\n", scaleY);
		ps.printf("50\r\n%f\r\n", rotation);
		ps.println("0");
	}

	/* (non-Javadoc)
	 * @see WaxDXFWriter.WriterInterface#CreateBlock(java.lang.String)
	 */
	
	public void CreateBlock(String name)
	{
		ps.println("BLOCK");
		ps.println("5");
		ps.println("0");
		ps.println("2");
		ps.println(""+name);
		ps.println("70");
		ps.println("0");
		ps.println("10");
		ps.println("0.0");
		ps.println("20");
		ps.println("0.0");
		ps.println("30");
		ps.println("0.0");
		ps.println("3");
		ps.println(""+name);
		ps.println("1");
		ps.println("");
	//	ps.println("8");
	//	ps.println("LAYER_TEST");
		
		ps.println("0");
	}

	/* (non-Javadoc)
	 * @see WaxDXFWriter.WriterInterface#EndBlock()
	 */
	
	public void EndBlock()
	{
		ps.println("ENDBLK");
		ps.println("0");
	}
	
	/**
	 * @param string
	 * @throws IOException 
	 */
	public void includeDXFEntity(String string) throws IOException
	{
		String fondpland = FiletoString(string);
		ps.print(fondpland);
	}

	public synchronized double getTranslateX()
	{
		return translateX;
	}

	public synchronized void setTranslateX(double translateX)
	{
		this.translateX = translateX;
	
	}

	public synchronized double getTranslateY()
	{
		return translateY;
	}

	public synchronized void setTranslateY(double translateY)
	{
		this.translateY = translateY;
	}

	public int getLayerOffset()
	{
		return LayerOffset;
	}

	public void setLayerOffset(int layerOffset)
	{
		LayerOffset = layerOffset;
		
		
	}

	public synchronized double getZoomX()
	{
		return ZoomX;
	}

	public synchronized void setZoomX(double zoomX)
	{
		ZoomX = zoomX;
	
	}

	public synchronized double getZoomY()
	{
		return ZoomY;
	}

	public synchronized void setZoomY(double zoomY)
	{
		ZoomY = zoomY;
	
	}

	
	public static Map<Integer, String> mapLayerName = new HashMap<>();
	
	/**
	 * 
	 */
	private void writeLayerStuff()
	{
		if (Layer!=0)
		{
			String str = "";
			if (mapLayerName!=null)
			{
				String named = mapLayerName.get(Layer);
				if (named!=null)
					str = named;
			}
			ps.printf("8\r\n%s_%d_%s\r\n", "OPTIVIEW", Layer, str);
		}
	}

	

	
	
	
}
