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

import java.awt.Dimension;
import java.awt.Point;

import org.martus.util.MultiCalendar;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.database.ProjectServer;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.IdList;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.FactorLink;
import org.miradi.objects.FundingSource;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Organization;
import org.miradi.objects.ProgressPercent;
import org.miradi.objects.ProgressReport;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.SubTarget;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.questions.HabitatAssociationQuestion;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProgressReportLongStatusQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.questions.StressContributionQuestion;
import org.miradi.questions.StressIrreversibilityQuestion;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.questions.ThreatClassificationQuestion;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.questions.TncOrganizationalPrioritiesQuestion;
import org.miradi.questions.TncProjectPlaceTypeQuestion;
import org.miradi.questions.TrendQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.OptionalDouble;
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
	
	public DiagramObject getMainDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}
	
	public void fillGeneralProjectData() throws Exception
	{
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_NAME, "Some Project Name");
		setProjectStartDate(MultiCalendar.createFromGregorianYearMonthDay(2008, 1, 1));
		setProjectEndDate(MultiCalendar.createFromGregorianYearMonthDay(2009, 12, 31));
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_DATA_EFFECTIVE_DATE, "2006-09-27");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_AREA, "10000");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT, "10");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_LONGITUDE, "30");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_LATITUDE, "40");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_SCOPE, "Some project scope");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_DESCRIPTION, "Some project description");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_VISION, "Some project vision");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT, "TNC planning team comment");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_LESSONS_LEARNED, "TNC lessons learned");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS, "Other Related Projects");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_COUNTRIES, createSampleCountriesCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_OPERATING_UNITS, createSampleTncOperatingUnitsCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, createSampleFreshwaterEcoregionsCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, createSampleMarineEcoregionsCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, createSampleTerrestrialEcoregionsCodeList().toString());
		
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, createConproXenodata());
		
		ORef tncProjectDataRef = getSingletonObjectRef(TncProjectData.getObjectType());

		TncProjectPlaceTypeQuestion projectPlaceTypeQuestion = new TncProjectPlaceTypeQuestion();
		fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_PROJECT_PLACE_TYPES, projectPlaceTypeQuestion.getAllCodes().toString());
		
		TncOrganizationalPrioritiesQuestion organiziationalPrioritiesQuestion = new TncOrganizationalPrioritiesQuestion();
		fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES, organiziationalPrioritiesQuestion.getAllCodes().toString());
		
		fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT, "Some Parent Child Value");
	}
	
	public void setFiscalYearStartMonth(int startMonth) throws Exception
	{
		getMetadata().setData(ProjectMetadata.TAG_FISCAL_YEAR_START, Integer.toString(startMonth));
	}

	public void setSingleYearProjectDate(int singleYear) throws Exception
	{
		setProjectDates(singleYear, singleYear);	
	}

	public void setProjectDates(int startYear, int endYear) throws Exception
	{
		MultiCalendar projectStartDate = ProjectForTesting.createStartYear(startYear);
		MultiCalendar projectEndDate = ProjectForTesting.createEndYear(endYear);
		setProjectStartDate(projectStartDate);
		setProjectEndDate(projectEndDate);
	}
	
	public void setProjectStartDate(int startYear) throws Exception
	{
		setProjectStartDate(MultiCalendar.createFromGregorianYearMonthDay(startYear, 1, 1));
	}
	
	public void setProjectEndDate(int endYear) throws Exception
	{
		setProjectEndDate(MultiCalendar.createFromGregorianYearMonthDay(endYear, 1, 1));
	}
	
	public void setProjectStartDate(MultiCalendar startDate) throws Exception
	{
		setProjectDate(startDate, ProjectMetadata.TAG_START_DATE);
	}

	public void setProjectEndDate(MultiCalendar endDate) throws Exception
	{
		setProjectDate(endDate, ProjectMetadata.TAG_EXPECTED_END_DATE);
	}
	
	public void setProjectDate(MultiCalendar projectDate, String tag) throws Exception
	{
		String dateAsString = "";
		if (projectDate != null)
			dateAsString = projectDate.toString();  
			
		fillObjectUsingCommand(getMetadata().getRef(), tag, dateAsString);
	}
	
	private String createConproXenodata() throws Exception
	{
		ORef xenodataRef1 = createAndPopulateXenodata("1").getRef();
		ORef xenodataRef2 = createAndPopulateXenodata("2").getRef();
		StringRefMap refMap = new StringRefMap();
		refMap.add(ConProMiradiXml.CONPRO_CONTEXT, xenodataRef1);
		refMap.add("RandomKey", xenodataRef2);
		
		return refMap.toString();
	}

	public ProjectResource createAndPopulateProjectResource() throws Exception
	{
		ProjectResource projectResource = createProjectResource();
		populateProjectResource(projectResource);
		
		return projectResource;
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
	
	public Indicator createIndicatorContainingWhiteSpacePaddedCode() throws Exception
	{
		Cause threat = createAndPopulateThreat();
		ORefList indicatorRefs = threat.getDirectOrIndirectIndicatorRefs();
		ORef indicatorRef = indicatorRefs.getRefForType(Indicator.getObjectType());
		Indicator indicator = Indicator.find(this, indicatorRef);
		
		final String STRING_TO_TRIM = "\n\t  \t";
		fillObjectUsingCommand(indicator, Indicator.TAG_RATING_SOURCE, STRING_TO_TRIM + RatingSourceQuestion.ONSITE_RESEARCH_CODE + STRING_TO_TRIM);
		
		return indicator;
	}

	public Strategy createAndPopulateDraftStrategy() throws Exception
	{
		Strategy strategy = createAndPopulateStrategy();
		turnOnDraft(strategy);
		
		return strategy;
	}

	public void turnOnDraft(Strategy strategy) throws Exception
	{
		fillObjectUsingCommand(strategy, Strategy.TAG_STATUS, Strategy.STATUS_DRAFT);
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
	
	public Task createAndPopulateActivity() throws Exception
	{
		Task activity = createActivity();
		populateTask(activity, "Some Activity");
		
		return activity;
	}
	
	public Organization createAndPopulateOrganization() throws Exception
	{
		Organization organization = createOrganization();
		populateOrganization(organization);
		
		return organization;
	}
	
	public DiagramFactor createAndPopulateDiagramFactor() throws Exception
	{
		DiagramFactor diagramFactor = createDiagramFactorAndAddToDiagram(Target.getObjectType());
		populateDiagramFactor(diagramFactor);
		
		return diagramFactor;
	}
	
	public ExpenseAssignment createAndPopulateExpenseAssignment() throws Exception
	{
		ExpenseAssignment expenseAssignment = createExpenseAssignment();
		populateExpenseAssignment(expenseAssignment);
		
		return expenseAssignment;
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
	
	public Indicator createIndicatorWithCauseParent() throws Exception
	{
		Cause cause = createCause();
		IdList indicatorIds = new IdList(Indicator.getObjectType());
		Indicator indicator = createIndicator();
		indicatorIds.addRef(indicator.getRef());	
		fillObjectUsingCommand(cause, Cause.TAG_INDICATOR_IDS, indicatorIds.toString());
		
		return indicator;
	}
	
	public BaseObject createBaseObject(int objectType) throws Exception
	{
		ORef baseObjectRef = createObject(objectType);
		return BaseObject.find(this, baseObjectRef);
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
	
	public void tagDiagramFactor(ORef refToTag) throws Exception
	{
		TaggedObjectSet taggedObjectSet = createTaggedObjectSet();
		taggedObjectSet.setData(TaggedObjectSet.TAG_LABEL, "SomeTag");
		ORefList taggedFactorRefs = new ORefList(refToTag);
		taggedObjectSet.setData(TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, taggedFactorRefs.toString());
		
		ORefList taggedObjectSetRefs = new ORefList(taggedObjectSet.getRef());
		getTestingDiagramObject().setData(DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, taggedObjectSetRefs.toString());
	}
	
	public ResourceAssignment createResourceAssignment() throws Exception
	{
		ORef assignmentRef = createObject(ResourceAssignment.getObjectType());
		return ResourceAssignment.find(this, assignmentRef);
	}
	
	public ExpenseAssignment createExpenseAssignment() throws Exception
	{
		ORef expenseAssignmentRef = createObject(ExpenseAssignment.getObjectType());
		return ExpenseAssignment.find(this, expenseAssignmentRef);
	}
	
	public FundingSource createFundingSource() throws Exception
	{
		ORef fundingSourceRef = createObject(FundingSource.getObjectType());
		return FundingSource.find(this, fundingSourceRef);
	}
	
	public AccountingCode createAccountingCode() throws Exception
	{
		ORef accountingCodeRef = createObject(AccountingCode.getObjectType());
		return AccountingCode.find(this, accountingCodeRef);
	}
	
	public ThreatReductionResult createThreatReductionResult() throws Exception
	{
		ORef threatReductionResultRef = createObject(ThreatReductionResult.getObjectType());
		return ThreatReductionResult.find(this, threatReductionResultRef);
	}
	
	public Organization createOrganization() throws Exception
	{
		ORef organizationRef = createObject(Organization.getObjectType());
		return Organization.find(this, organizationRef);
	}
	
	public void populateTarget(Target target) throws Exception
	{
		fillObjectUsingCommand(target, Target.TAG_LABEL, "Reefs " + target.getId().toString());
		fillObjectUsingCommand(target, Target.TAG_TEXT, "Some Description Text");
		fillObjectUsingCommand(target, Target.TAG_COMMENTS, "Some comment Text");
		fillObjectUsingCommand(target, Target.TAG_CURRENT_STATUS_JUSTIFICATION, "Some status justification");
		fillObjectUsingCommand(target, Target.TAG_TARGET_STATUS, StatusQuestion.VERY_GOOD);
		turnOnTncMode(target);
				
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

	public void turnOnTncMode(Target target) throws Exception
	{
		fillObjectUsingCommand(target, Target.TAG_VIABILITY_MODE, ViabilityModeQuestion.TNC_STYLE_CODE);
	}
	
	public void populateCause(Cause cause) throws Exception
	{
		fillObjectUsingCommand(cause.getRef(), Cause.TAG_LABEL, "SomeCauseLabel");
		
		ChoiceQuestion question = getQuestion(ThreatClassificationQuestion.class);
		final int FIRST_CODE = 0;
		fillObjectUsingCommand(cause.getRef(), Cause.TAG_TAXONOMY_CODE, question.getCode(FIRST_CODE));
		
		IdList indicatorIds = new IdList(Indicator.getObjectType());
		indicatorIds.addRef(createAndPopulateIndicator().getRef());	
		fillObjectUsingCommand(cause, Cause.TAG_INDICATOR_IDS, indicatorIds.toString());
	}
	
	public void populateStress(Stress stress) throws Exception
	{
		fillObjectUsingCommand(stress, Stress.TAG_LABEL, "SomeStressLabel");
		fillObjectUsingCommand(stress, Stress.TAG_DETAIL, "Some Stress Details");
		fillObjectUsingCommand(stress, Stress.TAG_COMMENTS, "Some Stress Comments");
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
	
	private void populateProjectResource(ProjectResource projectResource) throws Exception
	{
		ChoiceQuestion roleQuestion = getQuestion(ResourceRoleQuestion.class);
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_ROLE_CODES, roleQuestion.getAllCodes().toString());
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_LABEL, PROJECT_RESOURCE_LABEL_TEXT);
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_GIVEN_NAME, "John");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_SUR_NAME, "Doe");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_LOCATION, "1 SomeStreet ave. Tampa FL 33600");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_PHONE_NUMBER, "555-555-5555");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_ORGANIZATION, "TurtleWise Corp");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_RESOURCE_TYPE, ResourceTypeQuestion.GROUP_CODE);
		fillCostPerUnitField(projectResource, "10");
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
		
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_INDICATOR_IDS, indicatorIds);
	}
	
	public void populateIndicator(Indicator indicator) throws Exception
	{
		fillObjectUsingCommand(indicator, Indicator.TAG_LABEL, "Some Indicator Label");
		fillObjectUsingCommand(indicator, Indicator.TAG_PRIORITY, PriorityRatingQuestion.HIGH_CODE);
		fillObjectUsingCommand(indicator, Indicator.TAG_DETAIL, "Some Indicator detail");
		fillObjectUsingCommand(indicator, Indicator.TAG_VIABILITY_RATINGS_COMMENT, "Some Indicator viability ratings comment");
		fillObjectUsingCommand(indicator, Indicator.TAG_RATING_SOURCE, RatingSourceQuestion.ONSITE_RESEARCH_CODE);
		
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
		fillObjectUsingCommand(indicator, Indicator.TAG_COMMENTS, "Some indicator Comment");
	}
	
	public void populateTask(Task task, String customLabel) throws Exception
	{
		fillObjectUsingCommand(task, Task.TAG_LABEL, customLabel);
		fillObjectUsingCommand(task, Task.TAG_DETAILS, "Some Task details");
		addResourceAssignment(task);

		addExpenseWithValue(task);
	}
	
	public void populateMeasurement(Measurement measurement) throws Exception
	{
		fillObjectUsingCommand(measurement, Measurement.TAG_STATUS, StatusQuestion.GOOD);
		fillObjectUsingCommand(measurement, Measurement.TAG_DATE, "2007-03-19");
		fillObjectUsingCommand(measurement, Measurement.TAG_STATUS_CONFIDENCE, StatusConfidenceQuestion.ROUGH_GUESS_CODE);
		fillObjectUsingCommand(measurement, Measurement.TAG_TREND, TrendQuestion.STRONG_DECREASE_CODE);
		fillObjectUsingCommand(measurement, Measurement.TAG_COMMENTS, "Some Measurement comment");
	}
	
	public void populateObjective(Objective objective) throws Exception
	{
		fillObjectUsingCommand(objective, Objective.TAG_SHORT_LABEL, "123");
		fillObjectUsingCommand(objective, Objective.TAG_LABEL, "Some Objective label");
		fillObjectUsingCommand(objective, Objective.TAG_FULL_TEXT, "Some objective full text data");
		fillObjectUsingCommand(objective, Objective.TAG_COMMENTS, "Some Objective comments");
		
		Cause threat = createAndPopulateThreat();
		ORefList relevantIndicatorRefs = threat.getObjectiveRefs();
		RelevancyOverrideSet relevantIndicators = new RelevancyOverrideSet();
		for (int index = 0; index < relevantIndicatorRefs.size(); ++index)
		{
			relevantIndicators.add(new RelevancyOverride(relevantIndicatorRefs.get(index), true));
		}
			
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
		fillObjectUsingCommand(strategy, Strategy.TAG_COMMENTS, "Some Strategy comments");
		
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
		
		addExpenseWithValue(strategy);
	}
	
	public void populateProgressReport(ProgressReport progressReport) throws Exception
	{
		fillObjectUsingCommand(progressReport, ProgressReport.TAG_PROGRESS_DATE, "2008-01-23");
		fillObjectUsingCommand(progressReport, ProgressReport.TAG_PROGRESS_STATUS, ProgressReportLongStatusQuestion.PLANNED_CODE);
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
	
	public void populateOrganization(Organization organization) throws Exception
	{
		fillObjectUsingCommand(organization, Organization.TAG_LABEL, "Some organization name");
		fillObjectUsingCommand(organization, Organization.TAG_SHORT_LABEL, "Some organization id");
		fillObjectUsingCommand(organization, Organization.TAG_ROLES_DESCRIPTION, "Some organization roles");
		fillObjectUsingCommand(organization, Organization.TAG_CONTACT_FIRST_NAME, "Some organization contact first name");
		fillObjectUsingCommand(organization, Organization.TAG_CONTACT_LAST_NAME, "Some organization contact last name");
		fillObjectUsingCommand(organization, Organization.TAG_EMAIL, "Some organization email");
		fillObjectUsingCommand(organization, Organization.TAG_PHONE_NUMBER, "Some organization phone number");
		fillObjectUsingCommand(organization, Organization.TAG_COMMENTS, "Some organization comments");
	}
	
	public void populateDiagramFactor(DiagramFactor diagramFactor) throws Exception
	{
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_LOCATION, EnhancedJsonObject.convertFromPoint(new Point(10, 10)));
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FONT_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(30, 120)));
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_BACKGROUND_COLOR, DiagramFactorBackgroundQuestion.ORANGE_COLOR_CODE);
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FOREGROUND_COLOR, DiagramFactorFontColorQuestion.BROWN_HEX);
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FONT_SIZE, "2.5");
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FONT_STYLE, DiagramFactorFontStyleQuestion.BOLD_CODE);
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE, TextBoxZOrderQuestion.FRONT_CODE);		
	}
	
	public void populateExpenseAssignment(ExpenseAssignment expenseAssignment) throws Exception
	{
		fillObjectUsingCommand(expenseAssignment, ExpenseAssignment.TAG_LABEL, "Some Expense");
		fillObjectUsingCommand(expenseAssignment, ExpenseAssignment.TAG_FUNDING_SOURCE_REF, createFundingSource().getRef().toString());
		fillObjectUsingCommand(expenseAssignment, ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, createAccountingCode().getRef().toString());
		
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(createDateUnitEffort(2008, 2008, 10.0));
		fillObjectUsingCommand(expenseAssignment, ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());
	}
	
	public void populateEverything() throws Exception
	{
		switchToStressBaseMode();
		fillGeneralProjectData();
		createAndPopulateDirectThreatLink();
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
		createAndPopulateActivity();
		createIndicatorContainingWhiteSpacePaddedCode();
		createAndPopulateOrganization();
		createAndPopulateExpenseAssignment();
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

	public void fillCostPerUnitField(ProjectResource projectResource, String costPerUnit) throws Exception
	{
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_COST_PER_UNIT, costPerUnit);
	}
	
	public void fillObjectUsingCommand(ORef objectRef, String fieldTag, String data) throws Exception
	{
		CommandSetObjectData setData = new CommandSetObjectData(objectRef, fieldTag, data);
		executeCommand(setData);
	}

	public void fillObjectUsingCommand(BaseObject object, String fieldTag, String data) throws Exception
	{
		fillObjectUsingCommand(object.getRef(), fieldTag, data);
	}
	
	public void fillObjectUsingCommand(BaseObject object, String fieldTag, ORefList data) throws Exception
	{
		fillObjectUsingCommand(object, fieldTag, data.toString());
	}
	
	public void fillObjectUsingCommand(BaseObject object, String fieldTag, IdList data) throws Exception
	{
		fillObjectUsingCommand(object, fieldTag, data.toString());
	}
	
	public void fillObjectUsingCommand(BaseObject object, String fieldTag, ORefList refListToBeConvertedToIdList, int type) throws Exception
	{
		fillObjectUsingCommand(object, Task.TAG_SUBTASK_IDS, refListToBeConvertedToIdList.convertToIdList(type));
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

	public DiagramFactor createAndAddFactorToDiagram(int nodeType) throws Exception
	{
		return createAndAddFactorToDiagram(nodeType, takeNextId(nodeType));
	}
	
	public DiagramFactor createAndAddFactorToDiagram(DiagramObject diagramObject, int objectType) throws Exception
	{
		return createAndAddFactorToDiagram(diagramObject, objectType, takeNextId(objectType));
	}
		
	private DiagramFactor createAndAddFactorToDiagram(int nodeType, int id) throws Exception
	{
		DiagramObject diagramObject = getDiagramModel().getDiagramObject();
		return createAndAddFactorToDiagram(diagramObject, nodeType, id);
	}

	private DiagramFactor createAndAddFactorToDiagram(DiagramObject diagramObject, int objectType, int id) throws Exception
	{
		FactorCommandHelper factorHelper = new FactorCommandHelper(this, getDiagramModel());
		CommandCreateObject createFactor = new CommandCreateObject(objectType);
		createFactor.setCreatedId(new BaseId(id));
		executeCommand(createFactor);
		ORef factorRef = new ORef(createFactor.getObjectType(), createFactor.getCreatedId());
		
		CommandCreateObject createDiagramFactor = factorHelper.createDiagramFactor(diagramObject, factorRef);
		
		return DiagramFactor.find(this, createDiagramFactor.getObjectRef());
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
		return createAndAddFactorToDiagram(objectType, takeNextId(objectType));
	}
	
	public ORef createDiagramFactorLinkAndAddToDiagram(DiagramFactor from, DiagramFactor to) throws Exception
	{
		ORef diagramLinkRef = createDiagramLink(from, to);
		CommandSetObjectData cmd = CommandSetObjectData.createAppendIdCommand(getMainDiagramObject(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkRef.getObjectId());
		executeCommand(cmd);
		return diagramLinkRef;
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
	
	public FactorId createNodeAndAddToDiagram(int objectType) throws Exception
	{
		return createNodeAndAddToDiagram(objectType, takeNextId(objectType));
	}

	public FactorId createNodeAndAddToDiagram(int objectType, int factorId) throws Exception
	{
		return createAndAddFactorToDiagram(objectType, factorId).getWrappedId();
	}
	
	public FactorCell createFactorCell(int objectType) throws Exception
	{
		return createFactorCell(objectType, takeNextId(objectType));
	}
	
	public FactorCell createFactorCell(int objectType, int factorId) throws Exception
	{
		DiagramFactor diagramFactor = createAndAddFactorToDiagram(objectType, factorId);
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
		DiagramFactor from = createAndAddFactorToDiagram(ObjectType.CAUSE, takeNextId(Cause.getObjectType()));
		DiagramFactor to = createAndAddFactorToDiagram(ObjectType.CAUSE, takeNextId(Cause.getObjectType()));
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
		return createDiagramLinkAndAddToDiagramModel(from, to).getRef();
	}
	
	//TODO - find a better name,  or inline above method
	public DiagramLink createDiagramLinkAndAddToDiagramModel(DiagramFactor from, DiagramFactor to) throws Exception
	{
		ORef diagramLinkRef = createDiagramLink(from, to);
		DiagramLink diagramLink = DiagramLink.find(this, diagramLinkRef);
		addDiagramLinkToModel(diagramLink);
	
		return diagramLink;
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
		DiagramFactor threat = createAndAddFactorToDiagram(ObjectType.CAUSE, takeNextId(Cause.getObjectType()));
		enableAsThreat(threat.getWrappedORef());
		DiagramFactor target = createAndAddFactorToDiagram(ObjectType.TARGET, takeNextId(Target.getObjectType()));

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
	
	public void addExpenseWithValue(BaseObject baseObject) throws Exception
	{
		DateUnitEffort dateUnitEffort = new DateUnitEffort(new DateUnit(), 12.0);
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(dateUnitEffort);
		addExpenseAssignment(baseObject, dateUnitEffortList);
	}
	
	public void addExpenseAssignment(BaseObject baseObject, DateUnitEffortList dateUnitEffortList) throws Exception
	{
		ORef expenseRef = createObject(ExpenseAssignment.getObjectType());
		ExpenseAssignment assignment = ExpenseAssignment.find(this, expenseRef);
		assignment.setData(ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());
		
		ORefList currentAssignmentRefList = baseObject.getExpenseAssignmentRefs();
		currentAssignmentRefList.add(assignment.getRef());
		baseObject.setData(BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS, currentAssignmentRefList.toString());
	}
	
	public ResourceAssignment addResourceAssignment(BaseObject baseObject, double units, int startYear, int endYear) throws Exception
	{
		ProjectResource projectResource = createAndPopulateProjectResource();
		return addResourceAssignment(baseObject, projectResource, units, startYear, endYear);
	}

	public ResourceAssignment addResourceAssignment(BaseObject parentObject, ProjectResource projectResource, double units, int startYear,	int endYear) throws Exception
	{
		MultiCalendar startDate = createStartYear(startYear);
		MultiCalendar endDate = createEndYear(endYear);
		DateUnitEffort dateUnitEffort = createDateUnitEffort(startDate, endDate);
		dateUnitEffort.setUnitQuantity(units);
		
		return addResourceAssignment(parentObject, projectResource, dateUnitEffort);
	}
	
	public void addResourceAssignment(BaseObject object) throws Exception
	{
		addResourceAssignment(object, createAndPopulateProjectResource(), 10.0, new DateUnit());
	}

	private ResourceAssignment addResourceAssignment(BaseObject parentObject, ProjectResource projectResource, double units, DateUnit dateUnit) throws Exception
	{
		ResourceAssignment resourceAssignment = addResourceAssignment(parentObject, units, dateUnit);
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		
		return resourceAssignment;
	}
	
	public ResourceAssignment addResourceAssignment(BaseObject parentObject, double units,  DateUnit dateUnit) throws Exception
	{
		DateUnitEffort dateUnitEffort = new DateUnitEffort(dateUnit, units);
		ResourceAssignment assignment = createResourceAssignment();
		return addResourceAssignment(parentObject, assignment, dateUnitEffort);
	}

	public ResourceAssignment addResourceAssignment(BaseObject parentObject, ProjectResource projectResource, DateUnitEffort dateUnitEffort) throws Exception
	{
		ResourceAssignment assignment = createResourceAssignment();
		fillObjectUsingCommand(assignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		
		return addResourceAssignment(parentObject, assignment, dateUnitEffort);
	}

	private ResourceAssignment addResourceAssignment(BaseObject parentObject, ResourceAssignment assignment, DateUnitEffort dateUnitEffort) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(dateUnitEffort);
		assignment.setData(ResourceAssignment.TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toString());
		IdList currentAssignmentIdList = parentObject.getResourceAssignmentIdList();
		currentAssignmentIdList.add(assignment.getId());
		parentObject.setData(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, currentAssignmentIdList.toString());
		
		return assignment;
	}

	public static MultiCalendar createStartYear(int startYear)
	{
		return MultiCalendar.createFromGregorianYearMonthDay(startYear, 1, 1);
	}
	
	public static MultiCalendar createEndYear(int endYear)
	{
		return MultiCalendar.createFromGregorianYearMonthDay(endYear, 12, 31);
	}
	
	public DateUnitEffort createDateUnitEffort(int startYear, int endYear) throws Exception
	{
		return createDateUnitEffort(startYear, endYear, 0.0);
	}
	
	public DateUnitEffort createDateUnitEffort(int startYear, int endYear, double effort) throws Exception
	{
		DateUnit dateUnit = createDateUnit(startYear, endYear);
		return new DateUnitEffort(dateUnit,  effort);
	}
	
	public DateUnitEffort createDateUnitEffort(MultiCalendar startDate, MultiCalendar endDate) throws Exception
	{
		DateRange dateRange = createDateRange(startDate, endDate);
		return new DateUnitEffort(DateUnit.createFromDateRange(dateRange), 0);
	}

	public static DateRange createDateRange(int startYear, int endYear) throws Exception
	{
		MultiCalendar startDate = createStartYear(startYear);
		MultiCalendar endDate = createEndYear(endYear);
		return createDateRange(startDate, endDate);
	}
	
	public static DateRange createDateRange(MultiCalendar startDate, MultiCalendar endDate) throws Exception
	{
		return new DateRange(startDate, endDate);
	}
	
	public DateUnit createDateUnit(int sameYear) throws Exception
	{
		return createDateUnit(sameYear, sameYear);
	}
	
	public DateUnit createDateUnit(int startYear, int endYear) throws Exception
	{
		return DateUnit.createFromDateRange(createDateRange(startYear, endYear));
	}
	
	public DateUnit createSingleYearDateUnit(int year) throws Exception
	{
		
		return createDateUnit(year, year);
	}
	
	public MultiCalendar createMultiCalendar(int year)
	{
		return createStartYear(year);
	}
	
	public TimePeriodCosts createTimePeriodCosts(double expenses, ORef projectResourceRef, double units)
	{
		TimePeriodCosts timePeriodCosts = createTimePeriodCosts(projectResourceRef, units);
		addExpense(timePeriodCosts, expenses);
		
		return timePeriodCosts;
	}
	
	public void addExpense(TimePeriodCosts timePeriodCosts, double expenses)
	{
		timePeriodCosts.add(createTimePeriodCosts((expenses)));
	}

	public TimePeriodCosts createTimePeriodCosts(double expenses)
	{
		return new TimePeriodCosts(ORef.INVALID, ORef.INVALID, new OptionalDouble(expenses));
	}
	
	public TimePeriodCosts createTimePeriodCosts(ORef projectResourceRef, double workUnits)
	{
		return new TimePeriodCosts(projectResourceRef, ORef.INVALID, ORef.INVALID, new OptionalDouble(workUnits));
	}
	
	public Task createActivity() throws Exception
	{
		Strategy strategy = createStrategy();
		Task activity = createTask();
		appendActivityToStrategy(strategy, activity);
		
		return activity;
	}
	
	public void appendActivityToStrategy(Strategy strategy, Task activity) throws Exception
	{
		appendTaskToParentIdList(strategy, activity, Strategy.TAG_ACTIVITY_IDS);	
	}
	
	public Task createMethod() throws Exception
	{
		Indicator indicator = createIndicatorWithCauseParent();
		Task method = createTask();
		appendMethodToIndicator(indicator, method);
		
		return method;
	}
	
	public void appendMethodToIndicator(Indicator indicator, Task method) throws Exception
	{
		appendTaskToParentIdList(indicator, method, Indicator.TAG_METHOD_IDS);
	}
	
	public void appendTaskToTask(Task parentTask, Task childTask) throws Exception
	{
		appendTaskToParentIdList(parentTask, childTask, Task.TAG_SUBTASK_IDS);
	}
	
	private void appendTaskToParentIdList(BaseObject parent, BaseObject child, String childListTag) throws Exception
	{
		ORefList childTaskRefs = new ORefList(child);
		fillObjectUsingCommand(parent, childListTag, childTaskRefs.convertToIdList(Task.getObjectType()).toString());
	}
	
	public static double calculateTimePeriodCosts(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		return calculateRawTimePeriodCosts(baseObject, dateUnit).getValue();
	}
	
	public static OptionalDouble calculateRawTimePeriodCosts(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		return baseObject.calculateTimePeriodCosts(dateUnit).getTotalWorkUnits();
	}
			
	private static int nextTargetId;
	private static int nextCauseId;
	private static int nextStrategyId;
	private static int nextOtherId;
	
	public static final String PROJECT_RESOURCE_LABEL_TEXT = "John Doe";
	public static final String SIMPLE_THREAT_RATING_COMMENT = "sample simple threat rating comment";
	public static final String STRESS_BASED_THREAT_RATING_COMMENT = "sample stress based threat rating comment";
}
