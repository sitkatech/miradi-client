/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.ViewDoer;

public class ZoomOut extends ViewDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		DiagramView view = (DiagramView)getView();
		view.getDiagramComponent().zoom(1.0/ZoomIn.ZOOM_FACTOR);
	}
}
