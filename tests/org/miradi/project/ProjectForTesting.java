/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.project;

import java.io.File;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.database.ProjectServer;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.ids.AssignmentId;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdList;
import org.miradi.ids.TaskId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.ConceptualModelDiagramPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.Task;
import org.miradi.project.FactorCommandHelper;
import org.miradi.project.Project;
import org.miradi.utils.PointList;
import org.miradi.views.diagram.DiagramModelUpdater;



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

	protected void setDefaultDiagramPage(int objectType)
	{
		//TODO overrode to be empty, so that tests dont fail
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
	
	//TODO come up with a better name or eventualy all creates should return ref
	public ORef createFactorAndReturnRef(int objectType) throws Exception
	{
		return createObject(objectType);
	}
	
	public FactorId createFactorAndReturnId(int objectType) throws Exception
	{
		BaseId factorId = createObjectAndReturnId(objectType);
		return new FactorId(factorId.asInt());
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
	
	public BaseId addItemToGoalList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref.getObjectType(), ref.getObjectId(), ObjectType.GOAL,  tag);
	}
	
	public BaseId addItemToObjectiveList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref.getObjectType(), ref.getObjectId(), ObjectType.OBJECTIVE,  tag);
	}
	
	public BaseId addItemToIndicatorList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref.getObjectType(), ref.getObjectId(), Indicator.getObjectType(), tag);
	}
	
	public BaseId addSubtaskToActivity(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref.getObjectType(), ref.getObjectId(), Task.getObjectType(), tag);
	}
	
	public BaseId addActivityToStrateyList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref.getObjectType(), ref.getObjectId(), Task.getObjectType(), tag);
	}
	
	public BaseId addItemToIndicatorList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.INDICATOR, id,  type,  tag);
	}
	
	public BaseId addItemToTaskList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.TASK, id,  type,  tag);
	}
	
	public BaseId addItemToFactorList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.FACTOR, id,  type,  tag);
	}
	
	public BaseId addItemToList(int parentType, BaseId parentId, int typeToCreate, String tag) throws Exception
	{
		BaseObject foundObject = findObject(new ORef(parentType, parentId));
		IdList currentIdList = new IdList(typeToCreate, foundObject.getData(tag));
		
		BaseId baseId = createObjectAndReturnId(typeToCreate);
		currentIdList.add(baseId);
		setObjectData(parentType, parentId, tag, currentIdList.toString());

		return baseId;
	}

	public TaskId createTask() throws Exception
	{
		return (TaskId)createObject(ObjectType.TASK, BaseId.INVALID);
	}

	public AssignmentId createAssignment(ORef oref) throws Exception
	{
		AssignmentId cmAssignmentId = (AssignmentId)createObject(ObjectType.ASSIGNMENT, BaseId.INVALID);
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
			BaseId id = createObjectAndReturnId(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
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
		if (! event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;

		try
		{
			CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
			DiagramModelUpdater modelUpdater = new DiagramModelUpdater(this, getDiagramModel());
			modelUpdater.commandSetObjectDataWasExecuted(setCommand);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public LinkCell createLinkCell() throws Exception
	{
		BaseId diagramLinkId = createDiagramFactorLink();
		DiagramLink diagramLink = (DiagramLink) findObject(new ORef(ObjectType.DIAGRAM_LINK, diagramLinkId));
		addDiagramLinkToModel(diagramLink);
		
		return getDiagramModel().getDiagramFactorLink(diagramLink);	
	}
	
	public void addDiagramLinkToModel(DiagramLink diagramLink) throws Exception 
	{
		 addDiagramLinkToModel(diagramLink.getDiagramLinkageId());
	}
	
	public void addDiagramLinkToModel(BaseId diagramLinkId) throws Exception
	{
		CommandSetObjectData addLink = CommandSetObjectData.createAppendIdCommand(getDiagramObject(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkId);
		executeCommand(addLink);
	}
	
	public BaseId createDiagramFactorLink() throws Exception
	{
		return createDiagramLink().getObjectId();
	}

	public ORef createDiagramLink() throws Exception
	{
		DiagramFactor from = createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		DiagramFactor to = createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		return createDiagramLink(from, to);
	}

	public ORef createDiagramLinkAndAddToDiagram(DiagramFactor from, DiagramFactor to) throws Exception
	{
		ORef linkRef = createDiagramLink(from, to);
		
		IdList links = getDiagramObject().getAllDiagramFactorLinkIds();
		links.add(linkRef.getObjectId());
		getDiagramObject().setData(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, links.toString());
		return linkRef;
	}
	
	public BaseId createDiagramFactorLink(DiagramFactor from, DiagramFactor to) throws Exception
	{
		return createDiagramLink(from, to).getObjectId();
	}
	
	public ORef createDiagramLink(DiagramFactor from, DiagramFactor to) throws Exception
	{
		BaseId baseId = createFactorLink(from.getWrappedORef(), to.getWrappedORef()).getObjectId();
		FactorLinkId factorLinkId = new FactorLinkId(baseId.asInt());
		
		CreateDiagramFactorLinkParameter extraInfo = new CreateDiagramFactorLinkParameter(factorLinkId, from.getDiagramFactorId(), to.getDiagramFactorId());

		return createObjectAndReturnRef(ObjectType.DIAGRAM_LINK, extraInfo);
	}

	public ORef createFactorLink(ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(fromFactorRef, toFactorRef);
		return createObjectAndReturnRef(ObjectType.FACTOR_LINK, parameter);
	}

	public LinkCell createLinkCellWithBendPoints(PointList bendPoints) throws Exception
	{
		LinkCell linkCell = createLinkCell();
	
		CommandSetObjectData createBendPointsCommand =	CommandSetObjectData.createNewPointList(linkCell.getDiagramLink(), DiagramLink.TAG_BEND_POINTS, bendPoints);
		executeCommand(createBendPointsCommand);
		
		return linkCell;
	}
	
	public FactorId createThreat() throws Exception
	{
		ORef factorLinkRef = createThreatTargetLink();
		FactorLink factorLink = FactorLink.find(this, factorLinkRef);
		
		return (FactorId) factorLink.getFromFactorRef().getObjectId();
	}
	
	public ORef createThreatTargetLink() throws Exception
	{
		DiagramFactor threat = createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		DiagramFactor target = createDiagramFactorAndAddToDiagram(ObjectType.TARGET);
		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(threat.getWrappedORef(), target.getWrappedORef());
		
		return createObjectAndReturnRef(ObjectType.FACTOR_LINK, parameter);
	}
	
	public ORef creatThreatTargetBidirectionalLink() throws Exception
	{
		ORef factorLinkRef = createThreatTargetLink();
		CommandSetObjectData setBidirectionality = new CommandSetObjectData(factorLinkRef, FactorLink.TAG_BIDIRECTIONAL_LINK, BooleanData.BOOLEAN_TRUE);
		executeCommand(setBidirectionality);
		
		return factorLinkRef;
	}

	private DiagramModel diagramModel;
	private Vector commandStack;
}
