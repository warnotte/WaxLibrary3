package io.github.warnotte.waxlib3.core.IOCSV.tests;

import io.github.warnotte.waxlib3.core.IOCSV.DTOToLine;
import io.github.warnotte.waxlib3.core.IOCSV.lineToDTO;

public class StiffUpdateOrder implements lineToDTO, DTOToLine
{
	public int CS_ID;
	public int PANEL_ID;
	public int NODE1_ID;
	public int NODE2_ID;
	public Mode mode; // EE1 ou EE2
	public double STIFF_Spacing;
	public String STIFF_MaterialID;
	public String STIFF_StiffPropertyID;
	
	public StiffUpdateOrder()
	{
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "StiffUpdateOrder [CS_ID=" + CS_ID + ", PANEL_ID=" + PANEL_ID + ", NODE1_ID=" + NODE1_ID + ", NODE2_ID=" + NODE2_ID + ", mode=" + mode + ", STIFF_Spacing=" + STIFF_Spacing + ", STIFF_MaterialID=" + STIFF_MaterialID + ", STIFF_StiffPropertyID=" + STIFF_StiffPropertyID + "]";
	}

	/* (non-Javadoc)
	 * @see IO.lineToDTO#convertLineToDTO(java.lang.String)
	 */
	@Override
	public void convertLineToDTO(String line)
	{
		// TODO : Le mode n'est pas pris en compte.
		String vs [] = line.split(";");
		
		CS_ID = (int) Double.parseDouble(vs[0]);
		PANEL_ID = (int) Double.parseDouble(vs[1]);
		NODE1_ID = (int) Double.parseDouble(vs[2]);
		NODE2_ID = (int) Double.parseDouble(vs[3]);
		int _mode = (int)Double.parseDouble(vs[4]);
		mode = Mode.EE1;
		switch (_mode)
		{
			case 0 : mode = Mode.EE1;break;
			case 1 : mode = Mode.EE2;break;
			case 2 : mode = Mode.TRUESPACING;break;
		}
		STIFF_Spacing = Double.parseDouble(vs[5]);
		//STIFF_Direction = Direction.valueOf(vs[6]);
		//STIFF_Side = Side.valueOf(vs[7]);
		STIFF_MaterialID =  ""+(int) Double.parseDouble(vs[6]);
		STIFF_StiffPropertyID =  ""+(int) Double.parseDouble(vs[7]);
		
	}

	/* (non-Javadoc)
	 * @see org.warnotte.waxlib2.IOCSV.DTOToLine#convertDTOToLine()
	 */
	@Override
	public String[] convertDTOToLine()
	{
		String ret [] = new String [] {
				""+CS_ID,
				""+PANEL_ID,
				""+NODE1_ID,
				""+NODE2_ID,
				(mode==Mode.EE1?"0":
				(mode==Mode.EE2?"1":
				"2")
				),
				""+STIFF_Spacing,
				""+STIFF_MaterialID,
				""+STIFF_StiffPropertyID
		};
		return ret;
	}
	
	
	
}