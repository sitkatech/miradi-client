/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.diagram;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Stress;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.diagram.doers.HideStressBubbleDoer;

public abstract class DeleteAnnotationDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return (getSingleSelected(getAnnotationType()) != null);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
	
		String[] buttons = {EAM.text("Delete"), EAM.text("Retain"), };
		String[] dialogText = getDialogText();
		if(!EAM.confirmDialog(EAM.text("Delete"), dialogText, buttons))
			return;
	
		try
		{
			String tag = getAnnotationIdListTag();
			BaseObject annotationToDelete = getObjects()[0];
			BaseObject selectedFactor = getParent(annotationToDelete);

			deleteAnnotationViaCommands(getProject(), selectedFactor, annotationToDelete, tag);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}	
	}

	protected BaseObject getParent(BaseObject annotationToDelete)
	{
		return annotationToDelete.getOwner();
	}

	private void deleteAnnotationViaCommands(Project project, BaseObject owner, BaseObject annotationToDelete, String annotationIdListTag) throws Exception
	{
		Command[] commands = buildCommandsToDeleteAnnotation(project, owner, annotationIdListTag, annotationToDelete);
		getProject().executeCommandsAsTransaction(commands);
	}
	
	public static Command[] buildCommandsToDeleteAnnotation(Project project, BaseObject owner, String annotationIdListTag, BaseObject annotationToDelete) throws CommandFailedException, ParseException, Exception
	{
		Vector commands = new Vector();
		commands.addAll(buildCommandsToUntag(project, annotationToDelete.getRef()));
		commands.addAll(Arrays.asList(annotationToDelete.createCommandsToClear()));
		commands.addAll(buildCommandsToDeleteReferredObjects(project, owner, annotationIdListTag, annotationToDelete));
		commands.addAll(buildCommandsToDeleteReferringObjects(project, owner, annotationIdListTag, annotationToDelete));
		if (Indicator.is(annotationToDelete))
			commands.addAll(createCommandsToRemoveFromRelevancyList(project, annotationToDelete));
		
		commands.add(new CommandDeleteObject(annotationToDelete.getRef()));
		
		return (Command[])commands.toArray(new Command[0]);
	}

	public static Vector<Command> buildCommandsToUntag(Project project, ORef refToUntag) throws Exception
	{
		Vector<TaggedObjectSet> taggedObjectSetsWithFactor = project.getTaggedObjectSetPool().findTaggedObjectSetsWithFactor(refToUntag);
		Vector<Command> commandsToUntag = new Vector();
		for (int index = 0; index < taggedObjectSetsWithFactor.size(); ++index)
		{
			TaggedObjectSet taggedObjectSet = taggedObjectSetsWithFactor.get(index);
			CommandSetObjectData removeFromTaggedObjectSet = CommandSetObjectData.createRemoveORefCommand(taggedObjectSet, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, refToUntag);
			commandsToUntag.add(removeFromTaggedObjectSet);
		}
		
		return commandsToUntag;
	}

	private static Vector buildCommandsToDeleteReferredObjects(Project project, BaseObject owner, String annotationIdListTag,	BaseObject annotationToDelete) throws Exception
	{
		Vector commands = new Vector<Command>();
		commands.add(buildCommandToRemoveAnnotationFromObject(owner, annotationIdListTag, annotationToDelete.getRef()));
		
		if (Indicator.is(annotationToDelete))
		{
			commands.addAll(buildCommandsToDeleteMeasurements(project, (Indicator)annotationToDelete));
			commands.addAll(buildCommandsToDeleteMethods(project, (Indicator) annotationToDelete));
		}
		if (KeyEcologicalAttribute.is(annotationToDelete.getType()))
		{
			commands.addAll(buildCommandsToDeleteKEAIndicators(project, (KeyEcologicalAttribute) annotationToDelete));
		}
		
		return commands;
	}
	
	private static Vector<Command> buildCommandsToDeleteReferringObjects(Project project, BaseObject owner, String annotationIdListTag, BaseObject annotationToDelete) throws Exception
	{
		Vector<Command> commands = new Vector<Command>();
		if (Stress.is(annotationToDelete.getType()))
			commands.addAll(createCommandsToDeleteStressDiagramFactors(project, annotationToDelete));
		
		return commands;
	}

	private static Vector<Command> createCommandsToDeleteStressDiagramFactors(Project project, BaseObject annotationToDelete) throws Exception
	{
		Vector<Command> commandsToHide = new Vector();
		ORefList diagramFactorRefs = annotationToDelete.findObjectsThatReferToUs(DiagramFactor.getObjectType());
		for (int index = 0; index < diagramFactorRefs.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(index));
			ORefList conceptualModelRefs = diagramFactor.findObjectsThatReferToUs(ConceptualModelDiagram.getObjectType());
			for (int diagramRefIndex = 0; diagramRefIndex < conceptualModelRefs.size(); ++diagramRefIndex)
			{
				ConceptualModelDiagram conceptualModel = ConceptualModelDiagram.find(project, conceptualModelRefs.get(diagramRefIndex));
				commandsToHide.addAll(HideStressBubbleDoer.createCommandsToHideDiagramFactorForNonSelectedFactors(conceptualModel, diagramFactor));
			}
		}
		
		return commandsToHide;
	}
	
	private static Vector<Command> createCommandsToRemoveFromRelevancyList(Project project, BaseObject annotationToDelete) throws Exception
	{
		Vector<Command> removeFromRelevancyListCommands = new Vector();
		ORefList allObjectiveRefs = project.getObjectivePool().getORefList();
		for (int index = 0; index < allObjectiveRefs.size(); ++index)
		{
			Objective objective = Objective.find(project, allObjectiveRefs.get(index));
			ORefSet relevantIndicatorRefs = objective.getAllIndicatorRefsFromRelevancyOverrides();
			if (relevantIndicatorRefs.contains(annotationToDelete.getRef()))
			{
				ORefList listToRemoveFrom = new ORefList(relevantIndicatorRefs);
				listToRemoveFrom.remove(annotationToDelete.getRef());
				RelevancyOverrideSet relevancySet = objective.getCalculatedRelevantIndicatorOverrides(listToRemoveFrom);	
				CommandSetObjectData removeFromRelevancyListCommand = new CommandSetObjectData(objective.getRef(), Objective.TAG_RELEVANT_INDICATOR_SET, relevancySet.toString());
				removeFromRelevancyListCommands.add(removeFromRelevancyListCommand);
			}
		}

		return removeFromRelevancyListCommands;
	}

	public static CommandSetObjectData buildCommandToRemoveAnnotationFromObject(BaseObject owner, String annotationIdListTag, ORef refToRemove) throws ParseException
	{
		ObjectData objectData = owner.getField(annotationIdListTag);
		if (objectData.isIdListData())
			return CommandSetObjectData.createRemoveIdCommand(owner, annotationIdListTag, refToRemove.getObjectId());
		
		return CommandSetObjectData.createRemoveORefCommand(owner, annotationIdListTag, refToRemove);
	}

	public Factor getSelectedFactor()
	{
		BaseObject selected = getView().getSelectedObject();
		if(selected == null)
			return null;
		
		if(! Factor.isFactor(selected.getType()))
			return null;
		
		return (Factor)selected;
	}
	
	
	public static Vector buildCommandsToDeleteKEAIndicators(Project project, KeyEcologicalAttribute kea) throws Exception
	{
		Vector commands = new Vector();
		IdList indicatorList = kea.getIndicatorIds();
		for (int i  = 0; i < indicatorList.size(); i++)
		{
			BaseObject thisAnnotation = project.findObject(ObjectType.INDICATOR,  indicatorList.get(i));
			Command[] deleteCommands = DeleteIndicator.buildCommandsToDeleteAnnotation(project, kea, KeyEcologicalAttribute.TAG_INDICATOR_IDS, thisAnnotation);
			commands.addAll(Arrays.asList(deleteCommands));
		}

		return commands;
	}

	private static Collection buildCommandsToDeleteMeasurements(Project project, Indicator indicator) throws Exception
	{
		Vector commands = new Vector();
		CommandSetObjectData clearIndicatorMeasurements = new CommandSetObjectData(indicator.getRef(), Indicator.TAG_MEASUREMENT_REFS, new ORefList().toString());
		commands.add(clearIndicatorMeasurements);
		
		ORefList measurementRefs = indicator.getMeasurementRefs();
		for (int i  = 0; i < measurementRefs.size(); i++)
		{
			ORef measurementRef = measurementRefs.get(i);
			Measurement measurementToDelete = (Measurement) project.findObject(measurementRef);
			ORefList referrers = measurementToDelete.findObjectsThatReferToUs(Indicator.getObjectType());
			if (referrers.size() == 1)
			{
				commands.addAll(Arrays.asList(measurementToDelete.createCommandsToClear()));
				commands.add(new CommandDeleteObject(measurementRef));
			}
		}
		
		return commands;
	}
	
	private static Vector buildCommandsToDeleteMethods(Project project, Indicator indicator) throws Exception
	{
		Vector commands = new Vector();
		IdList subtaskList = indicator.getTaskIdList();
		for (int i  = 0; i < subtaskList.size(); i++)
		{
			Task methodToDelete = (Task)project.findObject(ObjectType.TASK, subtaskList.get(i));
			ORefList referrers = methodToDelete.findObjectsThatReferToUs(Indicator.getObjectType());
			if (referrers.size() == 1)
			{
				Vector returnedDeleteCommands = methodToDelete.getDeleteSelfAndSubtasksCommands(project);		
				commands.addAll(returnedDeleteCommands);
			}
		}
		
		return commands;
	}

	abstract public String[] getDialogText();
	abstract public String getAnnotationIdListTag();
	abstract public int getAnnotationType();
}
