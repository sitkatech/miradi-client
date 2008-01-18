/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.ViewDoer;

public class ToggleSlideShowPanelDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		return isInDiagram();
	}

	public void doIt()
	{
		if(!isAvailable())
			return;
		
		try
		{
			if (getDiagramView().isSlideShowVisible())
				getDiagramView().disposeOfSlideShowDialog();
			else
				getDiagramView().showSlideShowPanel();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
}
		
