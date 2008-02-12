/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.views.ViewDoer;

public class SelectAllDoer extends ViewDoer 
{
	public SelectAllDoer()
	{
		super();
	}
	
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isInDiagram())
			return false;
		
		int nSize = getProject().getAllDiagramFactorIds().length;
		return (nSize > 0);
	}
	
	public void doIt() throws CommandFailedException 
	{
		getMainWindow().preventActionUpdates();
		try
		{
			DiagramView diagramView = (DiagramView)getView();
			diagramView.getDiagramComponent().selectAll();
		}
		finally
		{
			getMainWindow().allowActionUpdates();
			getMainWindow().updateActionsAndStatusBar();
		}
	}
}
