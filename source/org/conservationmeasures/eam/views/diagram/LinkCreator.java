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
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.PointList;

public class LinkCreator
{
	public LinkCreator(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public boolean linkWasRejected(DiagramModel model, DiagramFactorId fromDiagramFactorId, DiagramFactorId toDiagramFactorId) throws Exception
	{
		DiagramFactor fromDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
		DiagramFactor toDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
		
		return linkWasRejected(model, fromDiagramFactor, toDiagramFactor);
	}
	
	public boolean linkWasRejected(DiagramModel model, DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
	{
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

		
		return false;
	}

	public DiagramLink createFactorLinkAndAddToDiagramUsingCommands(DiagramObject diagramObject, FactorId fromThreatId , FactorId toTargetId ) throws CommandFailedException, ParseException
	{
		DiagramFactor fromDiagramFactor = diagramObject.getDiagramFactor(fromThreatId);
		DiagramFactor toDiagramFactor = diagramObject.getDiagramFactor(toTargetId);

		return createFactorLinkAndAddToDiagramUsingCommands(diagramObject, fromDiagramFactor, toDiagramFactor);
	}
	
	public DiagramLink createFactorLinkAndAddToDiagramUsingCommands(DiagramModel model, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo, PointList bendPoints) throws Exception
	{
		DiagramObject diagramObject = model.getDiagramObject();
		
		return createFactorLinkAndAddToDiagramUsingCommands(diagramObject, diagramFactorFrom, diagramFactorTo, bendPoints);
	}
	
	public DiagramLink createFactorLinkAndAddToDiagramUsingCommands(DiagramModel model, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo) throws Exception
	{
		DiagramObject diagramObject = model.getDiagramObject();
		return createFactorLinkAndAddToDiagramUsingCommands(diagramObject, diagramFactorFrom, diagramFactorTo);
	}

	private DiagramLink createFactorLinkAndAddToDiagramUsingCommands(DiagramObject diagramObject, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo, PointList bendPoints) throws CommandFailedException, ParseException
	{
		DiagramLink diagramLink = createFactorLinkAndAddToDiagramUsingCommands(diagramObject, diagramFactorFrom, diagramFactorTo);
		CommandSetObjectData setBendPoints = CommandSetObjectData.createNewPointList(diagramLink, DiagramLink.TAG_BEND_POINTS, bendPoints);
		project.executeCommand(setBendPoints);
		
		return diagramLink;
	}
		
	private DiagramLink createFactorLinkAndAddToDiagramUsingCommands(DiagramObject diagramObject, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo) throws CommandFailedException, ParseException
	{
		FactorId fromFactorId = diagramFactorFrom.getWrappedId();
		FactorId toFactorId = diagramFactorTo.getWrappedId();
		FactorLinkId factorLinkId = project.getFactorLinkPool().getLinkedId(fromFactorId, toFactorId);
		
		if(factorLinkId != null)
			makeFactorLinkBidirectional(fromFactorId, factorLinkId);
		else
			factorLinkId = createFactorLink(fromFactorId, toFactorId);

		DiagramFactorId fromDiagramFactorId = diagramFactorFrom.getDiagramFactorId();
		DiagramFactorId toDiagramFactorId = diagramFactorTo.getDiagramFactorId();
		DiagramFactorLinkId diagramFactorLinkId = project.getDiagramFactorLinkPool().getLinkedId(fromDiagramFactorId, toDiagramFactorId);
	
		if (diagramFactorLinkId != null)
			return (DiagramLink)project.findObject(DiagramLink.getObjectType(), diagramFactorLinkId);
		
		return createDiagramLink(diagramObject, factorLinkId, fromDiagramFactorId, toDiagramFactorId);
	}

	private void makeFactorLinkBidirectional(FactorId fromFactorId, FactorLinkId factorlLinkId) throws CommandFailedException
	{
		FactorLink link = (FactorLink)project.findObject(FactorLink.getObjectType(), factorlLinkId);
		if(!link.isBidirectional() && !link.getFromFactorId().equals(fromFactorId))
		{
			CommandSetObjectData command = new CommandSetObjectData(link.getRef(), FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE);
			project.executeCommand(command);
		}
	}

	private FactorLinkId createFactorLink(FactorId fromFactorId, FactorId toFactorId) throws CommandFailedException
	{
		FactorLinkId factorlLinkId;
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(fromFactorId, toFactorId);
		CommandCreateObject createFactorLink = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		project.executeCommand(createFactorLink);
		factorlLinkId = (FactorLinkId)createFactorLink.getCreatedId();
		return factorlLinkId;
	}

	private DiagramLink createDiagramLink(DiagramObject diagramObject, FactorLinkId factorlLinkId, DiagramFactorId fromDiagramFactorId, DiagramFactorId toDiagramFactorId) throws CommandFailedException, ParseException
	{
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = createDiagramFactorLinkParameter(fromDiagramFactorId, toDiagramFactorId, factorlLinkId);
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
		project.executeCommand(createDiagramLinkCommand);
    	
    	BaseId rawId = createDiagramLinkCommand.getCreatedId();
		DiagramFactorLinkId createdDiagramLinkId = new DiagramFactorLinkId(rawId.asInt());
		
		CommandSetObjectData addDiagramLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, createdDiagramLinkId);
		project.executeCommand(addDiagramLink);
		
		return (DiagramLink) project.findObject(new ORef(ObjectType.DIAGRAM_LINK, createdDiagramLinkId));
	}

	private CreateDiagramFactorLinkParameter createDiagramFactorLinkParameter(DiagramFactorId fromId, DiagramFactorId toId, FactorLinkId factorlLinkId)
	{
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(factorlLinkId, fromId, toId);
		
		return diagramLinkExtraInfo;
	}
	
	private Project project;
}
