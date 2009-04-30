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
package org.miradi.project;

import org.martus.util.MultiCalendar;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.database.ProjectServer;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdList;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProgressPercent;
import org.miradi.objects.ProgressReport;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.SubTarget;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.questions.BudgetCostModeQuestion;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.HabitatAssociationQuestion;
import org.miradi.questions.IndicatorStatusRatingQuestion;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProgressReportStatusQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.questions.StressContributionQuestion;
import org.miradi.questions.StressIrreversibilityQuestion;
import org.miradi.questions.ThreatClassificationQuestion;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.questions.TncOrganizationalPrioritiesQuestion;
import org.miradi.questions.TncProjectPlaceTypeQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateRangeEffort;
import org.miradi.utils.PointList;
import org.miradi.utils.Translation;
import org.miradi.xml.conpro.ConProMiradiXml;



public class ProjectForTesting extends ProjectWithHelpers
{
	public ProjectForTesting(String testName) throws Exception
	{
		this(testName, new ProjectServerForTesting());
	}
	
	public ProjectForTesting(String testName, ProjectServer server) throws Exception
	{
		super(server);
		Translation.initialize();
		
		getDatabase().setMemoryDataLocation("Memory");
		getDatabase().createProject(testName);
		finishOpening();
	}
	
	@Override
	public void close() throws Exception
	{
		super.close();
	}
	
	public void fillGeneralProjectData() throws Exception
	{
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_NAME, "Some Project Name");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_START_DATE, new MultiCalendar().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_SIZE_IN_HECTARES, "10000");
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
		
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, createConproXenodata());
		
		ORef tncProjectDataRef = getSingletonObjectRef(TncProjectData.getObjectType());
		CodeList organizationalPriorityCodes = new CodeList();
		organizationalPriorityCodes.add(TncOrganizationalPrioritiesQuestion.CAPITAL_CAMPAIGN_CODE);
		organizationalPriorityCodes.add(TncOrganizationalPrioritiesQuestion.CSD_PROTECTED_AREAS_CODE);
		fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES, organizationalPriorityCodes.toString());
		
		CodeList projectTypes = new CodeList();
		projectTypes.add(TncProjectPlaceTypeQuestion.MULTI_PLACE_BASED_PROJECT_CODE);
		projectTypes.add(TncProjectPlaceTypeQuestion.NON_PLACE_BASED_PROJECT_CODE);
		fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_PROJECT_PLACE_TYPES, projectTypes.toString());
	}
	
	private String createConproXenodata() throws Exception
	{
		ORef xenodataRef1 = createAndPopulateXenodata("1").getRef();
		ORef xenodataRef2 = createAndPopulateXenodata("2").getRef();
		StringRefMap refMap = new StringRefMap();
		refMap.add(ConProMiradiXml.CONPRO_CONTEXT, xenodataRef1);
		refMap.add("randomContext", xenodataRef2);
		
		return refMap.toString();
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
		enableAsThreat(cause);
		return cause;
	}
	
	public FactorLink createAndPopulateDirectThreatLink() throws Exception
	{
		Target target = createAndPopulateTarget();
		Cause threat = createAndPopulateThreat();
		ORef directThreatLinkRef = createFactorLink(threat.getRef(), target.getRef());
		FactorLink directThreatLink = FactorLink.find(this, directThreatLinkRef);
		populateDirectThreatLink(directThreatLink, target.getStressRefs());

		return directThreatLink;
	}
	
	public FactorLink createAndPopulateDirectThreatLink(Target target) throws Exception
	{
		Cause threat = createAndPopulateThreat();
		ORef directThreatLinkRef = createFactorLink(threat.getRef(), target.getRef());
		FactorLink factorLink = FactorLink.find(this, directThreatLinkRef);
		populateDirectThreatLink(factorLink, target.getStressRefs());

		return factorLink;
	}
	
	public ThreatStressRating createAndPopulateThreatStressRating() throws Exception
	{
		ThreatStressRating threatStressRating = createThreatStressRating();
		populateThreatStressRating(threatStressRating);
		
		return threatStressRating;
	}
	
	public ThreatStressRating createAndPopulateThreatStressRating(ORef stressRef, ORef threatRef) throws Exception
	{
		ThreatStressRating threatStressRating = createThreatStressRating(stressRef, threatRef);
		populateThreatStressRating(threatStressRating);
		
		return threatStressRating;
	}
	
	public SubTarget createAndPopulateSubTarget() throws Exception
	{
		SubTarget subTarget = createSubTarget();
		populateSubTarget(subTarget);
		
		return subTarget;
	}
	
	public KeyEcologicalAttribute createAndPopulateKea() throws Exception
	{
		KeyEcologicalAttribute kea = createKea();
		populateKea(kea);
		
		return kea;
	}
	
	public Indicator createAndPopulateIndicator() throws Exception
	{
		Indicator indicator = createIndicator();
		populateIndicator(indicator);
		
		return indicator;
	}
	
	public Task createAndPopulateTask(String customTaskLabel) throws Exception
	{
		Task task  = createTask();
		populateTask(task, customTaskLabel);
		
		return task;
	}
	
	public Measurement createAndPopulateMeasurement() throws Exception
	{
		Measurement measurement = createMeasurement();
		populateMeasurement(measurement);
		
		return measurement;
	}
	
	public Objective createAndPopulateObjective() throws Exception
	{
		Objective objective = createObjective();
		populateObjective(objective);
		
		return objective;
	}
	
	public Strategy createAndPopulateStrategy() throws Exception
	{
		Strategy strategy = createStrategy();
		populateStrategy(strategy);
		
		return strategy;
	}

	public Strategy createAndPopulateDraftStrategy() throws Exception
	{
		Strategy strategy = createAndPopulateStrategy();
		fillObjectUsingCommand(strategy, Strategy.TAG_STATUS, Strategy.STATUS_DRAFT);
		
		return strategy;
	}
	
	public ProgressReport createAndPopulateProgressReport() throws Exception
	{
		ProgressReport progressReport = createProgressReport();
		populateProgressReport(progressReport);
		
		return progressReport;
	}
	
	public ProgressPercent createAndPopulateProgressPercent() throws Exception
	{
		ProgressPercent progressPercent = createProgressPercent();
		populateProgressPercent(progressPercent);
		
		return progressPercent;
	}
	
	public Xenodata createAndPopulateXenodata(String xenoDataProjectId) throws Exception
	{
		Xenodata xenodata = createXenodata();
		populateXenodata(xenodata, xenoDataProjectId);
		
		return xenodata;
	}
	
	public DiagramFactor createAndPopulateDiagramFactorGroupBox(DiagramFactor groupBoxChild) throws Exception
	{
		DiagramFactor diagramFactorGroupBox = createDiagramFactorAndAddToDiagram(GroupBox.getObjectType());
		populateDiagramFactorGroupBox(diagramFactorGroupBox, groupBoxChild);
		
		return diagramFactorGroupBox;	 
	}
	
	public ProjectResource createProjectResource() throws Exception
	{
		ORef projectResourceRef = createObject(ProjectResource.getObjectType());
		return ProjectResource.find(this, projectResourceRef);
	}
	
	public Target createTarget() throws Exception
	{
		ORef targetRef = createObject(Target.getObjectType(), new FactorId(takeNextId(Target.getObjectType())));
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
		Target target = createAndPopulateTarget();
		Cause threat = createAndPopulateThreat();
		
		ORef directThreatLinkRef = createFactorLink(threat.getRef(), target.getRef());
		
		return FactorLink.find(this, directThreatLinkRef);
	}

	public ThreatStressRating createThreatStressRating() throws Exception
	{
		Stress stress = createAndPopulateStress();
		Cause threat = createAndPopulateThreat();
		return createThreatStressRating(stress.getRef(), threat.getRef());
	}

	public ThreatStressRating createThreatStressRating(ORef stressRef, ORef threatRef) throws Exception
	{
		CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(stressRef, threatRef);
		ORef threatStressRatingRef = createObject(ThreatStressRating.getObjectType(), extraInfo);
		
		setObjectData(threatStressRatingRef, ThreatStressRating.TAG_IS_ACTIVE, BooleanData.BOOLEAN_TRUE);
		return ThreatStressRating.find(this, threatStressRatingRef);
	}
	
	public SubTarget createSubTarget() throws Exception
	{
		ORef subTargetRef = createObject(SubTarget.getObjectType());
		return SubTarget.find(this, subTargetRef);
	}
	
	public KeyEcologicalAttribute createKea() throws Exception
	{
		ORef keaRef = createObject(KeyEcologicalAttribute.getObjectType());
		return KeyEcologicalAttribute.find(this, keaRef);
	}
	
	public Indicator createIndicator() throws Exception
	{
		ORef indicatorRef = createObject(Indicator.getObjectType());
		return Indicator.find(this, indicatorRef);
	}
	
	public BaseObject createBaseObject(int objectType) throws Exception
	{
		ORef baseObjectRef = createObject(objectType);
		return BaseObject.find(this, baseObjectRef);
	}
	
	public Task createTaskWithWho() throws Exception
	{
		Task task = createAndPopulateTask("some label");
		ORef projectResourceRef = createAndPopulateProjectResource();
		ORefList projectResourceRefs = new ORefList(projectResourceRef);
		fillObjectUsingCommand(task, Task.TAG_WHO_OVERRIDE_REFS, projectResourceRefs.toString());
		
		return task;
	}
	
	public Task createTask() throws Exception
	{
		ORef taskRef = createObject(Task.getObjectType());
		return Task.find(this, taskRef);
	}
	
	public Measurement createMeasurement() throws Exception
	{
		ORef measurementRef = createObject(Measurement.getObjectType());
		return Measurement.find(this, measurementRef);
	}
	
	public Objective createObjective() throws Exception
	{
		ORef objectiveRef = createObject(Objective.getObjectType());
		return Objective.find(this, objectiveRef);
	}
	
	public Strategy createStrategy() throws Exception
	{
		ORef strategyRef = createObject(Strategy.getObjectType(), new FactorId(takeNextId(Strategy.getObjectType())));
		return Strategy.find(this, strategyRef);
	}
	
	public ProgressReport createProgressReport() throws Exception
	{
		ORef progressReportRef = createObject(ProgressReport.getObjectType());
		return ProgressReport.find(this, progressReportRef);
	}
	
	public ProgressPercent createProgressPercent() throws Exception
	{
		ORef progressPercentRef = createObject(ProgressPercent.getObjectType());
		return ProgressPercent.find(this, progressPercentRef);
	}
	
	private Xenodata createXenodata() throws Exception
	{
		ORef xenodataRef = createObject(Xenodata.getObjectType());
		return Xenodata.find(this, xenodataRef);
	}
	
	public TaggedObjectSet createTaggedObjectSet() throws Exception
	{
		ORef taggedObjectSetRef = createObject(TaggedObjectSet.getObjectType());
		return TaggedObjectSet.find(this, taggedObjectSetRef);
	}
	
	public TaggedObjectSet createLabeledTaggedObjectSet(String labelToUse) throws Exception
	{
		TaggedObjectSet taggedObjectSet = createTaggedObjectSet();
		setObjectData(taggedObjectSet.getRef(), TaggedObjectSet.TAG_LABEL, labelToUse);
		
		return taggedObjectSet;
	}
	
	public Assignment createAssignment() throws Exception
	{
		ORef assignmentRef = createObject(Assignment.getObjectType());
		return Assignment.find(this, assignmentRef);
	}
	
	public void populateTarget(Target target) throws Exception
	{
		fillObjectUsingCommand(target, Target.TAG_LABEL, "Reefs " + target.getId().toString());
		fillObjectUsingCommand(target, Target.TAG_TEXT, "Some Description Text");
		fillObjectUsingCommand(target, Target.TAG_COMMENT, "Some comment Text");
		fillObjectUsingCommand(target, Target.TAG_CURRENT_STATUS_JUSTIFICATION, "Some status justification");
		fillObjectUsingCommand(target, Target.TAG_TARGET_STATUS, StatusQuestion.VERY_GOOD);
		fillObjectUsingCommand(target, Target.TAG_VIABILITY_MODE, ViabilityModeQuestion.TNC_STYLE_CODE);
				
		CodeList habitatCodes = new CodeList();
		habitatCodes.add(HabitatAssociationQuestion.FOREST_CODE);
		habitatCodes.add(HabitatAssociationQuestion.SAVANNA_CODE);
		fillObjectUsingCommand(target, Target.TAG_HABITAT_ASSOCIATION, habitatCodes.toString());
		
		ORefList stressRefs = new ORefList(createAndPopulateStress().getRef());
		fillObjectUsingCommand(target, Target.TAG_STRESS_REFS, stressRefs.toString());
	
		createAndPopulateDirectThreatLink(target);
		
		SubTarget subTarget = createAndPopulateSubTarget();
		ORefList subTargetRefs = new ORefList(subTarget.getRef());
		fillObjectUsingCommand(target, Target.TAG_SUB_TARGET_REFS, subTargetRefs.toString());
		
		KeyEcologicalAttribute kea = createAndPopulateKea();
		IdList keaIds = new IdList(KeyEcologicalAttribute.getObjectType());
		keaIds.addRef(kea.getRef());
		fillObjectUsingCommand(target, Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keaIds.toString());
	}
	
	public void populateCause(Cause cause) throws Exception
	{
		fillObjectUsingCommand(cause.getRef(), Cause.TAG_LABEL, "SomeCauseLabel");
		
		ChoiceQuestion question = getQuestion(ThreatClassificationQuestion.class);
		final int FIRST_CODE = 0;
		fillObjectUsingCommand(cause.getRef(), Cause.TAG_TAXONOMY_CODE, question.getCode(FIRST_CODE));
	}
	
	public void populateStress(Stress stress) throws Exception
	{
		fillObjectUsingCommand(stress, Stress.TAG_LABEL, "SomeStressLabel");
		fillObjectUsingCommand(stress, Stress.TAG_SEVERITY, StatusQuestion.GOOD);
		fillObjectUsingCommand(stress, Stress.TAG_SCOPE, StatusQuestion.GOOD);
	}
	
	public void populateDirectThreatLink(FactorLink directThreatLink, ORefList stressRefs) throws Exception
	{
		for (int refIndex = 0; refIndex < stressRefs.size(); ++refIndex)
		{
			ORef threatRef = getUpstreamThreatRef(directThreatLink);
			createAndPopulateThreatStressRating(stressRefs.get(refIndex), threatRef).getRef();	
		}
	}
	
	public void populateThreatStressRating(ThreatStressRating threatStressRating) throws Exception
	{
		fillObjectUsingCommand(threatStressRating, ThreatStressRating.TAG_IS_ACTIVE, BooleanData.BOOLEAN_TRUE);
		fillObjectUsingCommand(threatStressRating, ThreatStressRating.TAG_IRREVERSIBILITY, StressIrreversibilityQuestion.HIGH_CODE);
		fillObjectUsingCommand(threatStressRating, ThreatStressRating.TAG_CONTRIBUTION, StressContributionQuestion.HIGH_CODE);
	}
	
	private void populateProjectResource(ORef projectResourceRef) throws Exception
	{
		CodeList roleCodes = new CodeList();
		roleCodes.add(ResourceRoleQuestion.TeamLeaderCode);
		roleCodes.add(ResourceRoleQuestion.TeamMemberRoleCode);
		
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_ROLE_CODES, roleCodes.toString());
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_LABEL, PROJECT_RESOURCE_LABEL_TEXT);
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_GIVEN_NAME, "John");
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_SUR_NAME, "Doe");
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_LOCATION, "1 SomeStreet ave. Tampa FL 33600");
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_PHONE_NUMBER, "555-555-5555");
		fillObjectUsingCommand(projectResourceRef, ProjectResource.TAG_ORGANIZATION, "TurtleWise Corp");
	}
	
	public void populateSubTarget(SubTarget subTarget) throws Exception
	{
		fillObjectUsingCommand(subTarget, SubTarget.TAG_LABEL, "Some SubTarget Label");
		fillObjectUsingCommand(subTarget, SubTarget.TAG_SHORT_LABEL, "ShortL");
		fillObjectUsingCommand(subTarget, SubTarget.TAG_DETAIL, "Some SubTarget detail text");
	}
	
	public void populateKea(KeyEcologicalAttribute kea) throws Exception
	{
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_LABEL, "Some Kea Label");
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_SHORT_LABEL, "kea");
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, KeyEcologicalAttributeTypeQuestion.CONDITION);
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_DETAILS, "Some kea details text");
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_DESCRIPTION, "Some kea description text");
		
		IdList indicatorIds = new IdList(Indicator.getObjectType());
		indicatorIds.add(createAndPopulateIndicator().getId());
		indicatorIds.add(createAndPopulateIndicator().getId());
		Indicator indicatorWithoutThreshold = createAndPopulateIndicator();
		fillObjectUsingCommand(indicatorWithoutThreshold, Indicator.TAG_INDICATOR_THRESHOLD, "");
		indicatorIds.add(indicatorWithoutThreshold.getId());
		
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_INDICATOR_IDS, indicatorIds.toString());
	}
	
	public void populateIndicator(Indicator indicator) throws Exception
	{
		fillObjectUsingCommand(indicator, Indicator.TAG_LABEL, "Some Indicator Label");
		fillObjectUsingCommand(indicator, Indicator.TAG_PRIORITY, PriorityRatingQuestion.HIGH_CODE);
		fillObjectUsingCommand(indicator, Indicator.TAG_DETAIL, "Some Indicator detail");
		fillObjectUsingCommand(indicator, Indicator.TAG_VIABILITY_RATINGS_COMMENT, "Some Indicator viability ratings comment");
		fillObjectUsingCommand(indicator, Indicator.TAG_STATUS, IndicatorStatusRatingQuestion.GOING_WELL_CODE);
		
		Task task = createAndPopulateTask("Some Method Name");
		IdList taskIds = new IdList(Task.getObjectType());
		taskIds.addRef(task.getRef());
		fillObjectUsingCommand(indicator, Indicator.TAG_METHOD_IDS, taskIds.toString());
		
		Measurement measurement = createAndPopulateMeasurement();
		ORefList measurementRefs = new ORefList(measurement.getRef());
		fillObjectUsingCommand(indicator, Indicator.TAG_MEASUREMENT_REFS, measurementRefs.toString());
		
		ProgressReport progressReport = createAndPopulateProgressReport();
		ORefList progressReportRefs = new ORefList(progressReport.getRef());
		fillObjectUsingCommand(indicator, Indicator.TAG_PROGRESS_REPORT_REFS, progressReportRefs.toString());
		
		StringMap threshold = new StringMap();
		threshold.add(StatusQuestion.POOR, "poor text");
		threshold.add(StatusQuestion.FAIR, "fair text");
		threshold.add(StatusQuestion.GOOD, "good text");
		threshold.add(StatusQuestion.VERY_GOOD, "very good text");
		fillObjectUsingCommand(indicator, Indicator.TAG_INDICATOR_THRESHOLD, threshold.toString());
		
		fillObjectUsingCommand(indicator, Indicator.TAG_FUTURE_STATUS_RATING, StatusQuestion.GOOD);
		fillObjectUsingCommand(indicator, Indicator.TAG_FUTURE_STATUS_DATE, "2020-01-23");
		fillObjectUsingCommand(indicator, Indicator.TAG_FUTURE_STATUS_COMMENT, "Some Indicator future status comment");
		fillObjectUsingCommand(indicator, Indicator.TAG_BUDGET_COST_MODE, BudgetCostModeQuestion.OVERRIDE_MODE_CODE);
		fillObjectUsingCommand(indicator, Indicator.TAG_BUDGET_COST_OVERRIDE, Double.toString(444.44));
		fillObjectUsingCommand(indicator, Indicator.TAG_COMMENT, "Some indicator Comment");
	}
	
	public void populateTask(Task task, String customLabel) throws Exception
	{
		fillObjectUsingCommand(task, Task.TAG_LABEL, customLabel);
		MultiCalendar startDate = MultiCalendar.createFromGregorianYearMonthDay(2000, 03, 19);
		MultiCalendar endDate = MultiCalendar.createFromGregorianYearMonthDay(2010, 3, 19);
		fillObjectUsingCommand(task, Task.TAG_BUDGET_COST_MODE, BudgetCostModeQuestion.OVERRIDE_MODE_CODE);
		fillObjectUsingCommand(task, Task.TAG_WHEN_OVERRIDE, new DateRange(startDate, endDate).toJson().toString());
	}
	
	public void populateMeasurement(Measurement measurement) throws Exception
	{
		fillObjectUsingCommand(measurement, Measurement.TAG_STATUS, StatusQuestion.GOOD);
		fillObjectUsingCommand(measurement, Measurement.TAG_DATE, "2007-03-19");
		fillObjectUsingCommand(measurement, Measurement.TAG_STATUS_CONFIDENCE, StatusConfidenceQuestion.ROUGH_GUESS_CODE);
		fillObjectUsingCommand(measurement, Measurement.TAG_COMMENT, "Some Measurement comment");
	}
	
	public void populateObjective(Objective objective) throws Exception
	{
		fillObjectUsingCommand(objective, Objective.TAG_SHORT_LABEL, "123");
		fillObjectUsingCommand(objective, Objective.TAG_LABEL, "Some Objective label");
		fillObjectUsingCommand(objective, Objective.TAG_FULL_TEXT, "Some objective full text data");
		fillObjectUsingCommand(objective, Objective.TAG_COMMENTS, "Some Objective comments");
		
		ORef relevantIndicatorRef = createAndPopulateIndicator().getRef();
		RelevancyOverrideSet relevantIndicators = new RelevancyOverrideSet();
		relevantIndicators.add(new RelevancyOverride(relevantIndicatorRef, true));
		fillObjectUsingCommand(objective, Objective.TAG_RELEVANT_INDICATOR_SET, relevantIndicators.toString());
		
		ProgressPercent populatedProgressPercent = createAndPopulateProgressPercent();
		ProgressPercent emptyProgressPercent = createProgressPercent();
		ORefList progressPercentRefs = new ORefList();
		progressPercentRefs.add(populatedProgressPercent.getRef());
		progressPercentRefs.add(emptyProgressPercent.getRef());
		fillObjectUsingCommand(objective, Objective.TAG_PROGRESS_PERCENT_REFS, progressPercentRefs.toString());
	}
	
	public void populateStrategy(Strategy strategy) throws Exception
	{
		fillObjectUsingCommand(strategy, Strategy.TAG_LABEL, "Some Strategy label");
		fillObjectUsingCommand(strategy, Strategy.TAG_COMMENT, "Some Strategy comments");
		
		final int FIRST_CODE = 0;
		ChoiceQuestion question = getQuestion(StrategyTaxonomyQuestion.class);
		fillObjectUsingCommand(strategy, Strategy.TAG_TAXONOMY_CODE, question.getCode(FIRST_CODE));
		fillObjectUsingCommand(strategy, Strategy.TAG_IMPACT_RATING, StrategyImpactQuestion.HIGH_CODE);
		fillObjectUsingCommand(strategy, Strategy.TAG_FEASIBILITY_RATING, StrategyFeasibilityQuestion.LOW_CODE);
		
		IdList activityIds = new IdList(Task.getObjectType());
		activityIds.addRef(createAndPopulateTask("Some activity Label").getRef());
		fillObjectUsingCommand(strategy, Strategy.TAG_ACTIVITY_IDS, activityIds.toString());
		
		IdList objectiveIds = new IdList(Objective.getObjectType());
		objectiveIds.addRef(createAndPopulateObjective().getRef());
		objectiveIds.addRef(createAndPopulateObjective().getRef());
		fillObjectUsingCommand(strategy, Strategy.TAG_OBJECTIVE_IDS, objectiveIds.toString());
		
		fillObjectUsingCommand(strategy, Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING, "good, tnc legacy strategy rating");
	}
	
	public void populateProgressReport(ProgressReport progressReport) throws Exception
	{
		fillObjectUsingCommand(progressReport, ProgressReport.TAG_PROGRESS_DATE, "2008-01-23");
		fillObjectUsingCommand(progressReport, ProgressReport.TAG_PROGRESS_STATUS, ProgressReportStatusQuestion.PLANNED_CODE);
	}
	
	public void populateProgressPercent(ProgressPercent progressPercent) throws Exception
	{
		fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_DATE, "2009-01-23");
		fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_PERCENT_COMPLETE, "21");
		fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_PERCENT_COMPLETE_NOTES, "some percent complete notes");
	}
	
	public void populateXenodata(Xenodata xenodata, String xenoDataProjectId) throws Exception
	{
		setObjectData(xenodata.getRef(), Xenodata.TAG_PROJECT_ID, xenoDataProjectId);
	}
	
	public void populateDiagramFactorGroupBox(DiagramFactor groupBoxDiagramFactor, DiagramFactor groupBoxChild) throws Exception
	{
		ORefList groupBoxChildren = new ORefList(groupBoxChild);
		fillObjectUsingCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, groupBoxChildren.toString());
	}
	
	public void populateThreatRatingCommentsData(ThreatRatingCommentsData threatRatingCommentsData, ORef threatRef, ORef targetRef) throws Exception
	{
		String threatTargetKey = ThreatRatingCommentsData.createKey(threatRef, targetRef);
				
		StringMap simpleThreatRatingCommentsMap = threatRatingCommentsData.getSimpleThreatRatingCommentsMap();
		simpleThreatRatingCommentsMap.add(threatTargetKey, SIMPLE_THREAT_RATING_COMMENT);
		fillObjectUsingCommand(threatRatingCommentsData, ThreatRatingCommentsData.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP, simpleThreatRatingCommentsMap.toString());
	
		StringMap stressBasedThreatRatingCommentsMap = threatRatingCommentsData.getStressBasedThreatRatingCommentsMap();
		stressBasedThreatRatingCommentsMap.add(threatTargetKey, STRESS_BASED_THREAT_RATING_COMMENT);
		fillObjectUsingCommand(threatRatingCommentsData, ThreatRatingCommentsData.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP, stressBasedThreatRatingCommentsMap.toString());
	}
	
	public void populateEverything() throws Exception
	{
		switchToStressBaseMode();
		fillGeneralProjectData();
		createAndPopulateDirectThreatLink();
		createAndPopulateIndicator();
		createAndPopulateKea();
		createAndPopulateMeasurement();
		createAndPopulateProjectResource();
		createAndPopulateStress();
		createAndPopulateSubTarget();
		createAndPopulateTarget();
		createAndPopulateTask("Some Task Label");
		createAndPopulateThreat();
		createAndPopulateObjective();
		createAndPopulateDraftStrategy();
		createAndPopulateStrategy();
		createAndPopulateStrategyThreatTargetAssociation();
		
	}

	public void switchToStressBaseMode() throws Exception
	{
		setObjectData(getMetadata().getRef(), ProjectMetadata.TAG_THREAT_RATING_MODE, ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
	}
	
	public void createAndPopulateStrategyThreatTargetAssociation() throws Exception
	{
		FactorLink factorLink = createAndPopulateDirectThreatLink();
		ORef threatRef = getUpstreamThreatRef(factorLink);
		Strategy strategy = createAndPopulateStrategy();
		createFactorLink(threatRef, strategy.getRef());
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

	public void fillObjectUsingCommand(BaseObject object, String fieldTag, String data) throws Exception
	{
		fillObjectUsingCommand(object.getRef(), fieldTag, data);
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
		return addItemToList(new ORef(ObjectType.VIEW_DATA, id),  type,  tag);
	}
	
	public BaseId addItemToProjectMetaDataList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(new ORef(ObjectType.PROJECT_METADATA, id),  type,  tag);
	}
	
	public BaseId addItemToKeyEcologicalAttributeList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(new ORef(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, id),  type,  tag);
	}
	
	public BaseId addItemToGoalList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref, ObjectType.GOAL,  tag);
	}
	
	public BaseId addItemToObjectiveList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref, ObjectType.OBJECTIVE,  tag);
	}
	
	public BaseId addItemToIndicatorList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref, Indicator.getObjectType(), tag);
	}
	
	public BaseId addSubtaskToActivity(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref, Task.getObjectType(), tag);
	}
	
	public BaseId addActivityToStrateyList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref, Task.getObjectType(), tag);
	}
	
	public BaseId addItemToIndicatorList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(new ORef(ObjectType.INDICATOR, id),  type,  tag);
	}
	
	public BaseId addItemToTaskList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(new ORef(ObjectType.TASK, id),  type,  tag);
	}
	
	public BaseId addItemToFactorList(ORef factorRef, int type, String tag) throws Exception
	{
		return addItemToList(factorRef,  type,  tag);
	}
	
	public BaseId addItemToList(ORef parentRef, int typeToCreate, String tag) throws Exception
	{
		BaseObject foundObject = findObject(parentRef);
		IdList currentIdList = new IdList(typeToCreate, foundObject.getData(tag));
		
		BaseId baseId = createObjectAndReturnId(typeToCreate);
		currentIdList.add(baseId);
		setObjectData(parentRef, tag, currentIdList.toString());

		return baseId;
	}

	public FactorId createTaskAndReturnId() throws Exception
	{
		return (FactorId)createObjectAndReturnId(ObjectType.TASK, BaseId.INVALID);
	}

	public DiagramFactorId createAndAddFactorToDiagram(int nodeType) throws Exception
	{
		return createAndAddFactorToDiagram(nodeType, takeNextId(nodeType));
	}
	
	public DiagramFactorId createAndAddFactorToDiagram(int nodeType, int id) throws Exception
	{
		FactorCommandHelper factorHelper = new FactorCommandHelper(this, getDiagramModel());
		CommandCreateObject createFactor = new CommandCreateObject(nodeType);
		createFactor.setCreatedId(new BaseId(id));
		executeCommand(createFactor);
		ORef factorRef = new ORef(createFactor.getObjectType(), createFactor.getCreatedId());
		CommandCreateObject createDiagramFactor = factorHelper.createDiagramFactor(getDiagramModel().getDiagramObject(), factorRef);
		
		return new DiagramFactorId(createDiagramFactor.getCreatedId().asInt());
	}
	
	public DiagramFactor createDiagramFactorWithWrappedRefLabelAndAddToDiagram(int objectType) throws Exception
	{
		DiagramFactor diagramFactor = createDiagramFactorAndAddToDiagram(objectType);
		ORef wrappedRef = diagramFactor.getWrappedORef();
		setObjectData(wrappedRef, BaseObject.TAG_LABEL, wrappedRef.toString());
		
		return diagramFactor;
	}
	
	public DiagramFactor createDiagramFactorAndAddToDiagram(int objectType) throws Exception
	{
		return createDiagramFactorAndAddToDiagram(objectType, takeNextId(objectType));
	}
	
	private int takeNextId(int objectType)
	{
		if(nextStrategyId == 0)
		{
			int startAt = getNodeIdAssigner().getHighestAssignedId() + 1;
			nextStrategyId = startAt;
			nextCauseId = startAt;
			nextTargetId = startAt;
			nextOtherId = startAt;
		}
		
		int next = -1;
		
		if(objectType == Strategy.getObjectType())
			next = nextStrategyId++;
		else if(objectType == Cause.getObjectType())
			next = nextCauseId++;
		else if(objectType == Target.getObjectType())
			next = nextTargetId++;
		else
			next = nextOtherId++;
		
		getNodeIdAssigner().idTaken(new BaseId(next));
		return next;
	}
	
	private DiagramFactor createDiagramFactorAndAddToDiagram(int objectType, int factorId) throws Exception
	{
		DiagramFactorId diagramFactorId = createAndAddFactorToDiagram(objectType, factorId);
		DiagramFactor diagramFactor = (DiagramFactor) findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));

		return diagramFactor;
	}
	
	public FactorId createNodeAndAddToDiagram(int objectType) throws Exception
	{
		return createNodeAndAddToDiagram(objectType, takeNextId(objectType));
	}

	public FactorId createNodeAndAddToDiagram(int objectType, int factorId) throws Exception
	{
		DiagramFactorId diagramFactorId = createAndAddFactorToDiagram(objectType, factorId);
		DiagramFactor diagramFactor = (DiagramFactor) findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));
	
		return diagramFactor.getWrappedId();
	}
	
	public FactorCell createFactorCell(int objectType) throws Exception
	{
		return createFactorCell(objectType, takeNextId(objectType));
	}
	
	public FactorCell createFactorCell(int objectType, int factorId) throws Exception
	{
		DiagramFactor diagramFactor = createDiagramFactorAndAddToDiagram(objectType, factorId);
		return getDiagramModel().getFactorCellByWrappedRef(diagramFactor.getWrappedORef());
	}
	
	public LinkCell createLinkCell() throws Exception
	{
		BaseId diagramLinkId = createDiagramFactorLink();
		DiagramLink diagramLink = (DiagramLink) findObject(new ORef(ObjectType.DIAGRAM_LINK, diagramLinkId));
		addDiagramLinkToModel(diagramLink);
		
		return getDiagramModel().getLinkCell(diagramLink);	
	}
	
	public void addDiagramLinkToModel(DiagramLink diagramLink) throws Exception 
	{
		 addDiagramLinkToModel(diagramLink.getDiagramLinkId());
	}
	
	public void addDiagramLinkToModel(BaseId diagramLinkId) throws Exception
	{
		CommandSetObjectData addLink = CommandSetObjectData.createAppendIdCommand(getTestingDiagramObject(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkId);
		executeCommand(addLink);
	}
	
	public BaseId createDiagramFactorLink() throws Exception
	{
		return createDiagramLink().getObjectId();
	}

	public ORef createDiagramLink() throws Exception
	{
		DiagramFactor from = createDiagramFactorAndAddToDiagram(ObjectType.CAUSE, takeNextId(Cause.getObjectType()));
		DiagramFactor to = createDiagramFactorAndAddToDiagram(ObjectType.CAUSE, takeNextId(Cause.getObjectType()));
		return createDiagramLink(from, to);
	}

	public ORef createDiagramLinkAndAddToDiagram(DiagramFactor from, DiagramFactor to, String isBidirectionalTag) throws Exception
	{
		ORef diagramLinkRef = createDiagramLinkAndAddToDiagram(from, to);
		DiagramLink diagramLink = DiagramLink.find(this, diagramLinkRef);
		setBidrectionality(diagramLink.getWrappedRef(), isBidirectionalTag);
		
		return diagramLinkRef;
	}
	
	public ORef createDiagramLinkAndAddToDiagram(DiagramFactor from, DiagramFactor to) throws Exception
	{
		ORef linkRef = createDiagramLink(from, to);
		
		IdList links = getTestingDiagramObject().getAllDiagramFactorLinkIds();
		links.add(linkRef.getObjectId());
		getTestingDiagramObject().setData(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, links.toString());
		return linkRef;
	}
	
	public BaseId createDiagramFactorLink(DiagramFactor from, DiagramFactor to) throws Exception
	{
		return createDiagramLink(from, to).getObjectId();
	}
	
	public ORef createDiagramLink(DiagramFactor from, DiagramFactor to) throws Exception
	{
		BaseId baseId = BaseId.INVALID;
		if(!shouldCreateGroupBoxLink(from, to))
			baseId = createFactorLink(from.getWrappedORef(), to.getWrappedORef()).getObjectId();
		
		CreateDiagramFactorLinkParameter extraInfo = createDiagramLinkExtraInfo(from, to, baseId);

		return createObject(ObjectType.DIAGRAM_LINK, extraInfo);
	}

	public ORef createFactorLink(ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		CreateFactorLinkParameter parameter = createFactorLinkExtraInfo(fromFactorRef, toFactorRef);
		return createObject(ObjectType.FACTOR_LINK, parameter);
	}
	
	public ORef createDiagramLinkWithCommand(DiagramFactor from, DiagramFactor to) throws Exception
	{
		BaseId baseId = BaseId.INVALID;
		if(!shouldCreateGroupBoxLink(from, to))
			baseId = createFactorLinkWithCommand(from.getWrappedORef(), to.getWrappedORef()).getObjectId();
		
		CreateDiagramFactorLinkParameter extraInfo = createDiagramLinkExtraInfo(from, to, baseId);
		CommandCreateObject createDiagramLink = new CommandCreateObject(DiagramLink.getObjectType(), extraInfo);
		executeCommand(createDiagramLink);

		return createDiagramLink.getObjectRef();
	}
	
	public ORef createFactorLinkWithCommand(ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		CreateFactorLinkParameter parameter = createFactorLinkExtraInfo(fromFactorRef, toFactorRef);
		CommandCreateObject createFactorLink = new CommandCreateObject(FactorLink.getObjectType(), parameter);
		executeCommand(createFactorLink);
		
		return createFactorLink.getObjectRef();
	}
	
	private boolean shouldCreateGroupBoxLink(DiagramFactor from, DiagramFactor to)
	{
		return from.isGroupBoxFactor() || to.isGroupBoxFactor();
	}
	
	private CreateDiagramFactorLinkParameter createDiagramLinkExtraInfo(DiagramFactor from, DiagramFactor to, BaseId baseId)
	{
		return new CreateDiagramFactorLinkParameter(new FactorLinkId(baseId.asInt()), from.getDiagramFactorId(), to.getDiagramFactorId());
	}
	
	private CreateFactorLinkParameter createFactorLinkExtraInfo(ORef fromFactorRef, ORef toFactorRef)
	{
		return new CreateFactorLinkParameter(fromFactorRef, toFactorRef);
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
		DiagramFactor threat = createDiagramFactorAndAddToDiagram(ObjectType.CAUSE, takeNextId(Cause.getObjectType()));
		enableAsThreat(threat.getWrappedORef());
		DiagramFactor target = createDiagramFactorAndAddToDiagram(ObjectType.TARGET, takeNextId(Target.getObjectType()));

		CreateFactorLinkParameter parameter = new CreateFactorLinkParameter(threat.getWrappedORef(), target.getWrappedORef());
		
		return createObject(ObjectType.FACTOR_LINK, parameter);
	}
	
	public ORef creatThreatTargetBidirectionalLink() throws Exception
	{
		ORef factorLinkRef = createThreatTargetLink();
		enableBidrectionality(factorLinkRef);
		
		return factorLinkRef;
	}

	public void enableBidrectionality(ORef factorLinkRef) throws CommandFailedException
	{
		setBidrectionality(factorLinkRef, BooleanData.BOOLEAN_TRUE);
	}

	public void setBidrectionality(ORef factorLinkRef, String isBidirectional)	throws CommandFailedException
	{
		CommandSetObjectData setBidirectionality = new CommandSetObjectData(factorLinkRef, FactorLink.TAG_BIDIRECTIONAL_LINK, isBidirectional);
		executeCommand(setBidirectionality);
	}

	public void disableAsThreat(Cause threat) throws CommandFailedException
	{
		disableAsThreat(threat.getRef());
	}
	
	public void disableAsThreat(ORef threatRef) throws CommandFailedException
	{
		changeThreatStatus(threatRef, BooleanData.BOOLEAN_FALSE);
	}
	
	public void enableAsThreat(Cause threat) throws Exception
	{
		enableAsThreat(threat.getRef());
	}
	
	public void enableAsThreat(ORef threatRef) throws Exception
	{
		changeThreatStatus(threatRef, BooleanData.BOOLEAN_TRUE);
	}

	private void changeThreatStatus(ORef threatRef, String isThreat) throws CommandFailedException
	{
		CommandSetObjectData setThreat = new CommandSetObjectData(threatRef, Cause.TAG_IS_DIRECT_THREAT, isThreat);
		executeCommand(setThreat);
	}
	
	public static ORef getDownstreamTargetRef(FactorLink factorLink) throws Exception
	{
		if (factorLink.getToFactorRef().getObjectType() == Target.getObjectType())
			return factorLink.getToFactorRef();
		
		if (factorLink.getFromFactorRef().getObjectType() == Target.getObjectType() && factorLink.isBidirectional())
			return factorLink.getFromFactorRef();
		
		throw new Exception();
	}
	
	public static ORef getUpstreamThreatRef(FactorLink factorLink) throws Exception
	{
		if (Cause.is(factorLink.getFromFactorRef()))
			return factorLink.getFromFactorRef();
		
		if (Cause.is(factorLink.getToFactorRef()) && factorLink.isBidirectional())
			return factorLink.getToFactorRef();
		
		throw new Exception();
	}
	
	public MultiCalendar parseIsoDate(String date)
	{
		return MultiCalendar.createFromIsoDateString(date);
	}

	public void addAssignment(BaseObject baseObject, double units, int startYear, int endYear) throws Exception
	{
		Assignment assignment = createAssignment();
		DateRangeEffortList dateRangeEffortList = new DateRangeEffortList();
		DateRangeEffort dateRangeEffort = createDateRangeEffort(startYear, endYear);
		dateRangeEffort.setUnitQuantity(units);
		dateRangeEffortList.add(dateRangeEffort);
		assignment.setData(Assignment.TAG_DATERANGE_EFFORTS, dateRangeEffortList.toString());
		IdList currentAssignmentIdList = baseObject.getAssignmentIdList();
		currentAssignmentIdList.add(assignment.getId());
		baseObject.setData(Task.TAG_ASSIGNMENT_IDS, currentAssignmentIdList.toString());
	}
	
	public DateRangeEffort createDateRangeEffort(int startYear, int endYear) throws Exception
	{
		MultiCalendar startDate = createMultiCalendar(startYear);
		MultiCalendar endDate = createMultiCalendar(endYear);
		DateRange dateRange = new DateRange(startDate, endDate);
		
		return new DateRangeEffort("", 0, dateRange);
	}
	
	public MultiCalendar createMultiCalendar(int year)
	{
		return MultiCalendar.createFromGregorianYearMonthDay(year, 1, 1);
	}
			
	private static int nextTargetId;
	private static int nextCauseId;
	private static int nextStrategyId;
	private static int nextOtherId;
	
	public static final String PROJECT_RESOURCE_LABEL_TEXT = "John Doe";
	public static final String SIMPLE_THREAT_RATING_COMMENT = "sample simple threat rating comment";
	public static final String STRESS_BASED_THREAT_RATING_COMMENT = "sample stress based threat rating comment";
}
