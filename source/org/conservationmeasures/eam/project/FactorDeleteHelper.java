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
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ThreatReductionResult;
import org.conservationmeasures.eam.views.diagram.DeleteAnnotationDoer;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;

public class FactorDeleteHelper
{
	public FactorDeleteHelper(DiagramModel modelToUse)
	{
		currentModel = modelToUse;
	}
	
	public void deleteFactor(DiagramFactor diagramFactorToDelete) throws Exception
	{
		Factor underlyingFactor = diagramFactorToDelete.getWrappedFactor();
		removeFromGroupBox(diagramFactorToDelete);
		removeFromThreatReductionResults(diagramFactorToDelete.getWrappedFactor());
		removeFromView(diagramFactorToDelete.getWrappedORef());
		removeNodeFromDiagram(getDiagramObject(), diagramFactorToDelete.getDiagramFactorId());
		deleteDiagramFactor(diagramFactorToDelete);
	
		if (! canDeleteFactor(underlyingFactor))
			return;

		deleteAnnotations(underlyingFactor);
		deleteUnderlyingNode(underlyingFactor);
	}

	private void removeFromGroupBox(DiagramFactor diagramFactor) throws Exception
	{
		ORef owningGroupRef = diagramFactor.getOwningGroupBox();
		if (owningGroupRef.isInvalid())
			return;
		
		DiagramFactor owningGroup = DiagramFactor.find(getProject(), owningGroupRef);
		CommandSetObjectData removeDiagramFactorFromGroup = CommandSetObjectData.createRemoveORefCommand(owningGroup, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, diagramFactor.getRef());
		getProject().executeCommand(removeDiagramFactorFromGroup);
	}

	private void removeFromThreatReductionResults(Factor factor) throws CommandFailedException
	{
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
		getProject().executeCommandsWithoutTransaction(commandsToClear);
		
		CommandDeleteObject deleteDiagramFactorCommand = new CommandDeleteObject(ObjectType.DIAGRAM_FACTOR, diagramFactor.getDiagramFactorId());
		getProject().executeCommand(deleteDiagramFactorCommand);
	}

	private void removeFromView(ORef factorRef) throws ParseException, Exception, CommandFailedException
	{
		Command[] commandsToRemoveFromView = getProject().getCurrentViewData().buildCommandsToRemoveNode(factorRef);
		for(int i = 0; i < commandsToRemoveFromView.length; ++i)
			getProject().executeCommand(commandsToRemoveFromView[i]);
	}

	private void removeNodeFromDiagram(DiagramObject diagramObject, DiagramFactorId idToDelete) throws CommandFailedException, ParseException
	{
		CommandSetObjectData removeDiagramFactor = CommandSetObjectData.createRemoveIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, idToDelete);
		getProject().executeCommand(removeDiagramFactor);
	}

	private void deleteUnderlyingNode(Factor factorToDelete) throws CommandFailedException
	{
		Command[] commandsToClear = factorToDelete.createCommandsToClear();
		getProject().executeCommandsWithoutTransaction(commandsToClear);
		getProject().executeCommand(new CommandDeleteObject(factorToDelete.getType(), factorToDelete.getFactorId()));
	}
	
	private void deleteAnnotations(Factor factorToDelete) throws Exception
	{
		deleteAnnotationIds(factorToDelete, ObjectType.GOAL, factorToDelete.TAG_GOAL_IDS);
		deleteAnnotationIds(factorToDelete, ObjectType.OBJECTIVE, factorToDelete.TAG_OBJECTIVE_IDS);
		deleteAnnotationIds(factorToDelete, ObjectType.INDICATOR, factorToDelete.TAG_INDICATOR_IDS);
		//TODO: there is much common code between DeleteAnnotationDoer and DeleteActivity classes and this class; 
		// for example DeleteActivity.deleteTaskTree( is general and and good not just for activities
		// I am thinking that each object Task should be able to handle its own deletion so when you call it it would delete all its own 
		// children inforceing referencial integrity as a cascade, instead of having the the code here.
		if (factorToDelete.isStrategy())
			removeAndDeleteTasksInList(factorToDelete, Strategy.TAG_ACTIVITY_IDS);
		
		if (factorToDelete.isTarget())
		{
			deleteAnnotationRefs(factorToDelete, Stress.getObjectType(), Target.TAG_STRESS_REFS);
			deleteAnnotationIds(factorToDelete, KeyEcologicalAttribute.getObjectType(), Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
		}
	}

	private void deleteAnnotationRefs(Factor factorToDelete, int annotationType, String annotationListTag) throws Exception
	{
		ORefList annotationRefs = new ORefList(factorToDelete.getData(annotationListTag));
		deleteAnnotations(factorToDelete, annotationRefs, annotationListTag);
	}
	
	private void deleteAnnotationIds(Factor factorToDelete, int annotationType, String annotationListTag) throws Exception
	{
		IdList ids = new IdList(annotationType, factorToDelete.getData(annotationListTag));
		deleteAnnotations(factorToDelete, new ORefList(annotationType, ids), annotationListTag);
	}
	
	private void deleteAnnotations(Factor factorToDelete, ORefList annotationRefs, String annotationListTag) throws Exception
	{
		for (int i = 0; i < annotationRefs.size(); ++i)
		{
			BaseObject thisAnnotation = getProject().findObject(annotationRefs.get(i));
			Command[] commands = DeleteAnnotationDoer.buildCommandsToDeleteReferencedObject(getProject(), factorToDelete, annotationListTag, thisAnnotation);
			getProject().executeCommandsWithoutTransaction(commands);
		}
	}
	
	private void removeAndDeleteTasksInList(BaseObject objectToDelete, String annotationListTag) throws Exception
	{
		ORefList hierarchyWithParent = new ORefList();
		hierarchyWithParent.add(objectToDelete.getRef());
		IdList ids = new IdList(Task.getObjectType(), objectToDelete.getData(annotationListTag));
		for(int annotationIndex = 0; annotationIndex < ids.size(); ++annotationIndex)
		{
			Task childTask = (Task)getProject().findObject(ObjectType.TASK, ids.get(annotationIndex));
			DeleteActivity.deleteTaskTree(getProject(), hierarchyWithParent, childTask);
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
