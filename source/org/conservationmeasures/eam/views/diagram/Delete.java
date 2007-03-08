/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandDiagramRemoveFactor;
import org.conservationmeasures.eam.commands.CommandDiagramRemoveFactorLink;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorCluster;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorCluster;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.ProjectDoer;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;

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
					deleteFactorLink(getProject(), cell.getDiagramFactorLink());	
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

	public static void deleteFactorLink(Project project, DiagramFactorLink linkageToDelete) throws CommandFailedException
	{	
		DiagramFactorLinkId id = linkageToDelete.getDiagramLinkageId();
		
		CommandDiagramRemoveFactorLink removeCommand = new CommandDiagramRemoveFactorLink(id);
		project.executeCommand(removeCommand);
		
		Command[] commandsToClear = project.findObject(ObjectType.FACTOR_LINK, linkageToDelete.getWrappedId()).createCommandsToClear();
		project.executeCommands(commandsToClear);
		
		CommandDeleteObject deleteLinkage = new CommandDeleteObject(ObjectType.FACTOR_LINK, linkageToDelete.getWrappedId());
		project.executeCommand(deleteLinkage);
		
		CommandDeleteObject removeFactorLinkCommand = new CommandDeleteObject(ObjectType.DIAGRAM_LINK, id);
		project.executeCommand(removeFactorLinkCommand);
	}

	// TODO: This method should be inside Project and should have unit tests
	private void deleteFactor(DiagramFactor factorToDelete) throws Exception
	{
		removeFromView(factorToDelete.getWrappedId());
		removeFromCluster(factorToDelete);
		removeNodeFromDiagram(factorToDelete);

		Factor underlyingNode = factorToDelete.getUnderlyingObject();
		deleteAnnotations(underlyingNode);
		deleteUnderlyingNode(underlyingNode);
	}

	private void removeFromCluster(DiagramFactor factorToDelete) throws ParseException, CommandFailedException
	{
		DiagramFactorCluster cluster = (DiagramFactorCluster)factorToDelete.getParent();
		if(cluster != null)
		{
			CommandSetObjectData removeFromCluster = CommandSetObjectData.createRemoveIdCommand(
					cluster.getUnderlyingObject(),
					FactorCluster.TAG_MEMBER_IDS, 
					factorToDelete.getDiagramFactorId());
			getProject().executeCommand(removeFromCluster);
		}
	}

	private void removeFromView(FactorId id) throws ParseException, Exception, CommandFailedException
	{
		Command[] commandsToRemoveFromView = getProject().getCurrentViewData().buildCommandsToRemoveNode(id);
		for(int i = 0; i < commandsToRemoveFromView.length; ++i)
			getProject().executeCommand(commandsToRemoveFromView[i]);
	}

	private void removeNodeFromDiagram(DiagramFactor factorToDelete) throws CommandFailedException
	{
		Command[] commandsToClear = factorToDelete.buildCommandsToClear();
		getProject().executeCommands(commandsToClear);
		
		getProject().executeCommand(new CommandDiagramRemoveFactor(factorToDelete.getDiagramFactorId()));
	}

	private void deleteUnderlyingNode(Factor factorToDelete) throws CommandFailedException
	{
		Command[] commandsToClear = factorToDelete.createCommandsToClear();
		getProject().executeCommands(commandsToClear);
		getProject().executeCommand(new CommandDeleteObject(factorToDelete.getType(), factorToDelete.getFactorId()));
	}
	
	private void deleteAnnotations(Factor factorToDelete) throws Exception
	{
		deleteAnnotations(factorToDelete, ObjectType.GOAL, factorToDelete.TAG_GOAL_IDS);
		deleteAnnotations(factorToDelete, ObjectType.OBJECTIVE, factorToDelete.TAG_OBJECTIVE_IDS);
		deleteAnnotations(factorToDelete, ObjectType.INDICATOR, factorToDelete.TAG_INDICATOR_IDS);
		//TODO: there is much common code between DeleteAnnotationDoer and DeleteActivity classes and this class; 
		// for example DeleteActivity.deleteTaskTree( is general and and good not just for activities
		// I am thinking that each object Task should be able to handle its own deletion so when you call it it would delete all its own 
		// children inforceing referencial integrity as a cascade, instead of having the the code here.
		if (factorToDelete.isStrategy())
			removeAndDeleteTasksInList(factorToDelete, Strategy.TAG_ACTIVITY_IDS);
	}

	
	private void deleteAnnotations(Factor factorToDelete, int annotationType, String annotationListTag) throws Exception
	{
		IdList ids = new IdList(factorToDelete.getData(annotationListTag));
		for(int annotationIndex = 0; annotationIndex < ids.size(); ++annotationIndex)
		{
			EAMBaseObject thisAnnotation = (EAMBaseObject)getProject().findObject(annotationType, ids.get(annotationIndex));
			Command[] commands = DeleteAnnotationDoer.buildCommandsToDeleteAnnotation(getProject(), factorToDelete, annotationListTag, thisAnnotation);
			getProject().executeCommands(commands);
		}
	}
	

	private void removeAndDeleteTasksInList(EAMBaseObject objectToDelete, String annotationListTag) throws Exception
	{
		IdList ids = new IdList(objectToDelete.getData(annotationListTag));
		for(int annotationIndex = 0; annotationIndex < ids.size(); ++annotationIndex)
		{
			Task childTask = (Task)getProject().findObject(ObjectType.TASK, ids.get(annotationIndex));
			DeleteActivity.deleteTaskTree(getProject(), childTask);
		}
	}
}
