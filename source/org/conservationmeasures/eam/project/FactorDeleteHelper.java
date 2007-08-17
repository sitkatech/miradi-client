/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ThreatReductionResult;
import org.conservationmeasures.eam.views.diagram.DeleteAnnotationDoer;
import org.conservationmeasures.eam.views.diagram.DeleteKeyEcologicalAttributeDoer;
import org.conservationmeasures.eam.views.diagram.LinkDeletor;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;

public class FactorDeleteHelper
{
	public FactorDeleteHelper(DiagramModel modelToUse)
	{
		currentModel = modelToUse;
	}
	
	public void deleteFactor(DiagramFactor factorToDelete) throws Exception
	{
		FactorCell factorCellToDelete = currentModel.getFactorCellById(factorToDelete.getDiagramFactorId());
		deleteFactor(factorCellToDelete);
	}

	public void deleteFactor(FactorCell factorToDelete) throws Exception
	{
		removeFromThreatReductionResults(factorToDelete);
		removeFromView(factorToDelete.getWrappedId());
		removeNodeFromDiagram(getDiagramObject(), factorToDelete.getDiagramFactor());
		removeFactorFromAllResultsChains(factorToDelete);
		deleteDiagramFactor(factorToDelete.getDiagramFactor());
	
		Factor underlyingNode = factorToDelete.getUnderlyingObject();
		if (! canDeleteFactor(underlyingNode))
			return;

		deleteAnnotations(underlyingNode);
		deleteUnderlyingNode(underlyingNode);
	}


	private void removeFactorFromAllResultsChains(FactorCell factorToDelete) throws Exception
	{
		if (!factorToDelete.isTarget())
			return;
		
		ORefList allResultsChainRefs = getProject().getResultsChainDiagramPool().getORefList();
		for (int i = 0; i < allResultsChainRefs.size(); ++i)
		{
			ResultsChainDiagram resultsChain = (ResultsChainDiagram) getProject().findObject(allResultsChainRefs.get(i));
			removeFactorsFromResultsChain(factorToDelete, resultsChain);
		}
	}

	private void removeFactorsFromResultsChain(FactorCell factorToDelete, ResultsChainDiagram resultsChain) throws Exception
	{
		DiagramFactor target = factorToDelete.getDiagramFactor();
		ORef wrappedRef = target.getWrappedORef();
		IdList allDiagramFactorIds = resultsChain.getAllDiagramFactorIds();
		for (int i = 0; i < allDiagramFactorIds.size(); ++i)
		{
			ORef diagramFactorRef = new ORef(DiagramFactor.getObjectType(), allDiagramFactorIds.get(i));
			DiagramFactor thisDiagramFactor = (DiagramFactor) getProject().findObject(diagramFactorRef);
			if (!thisDiagramFactor.getWrappedORef().equals(wrappedRef))
				continue;
		
			removeNodeFromDiagram(resultsChain, thisDiagramFactor);
			removeAllAttachedLinks(resultsChain, thisDiagramFactor);
		}
	}

	private void removeAllAttachedLinks(ResultsChainDiagram resultsChain, DiagramFactor diagramFactor) throws Exception
	{
		ORefList factorsAboutToBeDeleted = new ORefList();
		factorsAboutToBeDeleted.add(diagramFactor.getRef());
		IdList diagramLinkIds = resultsChain.getAllDiagramFactorLinkIds();
		for (int i = 0; i < diagramLinkIds.size(); ++i)
		{
			ORef diagramLinkRef = new ORef(DiagramLink.getObjectType(), diagramLinkIds.get(i));
			DiagramLink diagramLink = (DiagramLink) getProject().findObject(diagramLinkRef);
			boolean sameAsFromDiagramFactor = diagramLink.getFromDiagramFactorId().equals(diagramFactor);
			boolean sameAsToDiagramFactor = diagramLink.getToDiagramFactorId().equals(diagramFactor.getDiagramFactorId());
			if (!sameAsFromDiagramFactor && !sameAsToDiagramFactor)
				continue;
			
			LinkDeletor linkDeletor = new LinkDeletor(getProject());
			linkDeletor.deleteFactorLink(diagramLink.getDiagramLinkageId(), factorsAboutToBeDeleted);
		}
	}

	private void removeFromThreatReductionResults(FactorCell factorToDelete) throws CommandFailedException
	{
		Factor factor = factorToDelete.getUnderlyingObject();
		EAMObjectPool pool = getProject().getPool(ObjectType.THREAT_REDUCTION_RESULT);
		ORefList orefList = pool.getORefList();
		for (int i = 0; i < orefList.size(); ++i)
		{
			ThreatReductionResult threatReductionResult = (ThreatReductionResult) getProject().findObject(orefList.get(i));
			ORef directThreatRef = ORef.createFromString(threatReductionResult.getRelatedDirectThreatRefAsString());
			if (! directThreatRef.equals(factor.getRef()))
				continue;
			
			CommandSetObjectData setDirectThreat = new CommandSetObjectData(threatReductionResult.getRef(), ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, ORef.INVALID.toString());
			getProject().executeCommand(setDirectThreat);
		}
	}

	private boolean canDeleteFactor(Factor factorToDelete)
	{
		ObjectManager objectManager = getProject().getObjectManager();
		ORefList referrers = factorToDelete.findObjectsThatReferToUs(objectManager, ObjectType.DIAGRAM_FACTOR, factorToDelete.getRef());
		if (referrers.size() > 0)
			return false;
		
		return true;
	}

	private void deleteDiagramFactor(DiagramFactor diagramFactor) throws CommandFailedException
	{
		Command[] commandsToClear = diagramFactor.createCommandsToClear();
		getProject().executeCommands(commandsToClear);
		
		CommandDeleteObject deleteDiagramFactorCommand = new CommandDeleteObject(ObjectType.DIAGRAM_FACTOR, diagramFactor.getDiagramFactorId());
		getProject().executeCommand(deleteDiagramFactorCommand);
	}

	private void removeFromView(FactorId id) throws ParseException, Exception, CommandFailedException
	{
		Factor factor = getProject().findNode(id);
		Command[] commandsToRemoveFromView = getProject().getCurrentViewData().buildCommandsToRemoveNode(factor.getRef());
		for(int i = 0; i < commandsToRemoveFromView.length; ++i)
			getProject().executeCommand(commandsToRemoveFromView[i]);
	}

	private void removeNodeFromDiagram(DiagramObject diagramObject, DiagramFactor diagramFactor) throws CommandFailedException, ParseException
	{
		DiagramFactorId idToDelete = diagramFactor.getDiagramFactorId();
		CommandSetObjectData removeDiagramFactor = CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, idToDelete);
		getProject().executeCommand(removeDiagramFactor);
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
		
		if (factorToDelete.isTarget())
			removeAndDeleteKeyEcologicalAttributesInList(factorToDelete, Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
	}

	private void deleteAnnotations(Factor factorToDelete, int annotationType, String annotationListTag) throws Exception
	{
		IdList ids = new IdList(factorToDelete.getData(annotationListTag));
		for(int annotationIndex = 0; annotationIndex < ids.size(); ++annotationIndex)
		{
			BaseObject thisAnnotation = getProject().findObject(annotationType, ids.get(annotationIndex));
			Command[] commands = DeleteAnnotationDoer.buildCommandsToDeleteAnnotation(getProject(), factorToDelete, annotationListTag, thisAnnotation);
			getProject().executeCommands(commands);
		}
	}
	
	private void removeAndDeleteTasksInList(BaseObject objectToDelete, String annotationListTag) throws Exception
	{
		IdList ids = new IdList(objectToDelete.getData(annotationListTag));
		for(int annotationIndex = 0; annotationIndex < ids.size(); ++annotationIndex)
		{
			Task childTask = (Task)getProject().findObject(ObjectType.TASK, ids.get(annotationIndex));
			DeleteActivity.deleteTaskTree(getProject(), childTask);
		}
	}
	
	private void removeAndDeleteKeyEcologicalAttributesInList(Factor objectToDelete, String annotationListTag) throws Exception
	{
		IdList ids = new IdList(objectToDelete.getData(annotationListTag));
		for(int annotationIndex = 0; annotationIndex < ids.size(); ++annotationIndex)
		{
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute)getProject().findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, ids.get(annotationIndex));
			Command[] commands = DeleteKeyEcologicalAttributeDoer.buildCommandsToDeleteAnnotation(getProject(), objectToDelete, annotationListTag, kea);
			getProject().executeCommands(commands);
		}
	}
	
	private DiagramObject getDiagramObject()
	{
		return currentModel.getDiagramObject();
	}
	
	private Project getProject()
	{
		return currentModel.getProject();
	}
	
	DiagramModel currentModel;
}
