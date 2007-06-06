/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ViewDoer;

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
					deleteFactorLink(diagramObject,  cell.getDiagramFactorLink());	
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

	public static void deleteFactorLink(DiagramObject diagramObject, ORef diagramFactorLinkRef) throws Exception
	{
		DiagramFactorLink linkageToDelete = (DiagramFactorLink)diagramObject.getProject().findObject(diagramFactorLinkRef);
		deleteFactorLink(diagramObject, linkageToDelete);
	}
	
	public static void deleteFactorLink(DiagramObject diagramObject, DiagramFactorLink linkageToDelete) throws Exception
	{	
		Project project = diagramObject.getProject();
		DiagramFactorLinkId id = linkageToDelete.getDiagramLinkageId();
		CommandSetObjectData removeDiagramFactorLink = CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, id);
		project.executeCommand(removeDiagramFactorLink);
		
		Command[] commandsToClearDiagramLink = linkageToDelete.createCommandsToClear();
		project.executeCommands(commandsToClearDiagramLink);
		
		CommandDeleteObject removeFactorLinkCommand = new CommandDeleteObject(ObjectType.DIAGRAM_LINK, id);
		project.executeCommand(removeFactorLinkCommand);

		if (!canDeleteFactorLink(project, linkageToDelete))
				return;

		Command[] commandsToClear = project.findObject(ObjectType.FACTOR_LINK, linkageToDelete.getWrappedId()).createCommandsToClear();
		project.executeCommands(commandsToClear);
		
		CommandDeleteObject deleteLinkage = new CommandDeleteObject(ObjectType.FACTOR_LINK, linkageToDelete.getWrappedId());
		project.executeCommand(deleteLinkage);
	}

	private static boolean canDeleteFactorLink(Project project, DiagramFactorLink linkageToDelete)
	{
		ObjectManager objectManager = project.getObjectManager();
		FactorLinkId factorLinkId = linkageToDelete.getWrappedId();
		FactorLink factorLink = (FactorLink) project.findObject(new ORef(ObjectType.FACTOR_LINK, factorLinkId));
		ORefList referrers = factorLink.findObjectsThatReferToUs(objectManager, ObjectType.DIAGRAM_LINK, factorLink.getRef());
		if (referrers.size() > 0)
			return false;
		
		return true;
	}
}
