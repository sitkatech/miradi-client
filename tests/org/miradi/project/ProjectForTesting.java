/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.project;

import org.martus.util.MultiCalendar;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.database.ProjectServer;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.ids.AssignmentId;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdList;
import org.miradi.ids.TaskId;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Task;
import org.miradi.utils.CodeList;
import org.miradi.utils.PointList;



public class ProjectForTesting extends ProjectWithHelpers
{
	public ProjectForTesting(String testName) throws Exception
	{
		this(new ProjectServerForTesting());
	}
	
	public ProjectForTesting(ProjectServer server) throws Exception
	{
		super(server);
	}
	
	public void fillProjectStartDate() throws Exception
	{
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_START_DATE, new MultiCalendar().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT, "10");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_LONGITUDE, "30");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_LATITUDE, "40");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_SCOPE, "Some project scope");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_VISION, "Some project vision");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT, "TNC planning team comment");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_LESSONS_LEARNED, "TNC lessons learned");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_COUNTRIES, createSampleCountriesCodeList().toString());

		//FIXME remove commented code after adding them as sample data
//		writeOptionalElement(out, "stressless_threat_rank", getSimpleOverallProjectRating());
//		writeOptionalElement(out, "project_threat_rank", getStressBasedOverallProjectRating());
//		writeOptionalElement(out, "project_viability_rank", getComputedTncViability());
//		writeTeamMembers(out);
//		writeEcoregionCodes(out);
//		writeCodeListElements(out, "country_code", getProjectMetadata(), ProjectMetadata.TAG_COUNTRIES);
//		writeCodeListElements(out, "ou_code", getProjectMetadata(), ProjectMetadata.TAG_TNC_OPERATING_UNITS);

	}

	private CodeList createSampleCountriesCodeList()
	{
		CodeList countriesCodeList = new CodeList();
		countriesCodeList.add("USA");
		countriesCodeList.add("BGD");
		countriesCodeList.add("AGO");
		
		return countriesCodeList;
	}

	private void fillObjectUsingCommand(ORef objectRef, String fieldTag, String data) throws Exception
	{
		CommandSetObjectData setData = new CommandSetObjectData(objectRef, fieldTag, data);
		executeCommand(setData);
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
}
