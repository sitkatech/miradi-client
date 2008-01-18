/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.diagram.DiagramPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.DiagramObjectDeleteHelper;
import org.conservationmeasures.eam.project.Project;

abstract public class DeleteDiagramPageDoer extends DiagramPageDoer
{
	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		
		Project project = getProject();
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			DiagramPanel diagramPanel = getDiagramView().getCurrentDiagramPanel();
			DiagramObjectDeleteHelper deleteHelper = new DiagramObjectDeleteHelper(project, diagramPanel);
			deleteHelper.deleteDiagram();
			selectFirstItem(project, diagramPanel);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	private void selectFirstItem(Project project, DiagramPanel diagramPanel) throws Exception
	{
		ViewData viewData = project.getCurrentViewData();
		String currentObjectTag = getDiagramObjectTag();
		int objectType = diagramPanel.getDiagramSplitPane().getContentType();
		EAMObjectPool pool = project.getPool(objectType);
		ORefList refList = pool.getORefList();
		if (refList.size() == 0)
			return;
		
		final int FIRST_ITEM_INDEX = 0;
		ORef firstItem = refList.get(FIRST_ITEM_INDEX);
		CommandSetObjectData setCurrentDiagram = new CommandSetObjectData(viewData.getRef(), currentObjectTag, firstItem.toString());
		project.executeCommand(setCurrentDiagram);
	}
}
