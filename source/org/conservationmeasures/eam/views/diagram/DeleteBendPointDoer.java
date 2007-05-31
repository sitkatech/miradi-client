/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.utils.BendPointList;

public class DeleteBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
	
		if (! isDiagramView())
			return false;
		
		DiagramFactorLink[] links = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		if (links.length != 1)
			return false;
		
		BendPointList bendPoints = links[0].getBendPoints();
		if (bendPoints.size() <= 0)
			return false;

		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		deleteBendPoint();
	}
	
	private void deleteBendPoint() throws CommandFailedException
	{
		DiagramFactorLink[] links = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
		DiagramFactorLink diagramFactorLink = links[0];
		DiagramFactorLinkId linkId = diagramFactorLink.getDiagramLinkageId();
		
		BendPointList bendPoints = diagramFactorLink.getBendPoints();
		BendPointList newBendPoints = getBendPointListMinusDeletedPoint(bendPoints);
		String newBendPointList = newBendPoints.toJson().toString();
		
		CommandSetObjectData removeBendPointCommand = new CommandSetObjectData(ObjectType.DIAGRAM_LINK, linkId, DiagramFactorLink.TAG_BEND_POINTS, newBendPointList);
		getProject().executeCommand(removeBendPointCommand);
	}
	
	private BendPointList getBendPointListMinusDeletedPoint(BendPointList currentBendPoints)
	{
		BendPointList newBendPoints = new BendPointList();
		Point clickLocation = getLocation();
		if (clickLocation == null)
			return removeFirstPoint(currentBendPoints, newBendPoints);
		
		newBendPoints.addAll(currentBendPoints.getAllPoints());		
		Point pointToRemove = currentBendPoints.getClosestPoint(clickLocation);
		newBendPoints.removePoint(pointToRemove);
		
		return newBendPoints;
	}

	private BendPointList removeFirstPoint(BendPointList currentBendPoints, BendPointList newBendPoints)
	{
		newBendPoints.addAll(currentBendPoints.getAllPoints());
		newBendPoints.removePoint(0);

		return newBendPoints;
	}
}
