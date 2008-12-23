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
import java.util.Vector;

import org.jgraph.graph.GraphSelectionModel;
import org.miradi.commands.Command;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
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
	public FactorDeleteHelper(DiagramComponent diagramToUse)
	{
		this(diagramToUse.getDiagramModel().getDiagramObject());
		selectionModel = diagramToUse.getSelectionModel();
	}

	protected FactorDeleteHelper(DiagramObject diagramObjectToUse)
	{
		diagramObject = diagramObjectToUse;
	}

	public void deleteFactorAndDiagramFactor(DiagramFactor diagramFactorToDelete) throws Exception
	{
		clearSelection();
		
		if (Target.is(diagramFactorToDelete.getWrappedType()))
		{	
			Target target = (Target) diagramFactorToDelete.getWrappedFactor();
			deleteRelatedFactorDiagramFactors(target.getStressRefs());
		}
		
		if (Strategy.is(diagramFactorToDelete.getWrappedType()))
		{
			Strategy strategy = (Strategy) diagramFactorToDelete.getWrappedFactor();
			deleteRelatedFactorDiagramFactors(strategy.getActivityRefs());
		}
		
		deleteDiagramFactorAndUnderlyingFactor(diagramFactorToDelete);
	}

	public Vector<Command> buildCommandsToDeleteDiagramFactor(DiagramFactor diagramFactor) throws Exception
	{
		clearSelection();
		
		Vector<Command> deleteDiagramFactorCommands = new Vector();
		deleteDiagramFactorCommands.addAll(diagramFactor.createCommandsToClearAsList());
		deleteDiagramFactorCommands.add(new CommandDeleteObject(ObjectType.DIAGRAM_FACTOR, diagramFactor.getDiagramFactorId()));
		
		return deleteDiagramFactorCommands;
	}

	public void removeNodeFromDiagram(DiagramObject diagramObjectToUse, DiagramFactorId idToDelete) throws CommandFailedException, ParseException
	{
		clearSelection();
		
		CommandSetObjectData removeDiagramFactor = buildCommandToRemoveNodeFromDiagram(diagramObjectToUse, idToDelete);
		getProject().executeCommand(removeDiagramFactor);
	}

	public CommandSetObjectData buildCommandToRemoveNodeFromDiagram(DiagramObject diagramObjectToUse, DiagramFactorId idToDelete) throws ParseException
	{
		clearSelection();
		
		return CommandSetObjectData.createRemoveIdCommand(diagramObjectToUse, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, idToDelete);
	}
	
	
	
	private void clearSelection()
	{
		if(selectionModel == null)
			return;
		
		selectionModel.clearSelection();
	}

	private void deleteRelatedFactorDiagramFactors(ORefList factorRefs) throws Exception
	{
		for (int index = 0; index < factorRefs.size(); ++index)
		{
			Factor factor = (Factor) getProject().findObject(factorRefs.get(index));
			deleteFactorDiagramFactorInCurrentDiagram(factor);
		}
	}

	private void deleteFactorDiagramFactorInCurrentDiagram(Factor factor) throws Exception
	{
		ORefList diagramFactorReferrerRefs = factor.findObjectsThatReferToUs(DiagramFactor.getObjectType());
		ORefList currentContainedDiagramFactors = getDiagramObject().getAllDiagramFactorRefs();
		for (int index = 0; index < diagramFactorReferrerRefs.size(); ++index)
		{
			if (currentContainedDiagramFactors.contains(diagramFactorReferrerRefs.get(index)))
			{
				DiagramFactor diagramFactor = DiagramFactor.find(getProject(), diagramFactorReferrerRefs.get(index));		
				removeFromDiagramAndDelete(diagramFactor);
			}
		}
	}

	private void deleteDiagramFactorAndUnderlyingFactor(DiagramFactor diagramFactorToDelete) throws Exception
	{
		Factor underlyingFactor = diagramFactorToDelete.getWrappedFactor();
		removeFromGroupBox(diagramFactorToDelete);
		removeFromThreatReductionResults(diagramFactorToDelete.getWrappedFactor());
		removeFromView(diagramFactorToDelete.getWrappedORef());
		removeFromDiagramAndDelete(diagramFactorToDelete);
				
		if (underlyingFactor.mustBeDeletedBecauseParentIsGone())
			return;

		removeStrategyFromObjectiveRelevancyList(underlyingFactor);
		deleteAnnotations(underlyingFactor);
		deleteUnderlyingNode(underlyingFactor);
	}
	
	private void removeFromDiagramAndDelete(DiagramFactor diagramFactor) throws Exception
	{
		removeNodeFromDiagram(getDiagramObject(), diagramFactor.getDiagramFactorId());
		getProject().executeCommandsWithoutTransaction(buildCommandsToDeleteDiagramFactor(diagramFactor));
	}

	private void removeFromGroupBox(DiagramFactor diagramFactor) throws Exception
	{
		ORef owningGroupRef = diagramFactor.getOwningGroupBoxRef();
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

	private void removeFromView(ORef factorRef) throws ParseException, Exception, CommandFailedException
	{
		Command[] commandsToRemoveFromView = getProject().getCurrentViewData().buildCommandsToRemoveNode(factorRef);
		for(int i = 0; i < commandsToRemoveFromView.length; ++i)
			getProject().executeCommand(commandsToRemoveFromView[i]);
	}
	
	private void removeStrategyFromObjectiveRelevancyList(Factor factorToDelete) throws Exception
	{
		getProject().executeCommandsWithoutTransaction(DeleteActivity.buildRemoveFromObjectiveRelevancyListCommands(getProject(), factorToDelete));
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
			Command[] commands = DeleteAnnotationDoer.buildCommandsToDeleteAnnotation(getProject(), factorToDelete, annotationListTag, thisAnnotation);
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
		return diagramObject;
	}
	
	private Project getProject()
	{
		return diagramObject.getProject();
	}
	
	private DiagramObject diagramObject;
	private GraphSelectionModel selectionModel;
}
