/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.JTable;

public class MoveSlideUpDoer extends MoveSlideDoer
{
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		if (getPicker()==null)
			return false;
		
		int selectedRow = ((JTable)getPicker()).getSelectedRow();
		
		if ((selectedRow <= 0))
			return false;
		
		return true;
	}

	public int getDirection()
	{
		return -1;
	}
}

