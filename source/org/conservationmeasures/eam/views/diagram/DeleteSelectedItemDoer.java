/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;
import org.conservationmeasures.eam.views.diagram.wizard.LinkDeletor;

public class DeleteSelectedItemDoer extends ViewDoer
{
	public DeleteSelectedItemDoer()
	{
		super();
	}
	
	public DeleteSelectedItemDoer(Project project)
	{
		setProject(project);
	}
	
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;

		if (! isDiagramView())
			return false;
		
		EAMGraphCell[] selected = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		EAMGraphCell[] selectedRelatedCells = getDiagramView().getDiagramPanel().getSelectedAndRelatedCells();
		Project project = getProject();
		
		DiagramView diagramView = getDiagramView();
		DiagramModel model = diagramView.getDiagramModel();
		DiagramObject diagramObject = model.getDiagramObject();

		project.executeCommand(new CommandBeginTransaction());
		try
		{
			for(int i=0; i < selectedRelatedCells.length; ++i)
			{
				EAMGraphCell cell = selectedRelatedCells[i];
				if(cell.isFactorLink())
				{
					new LinkDeletor(project).deleteFactorLink(cell.getDiagramFactorLink().getWrappedId());
				}
			}
			
			for(int i=0; i < selectedRelatedCells.length; ++i)
			{
				EAMGraphCell cell = selectedRelatedCells[i];
				if(cell.isFactor())
				{
					new FactorCommandHelper(project, model).deleteFactor((FactorCell)cell, diagramObject);
				}
			}
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
}
