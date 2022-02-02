package org.warnotte.waxlib3.waxlib2.DXFWriter;

import java.io.IOException;

public interface WriterInterface {
	
	public void DrawLine(double x1, double y1, double x2, double y2, int col, boolean dashed);
	public void DrawText(double x1, double y1, String text, double fontSize, int col, float angle);
	public void DrawCircle(double x1, double y1, double radius, int col, boolean dashed);
	
	public void DrawRectangle(double x1, double y1, double x2, double y2, int col, boolean dashed);
	
	public void FillRectangle(double x1, double y1, double x2, double y2, int col, boolean dashed);
	
	public void CreateBlock(String name);
	public void EndBlock();
	
	public void EndSection();
	/**
	 * Insert a block.
	 * @param x1
	 * @param y1
	 * @param BlockName
	 */
	public void Insert(double x1, double y1, String BlockName);
	
	public void Insert(double x1, double y1, double scaleX, double scaleY, double rotation, String BlockName);
	
	public void includeDXFEntity(String string) throws IOException;
}
