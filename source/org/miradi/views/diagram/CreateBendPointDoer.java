/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.DiagramLink;
import org.miradi.project.Project;

public class CreateBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		if (! isInDiagram())
			return false;
		
		DiagramLink[] selectedLinks = getDiagramView().getDiagramPanel().getOnlySelectedLinks();
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
		
		Project project = getProject();
		
		DiagramLink selectedLink = getDiagramView().getDiagramPanel().getOnlySelectedLinks()[0];
		BendPointCreator bendPointCreator = new BendPointCreator(getDiagramView().getDiagramComponent());
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			bendPointCreator.createBendPoint(getLocation(), selectedLink);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
}
