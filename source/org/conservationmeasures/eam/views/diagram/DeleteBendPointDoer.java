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
import org.conservationmeasures.eam.utils.PointList;

public class DeleteBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		DiagramFactorLink[] links = getProject().getOnlySelectedLinks();
		if (links.length != 1)
			return false;
		
		PointList bendPoints = links[0].getBendPoints();
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
		DiagramFactorLink[] links = getProject().getOnlySelectedLinks();
		DiagramFactorLink diagramFactorLink = links[0];
		DiagramFactorLinkId linkId = diagramFactorLink.getDiagramLinkageId();
		
		PointList bendPoints = diagramFactorLink.getBendPoints();
		PointList newBendPoints = getBendPointListMinusDeletedPoint(bendPoints);
		String newBendPointList = newBendPoints.toJson().toString();
		String oldBendPointList = bendPoints.toJson().toString();
		
		CommandSetObjectData removeBendPointCommand = new CommandSetObjectData(ObjectType.DIAGRAM_LINK, linkId, DiagramFactorLink.TAG_BEND_POINTS, newBendPointList, oldBendPointList);
		getProject().executeCommand(removeBendPointCommand);
	}
	
	private PointList getBendPointListMinusDeletedPoint(PointList currentBendPoints)
	{
		PointList newBendPoints = new PointList();
		Point clickLocation = getLocation();
		if (clickLocation == null)
		{
			newBendPoints.addAll(currentBendPoints.getAllPoints());
			newBendPoints.removePoint(0);
			return newBendPoints;
		}
		
		for (int i = 0; i < currentBendPoints.size(); i++)
		{
			Point bendPoint = currentBendPoints.get(i);
			if (!isWithinRange(bendPoint, clickLocation))
				newBendPoints.add(bendPoint);
		}
		
		return newBendPoints;
	}
	
	private boolean isWithinRange(Point bendPoint, Point clickLocation)
	{
		//TODO check JGraph to see what the bounds of the bend point is, if any.
		final double MAX_DISTANCE = 10.0;
		double distance = clickLocation.distance(bendPoint);
		if (distance <= MAX_DISTANCE)
			return true;
		
		return false;
	}
}
