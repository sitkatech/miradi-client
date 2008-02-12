/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.FactorLinkSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;

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
		
		String tag = getAnnotationIdListTag();
		String[] dialogText = getDialogText();
		BaseObject annotationToDelete = getObjects()[0];
		BaseObject selectedFactor = getParent(annotationToDelete);
		
		deleteAnnotationViaCommands(getProject(), selectedFactor, annotationToDelete, tag, dialogText);
	}

	protected BaseObject getParent(BaseObject annotationToDelete)
	{
		return annotationToDelete.getOwner();
	}

	private void deleteAnnotationViaCommands(Project project, BaseObject owner, BaseObject annotationToDelete, String annotationIdListTag, String[] confirmDialogText) throws CommandFailedException
	{
		String[] buttons = {"Delete", "Retain", };
		if(!EAM.confirmDialog("Delete", confirmDialogText, buttons))
			return;
	
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			Command[] commands = buildCommandsToDeleteReferencedObject(project, owner, annotationIdListTag, annotationToDelete);
			for(int i = 0; i < commands.length; ++i)
				project.executeCommand(commands[i]);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
	
	public static Command[] buildCommandsToDeleteReferencedObject(Project project, BaseObject owner, String annotationIdListTag, BaseObject annotationToDelete) throws CommandFailedException, ParseException, Exception
	{
		Vector commands = new Vector();	
		commands.add(buildCommandToRemoveAnnotationFromObject(owner, annotationIdListTag, annotationToDelete.getRef()));
		commands.addAll(buildCommandsToDeleteMeasurements(project, annotationToDelete.getRef()));
		commands.addAll(buildCommandsToDeleteMethods(project, annotationToDelete.getRef()));
		commands.addAll(buildCommandsToDeleteKEAIndicators(project, annotationToDelete.getRef()));
		commands.addAll(buildCommandsToDeleteThreatStressRatings(project, owner, annotationToDelete.getRef()));
		commands.addAll(Arrays.asList(annotationToDelete.createCommandsToClear()));
		commands.add(new CommandDeleteObject(annotationToDelete.getRef()));
		
		return (Command[])commands.toArray(new Command[0]);
	}

	private static CommandSetObjectData buildCommandToRemoveAnnotationFromObject(BaseObject owner, String annotationIdListTag, ORef refToRemove) throws ParseException
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
	
	
	private static Vector buildCommandsToDeleteKEAIndicators(Project project, ORef ref) throws Exception
	{
		Vector commands = new Vector();
		if (!(ref.getObjectType() == ObjectType.KEY_ECOLOGICAL_ATTRIBUTE))
			return commands;
	
		KeyEcologicalAttribute kea = (KeyEcologicalAttribute)project.findObject(ref);
		commands.addAll(Arrays.asList(kea.createCommandsToClear()));
		
		IdList indicatorList = kea.getIndicatorIds();
		for (int i  = 0; i < indicatorList.size(); i++)
		{
			BaseObject thisAnnotation = project.findObject(ObjectType.INDICATOR,  indicatorList.get(i));
			Command[] deleteCommands = DeleteIndicator.buildCommandsToDeleteReferencedObject(project, kea, KeyEcologicalAttribute.TAG_INDICATOR_IDS, thisAnnotation);
			commands.addAll(Arrays.asList(deleteCommands));
		}

		return commands;
	}

	protected static Collection buildCommandsToDeleteThreatStressRatings(Project project, BaseObject owner, ORef stressRef) throws Exception
	{
		Vector commands = new Vector();
		if (stressRef.getObjectType() != Stress.getObjectType())
			return commands;
		
		Target target = (Target) owner;
		FactorLinkSet directThreatLinkSet = target.getThreatTargetFactorLinks();
		for(FactorLink factorLink : directThreatLinkSet)
		{
			ORef threatStressRatingStressReferrer = findThreatStressRatingReferringToStress(project, factorLink, stressRef);
			ThreatStressRating threatStressRating = (ThreatStressRating) project.findObject(threatStressRatingStressReferrer);
			commands.add(CommandSetObjectData.createRemoveORefCommand(factorLink, FactorLink.TAG_THREAT_STRESS_RATING_REFS, threatStressRatingStressReferrer));
			commands.addAll(Arrays.asList(threatStressRating.createCommandsToClear()));
			commands.add(new CommandDeleteObject(threatStressRatingStressReferrer));
		}
		
		return commands;
	}
	
	private static ORef findThreatStressRatingReferringToStress(Project project, FactorLink factorLink, ORef stressRef) throws Exception
	{
		ORefList threatStressRatingRefs = factorLink.getThreatStressRatingRefs();
		for(int i = 0; i < threatStressRatingRefs.size(); ++i)
		{
			ORef threatStressRatingRef = threatStressRatingRefs.get(i);
			ThreatStressRating threatStressRating = (ThreatStressRating) project.findObject(threatStressRatingRef);
			if (stressRef.equals(threatStressRating.getStressRef()))
				return threatStressRatingRef;
		}
		
		throw new Exception(); 
	}
	
	private static Collection buildCommandsToDeleteMeasurements(Project project, ORef ref) throws Exception
	{
		Vector commands = new Vector();
		if (ref.getObjectType() != ObjectType.INDICATOR)
			return commands;

		Indicator indicator = (Indicator)project.findObject(ref);
		CommandSetObjectData clearIndicatorMeasurements = new CommandSetObjectData(ref, Indicator.TAG_MEASUREMENT_REFS, new ORefList().toString());
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
	
	private static Vector buildCommandsToDeleteMethods(Project project, ORef ref) throws Exception
	{
		Vector commands = new Vector();
		if (ref.getObjectType() != ObjectType.INDICATOR)
			return commands;
	
		Indicator indicator = (Indicator)project.findObject(ref);
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
