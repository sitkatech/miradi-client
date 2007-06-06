/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.ConnectionPropertiesDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.BooleanData;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.views.ViewDoer;

public class InsertFactorLinkDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (!isDiagramView())
			return false;
		
		
		return (getDiagramView().getDiagramModel().getFactorCount() >= 2);
	}

	public void doIt() throws CommandFailedException
	{
		DiagramView diagramView = getDiagramView();
		ConnectionPropertiesDialog dialog = new ConnectionPropertiesDialog(getMainWindow(), diagramView.getDiagramPanel());
		dialog.setVisible(true);
		if(!dialog.getResult())
			return;
			
		DiagramModel model = diagramView.getDiagramModel();
		DiagramFactor fromDiagramFactor = dialog.getFrom();
		DiagramFactor toDiagramFactor = dialog.getTo();
		
		try
		{
			if (linkWasRejected(model, fromDiagramFactor, toDiagramFactor))
				return;
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			createModelLinkageAndAddToDiagramUsingCommands(model, fromDiagramFactor, toDiagramFactor);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());	
		}
	}

	public static boolean linkWasRejected(DiagramModel model, DiagramFactorId fromDiagramFactorId, DiagramFactorId toDiagramFactorId) throws Exception
	{
		Project project = model.getProject();
		DiagramFactor fromDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, fromDiagramFactorId));
		DiagramFactor toDiagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, toDiagramFactorId));
		
		return linkWasRejected(model, fromDiagramFactor, toDiagramFactor);
	}
	
	public static boolean linkWasRejected(DiagramModel model, DiagramFactor fromDiagramFactor, DiagramFactor toDiagramFactor) throws Exception
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

	public static DiagramFactorLink createModelLinkageAndAddToDiagramUsingCommands(DiagramModel model, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo, PointList bendPoints) throws Exception
	{
		DiagramObject diagramObject = model.getDiagramObject();
		Project project = model.getProject();
		
		return createModelLinkageAndAddToDiagramUsingCommands(project, diagramObject, diagramFactorFrom, diagramFactorTo, bendPoints);
	}
	
	public static DiagramFactorLink createModelLinkageAndAddToDiagramUsingCommands(DiagramModel model, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo) throws Exception
	{
		DiagramObject diagramObject = model.getDiagramObject();
		return createModelLinkageAndAddToDiagramUsingCommands(diagramObject, diagramFactorFrom, diagramFactorTo);
	}

	public static DiagramFactorLink createModelLinkageAndAddToDiagramUsingCommands(Project project, DiagramObject diagramObject, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo, PointList bendPoints) throws CommandFailedException, ParseException
	{
		DiagramFactorLink diagramLink = createModelLinkageAndAddToDiagramUsingCommands(diagramObject, diagramFactorFrom, diagramFactorTo);
		CommandSetObjectData setBendPoints = CommandSetObjectData.createNewPointList(diagramLink, DiagramFactorLink.TAG_BEND_POINTS, bendPoints);
		project.executeCommand(setBendPoints);
		
		return diagramLink;
	}
	
	public static DiagramFactorLink createModelLinkageAndAddToDiagramUsingCommands(DiagramObject diagramObject, FactorId fromThreatId , FactorId toTargetId ) throws CommandFailedException, ParseException
	{
		DiagramFactor fromDiagramFactor = diagramObject.getDiagramFactor(fromThreatId);
		DiagramFactor toDiagramFactor = diagramObject.getDiagramFactor(toTargetId);

		return InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(diagramObject, fromDiagramFactor, toDiagramFactor);
	}
	
	
	public static DiagramFactorLink createModelLinkageAndAddToDiagramUsingCommands(DiagramObject diagramObject, DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo) throws CommandFailedException, ParseException
	{
		Project project = getProject(diagramObject);
		FactorId fromFactorId = diagramFactorFrom.getWrappedId();
		FactorId toFactorId = diagramFactorTo.getWrappedId();
		FactorLinkId modelLinkageId = project.getFactorLinkPool().getLinkedId(fromFactorId, toFactorId);
		
		if(modelLinkageId != null)
			makeFactorLinkBidirectional(project, fromFactorId, modelLinkageId);
		else
			modelLinkageId = createFactorLink(project, fromFactorId, toFactorId);

		DiagramFactorId fromDiagramFactorId = diagramFactorFrom.getDiagramFactorId();
		DiagramFactorId toDiagramFactorId = diagramFactorTo.getDiagramFactorId();
		DiagramFactorLinkId diagramFactorLinkId = project.getDiagramFactorLinkPool().getLinkedId(fromDiagramFactorId, toDiagramFactorId);
	
		if (diagramFactorLinkId != null)
			return (DiagramFactorLink)project.findObject(DiagramFactorLink.getObjectType(), diagramFactorLinkId);
		
		return createDiagramLink(diagramObject, modelLinkageId, fromDiagramFactorId, toDiagramFactorId);
	}

	
	private static Project getProject(DiagramObject diagramObject)
	{
		return diagramObject.getObjectManager().getProject();
	}
	
	private static void makeFactorLinkBidirectional(Project project, FactorId fromFactorId, FactorLinkId modelLinkageId) throws CommandFailedException
	{
		FactorLink link = (FactorLink)project.findObject(FactorLink.getObjectType(), modelLinkageId);
		if(!link.isBidirectional() && !link.getFromFactorId().equals(fromFactorId))
		{
			CommandSetObjectData command = new CommandSetObjectData(link.getRef(), FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE);
			project.executeCommand(command);
		}
	}

	private static FactorLinkId createFactorLink(Project project, FactorId fromFactorId, FactorId toFactorId) throws CommandFailedException
	{
		FactorLinkId modelLinkageId;
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(fromFactorId, toFactorId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		project.executeCommand(createModelLinkage);
		modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		return modelLinkageId;
	}

	private static DiagramFactorLink createDiagramLink(DiagramObject diagramObject, FactorLinkId modelLinkageId, DiagramFactorId fromDiagramFactorId, DiagramFactorId toDiagramFactorId) throws CommandFailedException, ParseException
	{
		Project project = getProject(diagramObject);
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = createDiagramFactorLinkParameter(project, fromDiagramFactorId, toDiagramFactorId, modelLinkageId);
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
		project.executeCommand(createDiagramLinkCommand);
    	
    	BaseId rawId = createDiagramLinkCommand.getCreatedId();
		DiagramFactorLinkId createdDiagramLinkId = new DiagramFactorLinkId(rawId.asInt());
		
		CommandSetObjectData addDiagramLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, createdDiagramLinkId);
		project.executeCommand(addDiagramLink);
		
		return (DiagramFactorLink) project.findObject(new ORef(ObjectType.DIAGRAM_LINK, createdDiagramLinkId));
	}

	private static CreateDiagramFactorLinkParameter createDiagramFactorLinkParameter(Project projectToUse, DiagramFactorId fromId, DiagramFactorId toId, FactorLinkId modelLinkageId)
	{
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(modelLinkageId, fromId, toId);
		
		return diagramLinkExtraInfo;
	}
}
