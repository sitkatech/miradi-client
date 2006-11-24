/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandDiagramRemoveFactorLink;
import org.conservationmeasures.eam.commands.CommandDiagramRemoveFactor;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorCluster;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorLink;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.FactorCluster;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ProjectDoer;

public class Delete extends ProjectDoer
{
	public Delete()
	{
		super();
	}
	
	public Delete(Project project)
	{
		setProject(project);
	}
	
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;

		EAMGraphCell[] selected = getProject().getSelectedAndRelatedCells();
		return (selected.length > 0);
	}

	public void doIt() throws CommandFailedException
	{
		EAMGraphCell[] selectedRelatedCells = getProject().getSelectedAndRelatedCells();
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			for(int i=0; i < selectedRelatedCells.length; ++i)
			{
				EAMGraphCell cell = selectedRelatedCells[i];
				if(cell.isFactorLink())
					deleteFactorLink((DiagramFactorLink)cell);	
			}
			
			for(int i=0; i < selectedRelatedCells.length; ++i)
			{
				EAMGraphCell cell = selectedRelatedCells[i];
				if(cell.isFactor())
					deleteFactor((DiagramFactor)cell);
			}
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void deleteFactorLink(DiagramFactorLink linkageToDelete) throws CommandFailedException
	{
		DiagramFactorLinkId id = linkageToDelete.getDiagramLinkageId();
		CommandDiagramRemoveFactorLink removeCommand = new CommandDiagramRemoveFactorLink(id);
		getProject().executeCommand(removeCommand);
		CommandDeleteObject deleteLinkage = new CommandDeleteObject(ObjectType.MODEL_LINKAGE, linkageToDelete.getWrappedId());
		getProject().executeCommand(deleteLinkage);
	}

	// TODO: This method should be inside Project and should have unit tests
	private void deleteFactor(DiagramFactor factorToDelete) throws Exception
	{
		DiagramFactorId id = factorToDelete.getDiagramFactorId();

		removeFromView(id);
		//TODO: Silly to pass id since it is in the factor itself
		removeFromCluster(factorToDelete, id);
		removeNodeFromDiagram(factorToDelete, id);

		Factor underlyingNode = factorToDelete.getUnderlyingObject();
		deleteAnnotations(underlyingNode);
		deleteUnderlyingNode(underlyingNode);
	}

	private void removeFromCluster(DiagramFactor factorToDelete, DiagramFactorId id) throws ParseException, CommandFailedException
	{
		DiagramFactorCluster cluster = (DiagramFactorCluster)factorToDelete.getParent();
		if(cluster != null)
		{
			CommandSetObjectData removeFromCluster = CommandSetObjectData.createRemoveIdCommand(
					cluster.getUnderlyingObject(),
					FactorCluster.TAG_MEMBER_IDS, 
					id);
			getProject().executeCommand(removeFromCluster);
		}
	}

	private void removeFromView(DiagramFactorId id) throws ParseException, Exception, CommandFailedException
	{
		Command[] commandsToRemoveFromView = getProject().getCurrentViewData().buildCommandsToRemoveNode(id);
		for(int i = 0; i < commandsToRemoveFromView.length; ++i)
			getProject().executeCommand(commandsToRemoveFromView[i]);
	}

	private void removeNodeFromDiagram(DiagramFactor factorToDelete, DiagramFactorId id) throws CommandFailedException
	{
		Command[] commandsToClear = factorToDelete.buildCommandsToClear();
		getProject().executeCommands(commandsToClear);
		
		getProject().executeCommand(new CommandDiagramRemoveFactor(id));
	}

	private void deleteUnderlyingNode(Factor factorToDelete) throws CommandFailedException
	{
		Command[] commandsToClear = factorToDelete.createCommandsToClear();
		getProject().executeCommands(commandsToClear);
		
		getProject().executeCommand(new CommandDeleteObject(factorToDelete.getType(), factorToDelete.getModelNodeId()));
	}
	
	private void deleteAnnotations(Factor factorToDelete) throws Exception
	{
		deleteAnnotations(factorToDelete, ObjectType.GOAL, factorToDelete.TAG_GOAL_IDS);
		deleteAnnotations(factorToDelete, ObjectType.OBJECTIVE, factorToDelete.TAG_OBJECTIVE_IDS);
		deleteAnnotations(factorToDelete, ObjectType.INDICATOR, factorToDelete.TAG_INDICATOR_IDS);
	}

	private void deleteAnnotations(Factor factorToDelete, int annotationType, String annotationListTag) throws Exception
	{
		IdList ids = new IdList(factorToDelete.getData(annotationListTag));
		for(int annotationIndex = 0; annotationIndex < ids.size(); ++annotationIndex)
		{
			EAMBaseObject thisAnnotation = (EAMBaseObject)getProject().findObject(annotationType, ids.get(annotationIndex));
			Command[] commands = DeleteAnnotationDoer.buildCommandsToDeleteAnnotation(getProject(), factorToDelete, annotationListTag, thisAnnotation);
			
			for(int commandIndex = 0; commandIndex < commands.length; ++commandIndex)
				getProject().executeCommand(commands[commandIndex]);
		}
		
	}
}
