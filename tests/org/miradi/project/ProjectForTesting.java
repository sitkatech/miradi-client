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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.Stress;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatStressRating;
import org.miradi.questions.HabitatAssociationQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.StressContributionQuestion;
import org.miradi.questions.StressIrreversibilityQuestion;
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
		
		String filename = getFilename();
		getTestDatabase().openMemoryDatabase(filename);
		finishOpening();
	}
	
	public void fillGeneralProjectData() throws Exception
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
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_OPERATING_UNITS, createSampleTncOperatingUnitsCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, createSampleFreshwaterEcoregionsCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, createSampleMarineEcoregionsCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, createSampleTerrestrialEcoregionsCodeList().toString());
	}
	
	public ORef createAndPopulateProjectResource() throws Exception
	{
		ORef projectResourceRef = createProjectResource().getRef();
		populateProjectResource(projectResourceRef);
		
		return projectResourceRef;
	}
	
	public Target createAndPopulateTarget() throws Exception
	{
		Target target = createTarget();
		populateTarget(target);
		return target;
	}
	
	public Stress createAndPopulateStress() throws Exception
	{
		Stress stress = createStress();
		populateStress(stress);
		return stress;
	}
	
	public Cause createAndPopulateThreat() throws Exception
	{
		Cause cause = createCause();
		populateCause(cause);
		return cause;
	}
	
	public FactorLink createAndPopulateDirectThreatLink() throws Exception
	{
		FactorLink directThreatLink = createDirectThreatLink();
		populateFactorLink(directThreatLink);

		return directThreatLink;
	}
	
	public FactorLink createAndPopulateDirectThreatLink(Target target) throws Exception
	{
		Cause threat = createAndPopulateThreat();
		ORef directThreatLinkRef = createFactorLink(threat.getRef(), target.getRef());
		FactorLink factorLink = FactorLink.find(this, directThreatLinkRef);
		populateFactorLink(factorLink);

		return factorLink;
	}
	
	public ThreatStressRating createAndPopulateThreatStressRating() throws Exception
	{
		ThreatStressRating threatStressRating = createThreatStressRating();
		populateThreatStressRating(threatStressRating);
		
		return threatStressRating;
	}
	
	public SubTarget createAndPopulateSubTarget() throws Exception
	{
		SubTarget subTarget = createSubTarget();
		populateSubTarget(subTarget);
		
		return subTarget;
	}
	
	public ProjectResource createProjectResource() throws Exception
	{
		ORef projectResourceRef = createObject(ProjectResource.getObjectType());
		return ProjectResource.find(this, projectResourceRef);
	}
	
	public Target createTarget() throws Exception
	{
		ORef targetRef = createObject(Target.getObjectType());
		return Target.find(this, targetRef);
	}
	
	public Stress createStress() throws Exception
	{
		ORef stressRef = createObject(Stress.getObjectType());
		return Stress.find(this, stressRef);
	}
	
	public Cause createCause() throws Exception
	{
		ORef threatRef = createObject(Cause.getObjectType());
		return Cause.find(this, threatRef);
	}
	
	public FactorLink createDirectThreatLink() throws Exception
	{
		Target target = createTarget();
		Cause threat = createCause();
		ORef directThreatLinkRef = createFactorLink(threat.getRef(), target.getRef());
		
		return FactorLink.find(this, directThreatLinkRef);
	}

	public ThreatStressRating createThreatStressRating() throws Exception
	{
		ORef threatStressRatingRef = createObject(ThreatStressRating.getObjectType());
		return ThreatStressRating.find(this, threatStressRatingRef);
	}
	
	public SubTarget createSubTarget() throws Exception
	{
		ORef subTargetRef = createObject(SubTarget.getObjectType());
		return SubTarget.find(this, subTargetRef);
	}

	public void populateTarget(Target target) throws Exception
	{
		fillObjectUsingCommand(target.getRef(), Target.TAG_LABEL, "Reefs");
		fillObjectUsingCommand(target.getRef(), Target.TAG_TEXT, "Some Description Text");
		fillObjectUsingCommand(target.getRef(), Target.TAG_CURRENT_STATUS_JUSTIFICATION, "Some status justification");
		fillObjectUsingCommand(target.getRef(), Target.TAG_TARGET_STATUS, StatusQuestion.VERY_GOOD);
		
		CodeList habitatCodes = new CodeList();
		habitatCodes.add(HabitatAssociationQuestion.ONE_CODE);
		habitatCodes.add(HabitatAssociationQuestion.TWO_CODE);
		fillObjectUsingCommand(target.getRef(), Target.TAG_HABITAT_ASSOCIATION, habitatCodes.toString());
		
		ORefList stressRefs = new ORefList(createAndPopulateStress().getRef());
		fillObjectUsingCommand(target.getRef(), Target.TAG_STRESS_REFS, stressRefs.toString());
	
		createAndPopulateDirectThreatLink(target);
		
		SubTarget subTarget = createAndPopulateSubTarget();
		ORefList subTargetRefs = new ORefList(subTarget.getRef());
		fillObjectUsingCommand(target.getRef(), Target.TAG_SUB_TARGET_REFS, subTargetRefs.toString());
		
		//FIXME,  finish targets
	}
	
	public void populateCause(Cause cause) throws Exception
	{
		fillObjectUsingCommand(cause.getRef(), Cause.TAG_LABEL, "SomeCauseLabel");
		
		CodeList taxonomyCodes = new CodeList("T10.10");
		fillObjectUsingCommand(cause.getRef(), Cause.TAG_TAXONOMY_CODE, taxonomyCodes.toString());
	}
	
	public void populateStress(Stress stress) throws Exception
	{
		fillObjectUsingCommand(stress.getRef(), Stress.TAG_LABEL, "SomeStressLabel");
		fillObjectUsingCommand(stress.getRef(), Stress.TAG_SEVERITY, StatusQuestion.GOOD);
		fillObjectUsingCommand(stress.getRef(), Stress.TAG_SCOPE, StatusQuestion.GOOD);
	}
	
	public void populateFactorLink(FactorLink factorLink) throws Exception
	{
		ORef threatStressRatingRef = createAndPopulateThreatStressRating().getRef();
		ORefList threatStressRatingRefs = new ORefList(threatStressRatingRef);
		fillObjectUsingCommand(factorLink.getRef(), FactorLink.TAG_THREAT_STRESS_RATING_REFS, threatStressRatingRefs.toString());
		fillObjectUsingCommand(factorLink.getRef(), FactorLink.TAG_SIMPLE_THREAT_RATING_COMMENT, "Some simple ThreatRating comment");
		fillObjectUsingCommand(factorLink.getRef(), FactorLink.TAG_COMMENT, "Some FactorLink comment");
	}
	
	public void populateThreatStressRating(ThreatStressRating threatStressRating) throws Exception
	{
		Stress stress = createAndPopulateStress();
		
		fillObjectUsingCommand(threatStressRating.getRef(), ThreatStressRating.TAG_STRESS_REF, stress.getRef().toString());
		fillObjectUsingCommand(threatStressRating.getRef(), ThreatStressRating.TAG_IRREVERSIBILITY, StressIrreversibilityQuestion.HIGH_CODE);
		fillObjectUsingCommand(threatStressRating.getRef(), ThreatStressRating.TAG_CONTRIBUTION, StressContributionQuestion.HIGH_CODE);
	}
	
	private void populateProjectResource(ORef projectResourceRef) throws Exception
	{
		CodeList roleCodes = new CodeList();
		roleCodes.add(ResourceRoleQuestion.TeamLeaderCode);
		roleCodes.add(ResourceRoleQuestion.TeamMemberRoleCode);
		
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_ROLE_CODES, roleCodes.toString());
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_LABEL, "John Doe");
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_GIVEN_NAME, "John");
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_SUR_NAME, "Doe");
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_LOCATION, "1 SomeStreet ave. Tampa FL 33600");
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_PHONE_NUMBER, "555-555-5555");
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_ORGANIZATION, "TurtleWise Corp");
	}
	
	public void populateSubTarget(SubTarget subTarget) throws Exception
	{
		fillObjectUsingCommand(subTarget.getRef(), SubTarget.TAG_LABEL, "Some SubTarget Label");
		fillObjectUsingCommand(subTarget.getRef(), SubTarget.TAG_SHORT_LABEL, "ShortL");
		fillObjectUsingCommand(subTarget.getRef(), SubTarget.TAG_DETAIL, "Some SubTarget detail text");
	}
	
	private CodeList createSampleTerrestrialEcoregionsCodeList()
	{
		CodeList terrestrialEcoregions = new CodeList();
		terrestrialEcoregions.add("10000");
		terrestrialEcoregions.add("10649");
		terrestrialEcoregions.add("10671");
		
		return terrestrialEcoregions;
	}

	private CodeList createSampleMarineEcoregionsCodeList()
	{
		CodeList marineEcoregions = new CodeList();
		marineEcoregions.add("20030");
		marineEcoregions.add("25031");
		marineEcoregions.add("20192");
		
		return marineEcoregions;
	}
	
	private CodeList createSampleFreshwaterEcoregionsCodeList()
	{
		CodeList freshwaterEcoregions = new CodeList();
		freshwaterEcoregions.add("30736");
		freshwaterEcoregions.add("30424");
		freshwaterEcoregions.add("30103");
		
		return freshwaterEcoregions;
	}
	
	private CodeList createSampleTncOperatingUnitsCodeList()
	{
		CodeList tncOperatingUnitCodes = new CodeList();
		tncOperatingUnitCodes.add("IA_US");
		tncOperatingUnitCodes.add("SC_US");
		tncOperatingUnitCodes.add("FL_US");
		
		return tncOperatingUnitCodes;
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
