/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.exceptions.CommandFailedException;

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
		
		//TODO finish adding bend points
		//DiagramFactorLink selectedLink = getProject().getOnlySelectedLinks()[0];
		//LinkCell linkCell = getProject().getDiagramModel().findLinkCell(selectedLink);
		
	}

}
