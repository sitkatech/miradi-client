/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.JTable;

public class MoveSlideDownDoer extends MoveSlideDoer
{
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		if (getPicker()==null)
			return false;
		
		int rowCount = ((JTable)getPicker()).getRowCount();
		int selectedRow = ((JTable)getPicker()).getSelectedRow();
		
		if ((selectedRow < 0) || (selectedRow == rowCount-1))
			return false;
		
		return true;
	}

	public int getDirection()
	{
		return 1;
	}
}