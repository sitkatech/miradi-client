/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.BooleanData;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateThreatStressRatingParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.project.Project;

public class LinkCreator
{
	public LinkCreator(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public boolean linkWasRejected(DiagramModel model, ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		if (fromFactorRef.equals(toFactorRef))
			return true;
		
		if (! model.doesFactorExist(fromFactorRef) || ! model.doesFactorExist(toFactorRef))
			return true;

		if (model.areLinked(fromFactorRef, toFactorRef))
			return true;
		
		return false;
		
	}
	
	public boolean linkWasRejected(DiagramModel model, DiagramFactorId fromDiagramFactorId, DiagramFactorId toDiagramFactorId) throws Exception
	{
		DiagramFactor fromDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
		DiagramFactor toDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
		
		return linkWasRejected(model, fromDiagramFactor, toDiagramFactor);
	}
	
	public boolean linkWasRejected(DiagramModel model, DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
	{
		if (fromDiagramFactor == null || toDiagramFactor == null)
			return true;
		
		if(fromDiagramFactor.getDiagramFactorId().equals(toDiagramFactor.getDiagramFactorId()))
		{
			String[] body = {EAM.text("Can't link an item to itself"), };
			EAM.okDialog(EAM.text("Can't Create Link"), body);
			return true;
		}
		
		if(fromDiagramFactor.getDiagramFactorId().isInvalid() || toDiagramFactor.getDiagramFactorId().isInvalid())
		{
			EAM.logWarning("Unable to Paste Link : from " + fromDiagramFactor.getDiagramFactorId() + " to OriginalId:" + toDiagramFactor.getDiagramFactorId()+" node deleted?");	
			return true;
		}

		if (! model.containsDiagramFactor(fromDiagramFactor.getDiagramFactorId()) || ! model.containsDiagramFactor(toDiagramFactor.getDiagramFactorId()))
			return true;

		if (model.areDiagramFactorsLinked(fromDiagramFactor.getDiagramFactorId(), toDiagramFactor.getDiagramFactorId()))
			return true;
		
		return false;
	}

	public void createFactorLinkAndAddToDiagramUsingCommands(DiagramObject diagramObject, FactorId fromThreatId , FactorId toTargetId ) throws Exception
	{
		DiagramFactor fromDiagramFactor = diagramObject.getDiagramFactor(fromThreatId);
		DiagramFactor toDiagramFactor = diagramObject.getDiagramFactor(toTargetId);

		createFactorLinkAndAddToDiagramUsingCommands(diagramObject, fromDiagramFactor, toDiagramFactor);
	}
	
	public FactorLinkId createFactorLinkAndAddToDiagramUsingCommands(DiagramModel model, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo) throws Exception
	{
		DiagramObject diagramObject = model.getDiagramObject();
		return (FactorLinkId) createFactorLinkAndAddToDiagramUsingCommands(diagramObject, diagramFactorFrom, diagramFactorTo).getObjectId();
	}
	
	private ORef createFactorLinkAndAddToDiagramUsingCommands(DiagramObject diagramObject, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo) throws Exception
	{
		Factor fromFactor = Factor.findFactor(getProject(), diagramFactorFrom.getWrappedORef());
		Factor toFactor = Factor.findFactor(getProject(), diagramFactorTo.getWrappedORef());
		ORef factorLinkRef = project.getFactorLinkPool().getLinkedRef(fromFactor, toFactor);
		
		if(!factorLinkRef.isInvalid())
			ensureLinkGoesOurWay(factorLinkRef, fromFactor.getFactorId());
		else
			factorLinkRef = createFactorLink(diagramFactorFrom, diagramFactorTo);
		
		createDiagramLinks(factorLinkRef);
		return factorLinkRef; 
	}

	private void ensureLinkGoesOurWay(ORef factorLinkRef, FactorId fromFactorId) throws CommandFailedException
	{
		FactorLink link = (FactorLink)project.findObject(factorLinkRef);
		if (link.isBidirectional())
			return;
		
		if(link.getFromFactorRef().getObjectId().equals(fromFactorId))
			return;
		
		enableBidirectional(link.getRef());
	}

	private void enableBidirectional(ORef factorLinkRef) throws CommandFailedException
	{
		CommandSetObjectData command = new CommandSetObjectData(factorLinkRef, FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE);
		project.executeCommand(command);
	}

	private void enableBidirectional(DiagramLink diagramLink) throws CommandFailedException
	{
		enableBidirectional(diagramLink.getWrappedRef());
	}
	
	public ORef createFactorLink(DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
	{
		ORef factorLinkRef = createFactorLinkWithPossibleThreatStressRatings(fromDiagramFactor.getWrappedORef(), toDiagramFactor.getWrappedORef());
		return factorLinkRef;
	}

	public ORef createFactorLinkWithPossibleThreatStressRatings(ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		ORef factorLinkRef = createFactorLink(fromFactorRef, toFactorRef);
		FactorLink factorLink = (FactorLink) project.findObject(factorLinkRef);
		if (factorLink.isThreatTargetLink())
			createAndAddThreatStressRatingsFromTarget(factorLinkRef, factorLink.getDownstreamTargetRef());
		
		return factorLinkRef;
	}
	
	public ORef createFactorLinkWithoutThreatStressRatings(ORef fromRef, ORef toRef) throws Exception
	{
		return createFactorLink(fromRef, toRef);
	}
	
	private ORef createFactorLink(ORef fromFactorRef, ORef toFactorRef) throws CommandFailedException
	{
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(fromFactorRef, toFactorRef);
		CommandCreateObject createFactorLink = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		project.executeCommand(createFactorLink);
		
		return createFactorLink.getObjectRef();
	}
	
	public void createAndAddThreatStressRatingsFromTarget(ORef FactorLinkRef, ORef targetRef) throws Exception
	{
		ORefList threatStressRatingRefs = new ORefList();
		Target target = (Target) project.findObject(targetRef);
		ORefList stressRefs = target.getStressRefs();
		for (int i = 0; i < stressRefs.size(); ++i)
		{			
			ORef stressRef = stressRefs.get(i);
			ORef threatStressRatingRef = createThreatStressRating(stressRef);
			threatStressRatingRefs.add(threatStressRatingRef);
		}
		
		CommandSetObjectData setThreatStressRatingRefs = new CommandSetObjectData(FactorLinkRef, FactorLink.TAG_THREAT_STRESS_RATING_REFS, threatStressRatingRefs.toString());
		project.executeCommand(setThreatStressRatingRefs);
	}

	public ORef createThreatStressRating(ORef stressRef) throws CommandFailedException
	{
		CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(stressRef);
		CommandCreateObject createThreatStressRating = new CommandCreateObject(ThreatStressRating.getObjectType(), extraInfo);
		project.executeCommand(createThreatStressRating);
		
		return createThreatStressRating.getObjectRef();
	}

	public void createDiagramLinks(ORef factorLinkRef) throws Exception
	{
		FactorLink factorLink = (FactorLink) project.findObject(factorLinkRef);
		Factor toFactor = getFactor(factorLink.getToFactorRef());
		Factor fromFactor = getFactor(factorLink.getFromFactorRef());
		
		ORefList toDiagramFactors = toFactor.findObjectsThatReferToUs(ObjectType.DIAGRAM_FACTOR);  
		ORefList fromDiagramFactors = fromFactor.findObjectsThatReferToUs(ObjectType.DIAGRAM_FACTOR);
		
		ORefList allDiagramObjects = project.getAllDiagramObjectRefs();
		for (int i = 0; i < allDiagramObjects.size(); ++i)
		{
			ORef diagramObjectORef = allDiagramObjects.get(i);
			DiagramObject diagramObject = (DiagramObject) project.findObject(diagramObjectORef);
			ORef toORef = findDiagramFactor(diagramObject, toDiagramFactors); 
			if (toORef == null)
				continue;
			
			ORef fromORef = findDiagramFactor(diagramObject, fromDiagramFactors);
			if (fromORef == null)
				continue;
			
			createDiagramLink(diagramObject, createDiagramFactorLinkParameter(fromORef, toORef, factorLinkRef));
		}
	}

	//TODO nima check to see if this method occurs else where
	private ORef findDiagramFactor(DiagramObject diagramObject, ORefList diagramFactors)
	{
		for (int i = 0 ; i < diagramFactors.size(); ++i)
		{
			ORef diagramFactorORef = diagramFactors.get(i);
			if (diagramObject.containsDiagramFactor((DiagramFactorId) diagramFactorORef.getObjectId()))
				return diagramFactorORef;
		}
		
		return null;
	}

	private Factor getFactor(ORef factorRef)
	{
		return (Factor) project.findObject(factorRef);
	}
	
	private void createDiagramLinkWithChildren(DiagramObject diagramObject, ORefList allLinkRefs, ORef fromDiagramFactorRef, ORef toDiagramFactorRef) throws Exception
	{
//		FIXME create GB link disabled
//		CreateDiagramFactorLinkParameter extraInfoWithNoFactorLink = new CreateDiagramFactorLinkParameter(fromDiagramFactorRef, toDiagramFactorRef);
//		ORef newGroupBoxDiagramLinkRef = createDiagramLink(diagramObject, extraInfoWithNoFactorLink);
//		
//		CommandSetObjectData setChildrenRefs = new CommandSetObjectData(newGroupBoxDiagramLinkRef, DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS, allLinkRefs.toString());
//		getProject().executeCommand(setChildrenRefs);
	}
	
	private ORef createDiagramLink(DiagramObject diagramObject, CreateDiagramFactorLinkParameter diagramLinkExtraInfo) throws CommandFailedException, ParseException
	{
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
		project.executeCommand(createDiagramLinkCommand);
    	
    	BaseId rawId = createDiagramLinkCommand.getCreatedId();
		DiagramFactorLinkId createdDiagramLinkId = new DiagramFactorLinkId(rawId.asInt());
		
		CommandSetObjectData addDiagramLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, createdDiagramLinkId);
		project.executeCommand(addDiagramLink);
		
		return createDiagramLinkCommand.getObjectRef();
	}

	private CreateDiagramFactorLinkParameter createDiagramFactorLinkParameter(ORef fromDiagramFactorRef, ORef toDiagramFactorRef, ORef factorlLinkRef)
	{
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(factorlLinkRef, fromDiagramFactorRef, toDiagramFactorRef);
		
		return diagramLinkExtraInfo;
	}
	
	public void createGroupBoxChildrenDiagramLinks(DiagramModel model, DiagramFactor fromDiagramFactorToUse, DiagramFactor toDiagramFactorToUse) throws Exception
	{
		ORefList allDiagramLinkRefs = new ORefList();
		ORefList fromDiagramFactorRefs = fromDiagramFactorToUse.getSelfOrChildren();
		ORefList toDiagramFactorRefs = toDiagramFactorToUse.getSelfOrChildren();
		DiagramObject diagramObject = model.getDiagramObject();
		for (int from = 0; from < fromDiagramFactorRefs.size(); ++from)
		{
			for (int to = 0; to < toDiagramFactorRefs.size(); ++to)
			{
				DiagramFactor fromDiagramFactor = DiagramFactor.find(getProject(), fromDiagramFactorRefs.get(from));
				DiagramFactor toDiagramFactor = DiagramFactor.find(getProject(), toDiagramFactorRefs.get(to));
				if (model.areLinked(fromDiagramFactor.getWrappedORef(), toDiagramFactor.getWrappedORef()))
				{
					ORef factorLinkRef = model.getLink(fromDiagramFactor.getWrappedORef(), toDiagramFactor.getWrappedORef());
					allDiagramLinkRefs.add(model.getDiagramFactorLinkByWrappedRef(factorLinkRef).getRef());
					continue;
				}
				
				ORef factorLinkRef = createFactorLinkAndAddToDiagramUsingCommands(diagramObject, fromDiagramFactor, toDiagramFactor);
				DiagramLink diagramLink = model.getDiagramFactorLinkByWrappedRef(factorLinkRef);
				allDiagramLinkRefs.add(diagramLink.getRef());
			}
		}
		if (!model.areLinked(fromDiagramFactorToUse.getRef(), toDiagramFactorToUse.getRef()))
			createDiagramLinkWithChildren(diagramObject, allDiagramLinkRefs, fromDiagramFactorToUse.getRef(), toDiagramFactorToUse.getRef());
		
		if (anyOppositeLinks(allDiagramLinkRefs, fromDiagramFactorRefs, toDiagramFactorRefs))
			enableBidirectional(allDiagramLinkRefs);
	}
	
	private void enableBidirectional(ORefList createdDiagramLinkRefs) throws Exception
	{
		for (int i = 0; i < createdDiagramLinkRefs.size(); ++i)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), createdDiagramLinkRefs.get(i));
			enableBidirectional(diagramLink);
		}
	}

	private boolean anyOppositeLinks(ORefList createdDiagramLinkRefs, ORefList fromDiagramFactorRefs, ORefList toDiagramFactorRefs)
	{
		for (int i = 0; i < createdDiagramLinkRefs.size(); ++i)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), createdDiagramLinkRefs.get(i));
			if (diagramLink.isBidirectional())
				return true;
			
			ORef toDiagramFactorRef = diagramLink.getToDiagramFactorRef();
			if (fromDiagramFactorRefs.contains(toDiagramFactorRef))
				return true;
			
			ORef fromDiagramFactorRef = diagramLink.getFromDiagramFactorRef();
			if (toDiagramFactorRefs.contains(fromDiagramFactorRef))
				return true;
		}
		
		return false;
	}

	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
