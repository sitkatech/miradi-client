/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.DiagramFactorLink;

public class CreateBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		return (getProject().getOnlySelectedLinks().length == 1);
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			DiagramFactorLink selectedLink = getProject().getOnlySelectedLinks()[0];
			Point newBendPoint = getLocation();

			CommandSetObjectData setBendPointCommand = CommandSetObjectData.createAppendPointCommand(selectedLink, DiagramFactorLink.TAG_BEND_POINTS, newBendPoint);
			getProject().executeCommand(setBendPointCommand);
			
			//TODO remove commented code after finishing bend point work
//			//LinkCell linkCell = getProject().getDiagramModel().findLinkCell(selectedLink);
//			
//			String bendPointsAsString = selectedLink.getData(DiagramFactorLink.TAG_BEND_POINTS);
//			PointList bendPoints = new PointList(bendPointsAsString);
//			for (int i = 0 ; i < bendPoints.size(); i++)
//				System.out.println(bendPoints.get(i));
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

}
