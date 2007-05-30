/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.utils.PointList;
import org.jgraph.graph.GraphLayoutCache;

public class CreateBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isDiagramView())
			return false;
		
		DiagramFactorLink[] selectedLinks = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		if (selectedLinks.length != 1)
			return false;
		
		if (selectedLinks[0].bendPointAlreadyExists(getLocation()))
			return false;
		
		return true;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			diagramModel = getDiagramView().getDiagramModel();
			diagram = getDiagramView().getDiagramComponent();
			cache = diagram.getGraphLayoutCache();
			
			DiagramFactorLink selectedLink = getDiagramView().getDiagramPanel().getOnlySelectedLinks()[0];
			LinkCell linkCell = diagramModel.getDiagramFactorLink(selectedLink);
			Point newBendPoint = linkCell.getNewBendPointLocation(diagramModel, cache, getLocation());
			Point snapped = getProject().getSnapped(newBendPoint);
			PointList newListWithBendPoint = linkCell.getNewBendPointList(diagramModel, cache, snapped);
			
			CommandSetObjectData setBendPointsCommand = CommandSetObjectData.createNewPointList(selectedLink, DiagramFactorLink.TAG_BEND_POINTS, newListWithBendPoint);
			getProject().executeCommand(setBendPointsCommand);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	DiagramComponent diagram;
	GraphLayoutCache cache;
	DiagramModel diagramModel;
}
