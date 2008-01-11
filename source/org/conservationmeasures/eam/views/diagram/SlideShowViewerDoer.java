/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.dialogs.slideshow.SlideShowViewer;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ViewDoer;

public class SlideShowViewerDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (!isInDiagram())
			return false;
		
		if (viewer==null)
			return true;
		
		if (viewer.isVisible())
			return false;
		
		viewer = null;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		viewer = new SlideShowViewer(getMainWindow());
		viewer.setVisible(true);
	}
	
	SlideShowViewer viewer;

}