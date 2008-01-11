/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ViewDoer;

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
		
		if (! inInDiagram())
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
		}
	}
}
