/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.io.File;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.ids.AssignmentId;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.ConceptualModelDiagramPool;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.utils.PointList;
import org.conservationmeasures.eam.views.diagram.DiagramModelUpdater;



public class ProjectForTesting extends Project implements CommandExecutedListener
{
	public ProjectForTesting(String testName) throws Exception
	{
		this(new ProjectServerForTesting());
	}
	
	public ProjectForTesting(ProjectServer server) throws Exception
	{
		super(server);
		String filename = getFilename();
		addCommandExecutedListener(this);
		diagramModel = new DiagramModel(this);
		getTestDatabase().openMemoryDatabase(filename);
		finishOpening();
		commandStack = new Vector();
	}
	
	public DiagramModel getDiagramModel()
	{
		return diagramModel;
	}
	
	public DiagramObject getDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}
	
	protected void finishOpening() throws Exception
	{
		super.finishOpening();
		loadDiagram();
	}

	public void close() throws Exception
	{
		super.close();
		
		diagramModel = null;
	}

	
	//TODO this is fragile, should do a true close
	public void closeAndReopen() throws Exception
	{
		File topDirectory = getDatabase().getTopDirectory();
		getTestDatabase().closeAndDontDelete();
		createOrOpen(topDirectory);
	}

	void fireCommandExecuted(Command command) 
	{
		super.fireCommandExecuted(command);
		if(commandStack != null)
			commandStack.add(command);
	}

	Command getLastCommand()
	{
		return (Command)commandStack.remove(commandStack.size()-1);
	}
	
	public ProjectServerForTesting getTestDatabase()
	{
		return (ProjectServerForTesting)getDatabase();
	}
	
	public FactorId createFactor(int objectType) throws Exception
	{
		FactorId factorId = (FactorId)createObject(objectType);
		return factorId;
	}
	
	public BaseId addItemToViewDataList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.VIEW_DATA, id,  type,  tag);
	}
	
	public BaseId addItemToProjectMetaDataList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.PROJECT_METADATA, id,  type,  tag);
	}
	
	public BaseId addItemToKeyEcologicalAttributeList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, id,  type,  tag);
	}
	
	
	public BaseId addItemToIndicatorList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.INDICATOR, id,  type,  tag);
	}
	
	
	public BaseId addItemToFactorList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.FACTOR, id,  type,  tag);
	}
	
	public BaseId addItemToList(int sourceType, BaseId id, int type, String tag) throws Exception
	{
		BaseId baseId = createObject(type);
		IdList idList = new IdList(new BaseId[] {baseId});
		setObjectData(sourceType, id, tag, idList.toString());
		return baseId;
	}

	public TaskId createTask() throws Exception
	{
		return (TaskId)createObject(ObjectType.TASK, BaseId.INVALID);
	}

	public AssignmentId createAssignment(ORef oref) throws Exception
	{
		CreateAssignmentParameter createAssignment = new CreateAssignmentParameter((TaskId)oref.getObjectId());
		AssignmentId cmAssignmentId = (AssignmentId)createObject(ObjectType.ASSIGNMENT, BaseId.INVALID, createAssignment);
		return cmAssignmentId;
	}
	
	public DiagramFactorId createAndAddFactorToDiagram(int nodeType) throws Exception
	{
		FactorCommandHelper factorHelper = new FactorCommandHelper(this, getDiagramModel());
		CommandCreateObject command = factorHelper.createFactorAndDiagramFactor(nodeType);
		
		return new DiagramFactorId(command.getCreatedId().asInt());
	}
	
	public DiagramFactor createDiagramFactorAndAddToDiagram(int objectType) throws Exception
	{
		DiagramFactorId diagramFactorId = createAndAddFactorToDiagram(objectType);
		DiagramFactor diagramFactor = (DiagramFactor) findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));

		return diagramFactor;
	}

	public FactorId createNodeAndAddToDiagram(int objectType) throws Exception
	{
		DiagramFactorId diagramFactorId = createAndAddFactorToDiagram(objectType);
		DiagramFactor diagramFactor = (DiagramFactor) findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));
	
		return diagramFactor.getWrappedId();
	}

	public FactorCell createFactorCell(int objectType) throws Exception
	{
		FactorId insertedId = createNodeAndAddToDiagram(objectType);
		return getDiagramModel().getFactorCellByWrappedId(insertedId);
	}
	
	public void loadDiagram() throws Exception
	{
		ConceptualModelDiagramPool diagramContentsPool = (ConceptualModelDiagramPool) getPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		ORefList oRefs = diagramContentsPool.getORefList();
		ConceptualModelDiagram diagramContentsObject = getDiagramContentsObject(oRefs);
		getDiagramModel().fillFrom(diagramContentsObject);
	}

	private ConceptualModelDiagram getDiagramContentsObject(ORefList oRefs) throws Exception
	{
		if (oRefs.size() == 0)
		{
			BaseId id = createObject(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
			return (ConceptualModelDiagram) findObject(new ORef(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, id));
		}
		if (oRefs.size() > 1)
		{
			EAM.logVerbose("Found more than one diagram contents inside pool");
		}

		ORef oRef = oRefs.get(0);
		return (ConceptualModelDiagram) findObject(oRef);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			DiagramModelUpdater modelUpdater = new DiagramModelUpdater(this, getDiagramModel(), getDiagramObject());
			modelUpdater.commandExecuted(event);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public LinkCell createLinkCell() throws Exception
	{
		DiagramFactorLinkId diagramLinkId = createDiagramFactorLink();
		DiagramFactorLink diagramLink = (DiagramFactorLink) findObject(new ORef(ObjectType.DIAGRAM_LINK, diagramLinkId));
		addDiagramLinkToModel(diagramLink);
		
		return getDiagramModel().getDiagramFactorLink(diagramLink);	
	}
	
	public void addDiagramLinkToModel(DiagramFactorLink diagramLink) throws Exception 
	{
		CommandSetObjectData addLink = CommandSetObjectData.createAppendIdCommand(getDiagramObject(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLink.getDiagramLinkageId());
		executeCommand(addLink);
	}
	
	public DiagramFactorLinkId createDiagramFactorLink() throws Exception
	{
		DiagramFactor from = createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		DiagramFactor to = createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		return createDiagramFactorLink(from, to);
	}
	
	public DiagramFactorLinkId createDiagramFactorLink(DiagramFactor from, DiagramFactor to) throws Exception
	{
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(from.getWrappedId(), to.getWrappedId());
		BaseId baseId = createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, parameter);
		FactorLinkId factorLinkId = new FactorLinkId(baseId.asInt());
		
		CreateDiagramFactorLinkParameter extraInfo = new CreateDiagramFactorLinkParameter(factorLinkId, from.getDiagramFactorId(), to.getDiagramFactorId());
		BaseId diagramFactorLinkId = createObject(ObjectType.DIAGRAM_LINK, extraInfo);

		return new DiagramFactorLinkId(diagramFactorLinkId.asInt());
	}
	
	public LinkCell createLinkCellWithBendPoints(PointList bendPoints) throws Exception
	{
		LinkCell linkCell = createLinkCell();
	
		CommandSetObjectData bendPointMoveCommand =	CommandSetObjectData.createNewPointList(linkCell.getDiagramFactorLink(), DiagramFactorLink.TAG_BEND_POINTS, bendPoints);
		executeCommand(bendPointMoveCommand);
		
		return linkCell;
	}
	
	public FactorId createThreat() throws Exception
	{
		FactorId threatId = createFactor(ObjectType.CAUSE);
		FactorId targetId = createFactor(ObjectType.TARGET);
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(threatId, targetId);
		createObject(ObjectType.FACTOR_LINK, BaseId.INVALID, parameter);
		
		return threatId;
	}

	DiagramModel diagramModel;
	Vector commandStack;
}
