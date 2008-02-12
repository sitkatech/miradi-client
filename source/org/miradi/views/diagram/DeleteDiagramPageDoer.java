/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.ViewData;
import org.miradi.project.DiagramObjectDeleteHelper;
import org.miradi.project.Project;

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
