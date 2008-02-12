/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.dialogs.slideshow.SlideShowViewer;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.ViewDoer;

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