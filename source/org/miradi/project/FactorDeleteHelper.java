/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.project;

import java.text.ParseException;

import org.miradi.commands.Command;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.views.diagram.DeleteAnnotationDoer;
import org.miradi.views.umbrella.DeleteActivity;

public class FactorDeleteHelper
{
	public FactorDeleteHelper(DiagramModel modelToUse)
	{
		currentModel = modelToUse;
	}
	
	public void deleteFactor(DiagramFactor diagramFactorToDelete) throws Exception
	{
		deleteDiagramFactorAndUnderlyingFactor(diagramFactorToDelete);
	}

	private void deleteDiagramFactorAndUnderlyingFactor(DiagramFactor diagramFactorToDelete) throws Exception
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
