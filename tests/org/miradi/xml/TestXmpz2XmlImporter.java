/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.xml;

import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.TaxonomyHelper;
import org.miradi.objectpools.TaxonomyAssociationPool;
import org.miradi.objects.*;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.*;
import org.miradi.schemas.*;
import org.miradi.utils.*;
import org.miradi.views.diagram.DiagramView;
import org.miradi.views.diagram.TestLinkBendPointsMoveHandler;
import org.miradi.xml.xmpz2.MockXmpz2XmlExporterWithoutTimeStampForTesting;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

import java.util.Vector;

public class TestXmpz2XmlImporter extends TestCaseForXmpz2ExportAndImport
{
	public TestXmpz2XmlImporter(String name)
	{
		super(name);
	}
	
	public void testNewTncProjectData() throws Exception
	{
		getProject().fillTncProjectData();
		Project importedProject = validateUsingStringWriter();
		final ORef tncProjectDataRef = importedProject.getSingletonObjectRef(TncProjectDataSchema.getObjectType());
		TncProjectData tncProjectData = TncProjectData.find(importedProject, tncProjectDataRef);
		Vector<String> storedFieldTags = tncProjectData.getStoredFieldTags();
		for(String tag : storedFieldTags)
		{
			if (!tag.equals(BaseObject.TAG_LABEL))
				assertTrue("Field should contain data for " + tag + "?", tncProjectData.getData(tag).length() > 0);
		}
	}
	
	public void testObjectTreeTableConfiguration() throws Exception
	{
		final ORef ref = getProject().getSingletonObjectRef(ObjectTreeTableConfigurationSchema.getObjectType());
		ObjectTreeTableConfiguration objectTreeTableConfiguration = ObjectTreeTableConfiguration.find(getProject(), ref);
		getProject().populateObjectTreeTableConfiguration(objectTreeTableConfiguration);
		ProjectForTesting importedProject = validateUsingStringWriter();
		ORefList objectTreeTableConfigurationRefs = importedProject.getPool(ObjectTreeTableConfigurationSchema.getObjectType()).getRefList();
		for(ORef objectTreeTableConfigurationRef : objectTreeTableConfigurationRefs)
		{
			ObjectTreeTableConfiguration importedObjectTreeTableConfiguration = ObjectTreeTableConfiguration.find(importedProject, objectTreeTableConfigurationRef);
			CodeList importedCodes = importedObjectTreeTableConfiguration.getCodeList(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION);
			CodeList allInternalCodes = new CustomPlanningAllRowsQuestion().getAllCodes();
			final int expectedSize = allInternalCodes.size();
			allInternalCodes.retainAll(importedCodes);
			assertEquals("Incorrect codes imported?", expectedSize, allInternalCodes.size());
		}
	}
	
	public void testNestedBullets() throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_PROJECT_DESCRIPTION, getProject().createNestedBulletsList());
		validateUsingStringWriter();
	}
	
	public void testProjectSharingCode() throws Exception
	{
		ORef tncProjectDataRef = getProject().getSingletonObjectRef(TncProjectDataSchema.getObjectType());
		getProject().fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_PROJECT_SHARING_CODE, ProjectSharingQuestion.SHARE_ONLY_INSIDE_ORGANIZATION);
		ProjectForTesting importedProject = validateUsingStringWriter();
		
		ORef importedTncProjectDataRef = importedProject.getSingletonObjectRef(TncProjectDataSchema.getObjectType());
		TncProjectData importedTncProjectData = TncProjectData.find(importedProject, importedTncProjectDataRef);
		String importedProjectSharingCode = importedTncProjectData.getData(TncProjectData.TAG_PROJECT_SHARING_CODE);
		assertEquals("incorrect project sharing code?", "", importedProjectSharingCode);
	}
	
	public void testDashboardCommentsField() throws Exception
	{
		getProject().populateDashboard(getProject().getSingleLineSampleData());
		validateUsingStringWriter();
	}
	
	public void testQuotesWithHtmlTags() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		final String EXPECTED_VALUE = "<b>miradi &quot;inside quotes&quot; &amp; &gt; &lt; &apos;</b>";
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_TEXT, EXPECTED_VALUE);
		ProjectForTesting importedProject = validateUsingStringWriter();
		ORef strategyRef = importedProject.getStrategyPool().getORefList().getFirstElement();
		Strategy importedStrategy = Strategy.find(importedProject, strategyRef);
		String importedData = importedStrategy.getData(Strategy.TAG_TEXT);
		assertEquals("Incorrect data imported?", EXPECTED_VALUE, importedData);
	}

	public void testWorkPlanDiagramDataInclusion() throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION, DiagramObjectDataInclusionQuestion.INCLUDE_CONCEPTUAL_MODEL_DATA_CODE);
		ProjectForTesting importedProject = validateUsingStringWriter();
		assertEquals("Incorrect data inclusion value?", DiagramObjectDataInclusionQuestion.INCLUDE_CONCEPTUAL_MODEL_DATA_CODE, importedProject.getMetadata().getData(ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION));
	}
	
	public void testFutureStatus() throws Exception
	{
		Cause cause = getProject().createCause();
		Indicator indicator = getProject().createIndicator(cause);
		getProject().createAndPopulateFutureStatus(indicator);
		ProjectForTesting importedProject = validateUsingStringWriter();
		assertEquals("Incorrect future status count?", 1, importedProject.getFutureStatusPool().size());
	}
	
	public void testFormattedText() throws Exception
	{
		String sampleData = getProject().getSampleUserText(getProject().getMetadata(), ProjectMetadata.TAG_PROJECT_DESCRIPTION);
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_PROJECT_DESCRIPTION, sampleData);
		validateUsingStringWriter();
	}

	public void testTaxonomies() throws Exception
	{
		getProject().createAndPopulateMiradiShareTaxonomy();
		validateUsingStringWriter();
	}
	
	public void testTaxonomyAssociations() throws Exception
	{
		getProject().populateTaxonomyAssociationsForBaseObjectTypes();
		ProjectForTesting project = validateUsingStringWriter();
		Vector<Integer> objectTypesWithTaxonomyAssociationPool = getProject().getTypesWithTaxonomyAssociationPools();
		for(Integer taxonomyAssociationParentType : objectTypesWithTaxonomyAssociationPool)
		{
			verifyTaxonomyAssociationsForType(project, taxonomyAssociationParentType);
		}
	}

	private void verifyTaxonomyAssociationsForType(ProjectForTesting project, Integer taxonomyAssociationParentType)
	{
		Vector<String> taxonomyAssociationPoolNamesForType = TaxonomyHelper.getTaxonomyAssociationPoolNamesForType(taxonomyAssociationParentType);
		for(String taxonomyPoolNameForType : taxonomyAssociationPoolNamesForType)
		{
			final TaxonomyAssociationPool taxonomyAssociationPool = project.getTaxonomyAssociationPool();
			Vector<TaxonomyAssociation> taxonomyAssociationsForType = taxonomyAssociationPool.findTaxonomyAssociationsForPoolName(taxonomyPoolNameForType);
			assertEquals("Incorrect taxonomy associations imported for type:"+ taxonomyPoolNameForType + "?", 1, taxonomyAssociationsForType.size());
		}
	}
	
	public void testMiradiShareProjectData() throws Exception
	{
		getProject().createAndPopulateMiradiShareProjectData();
		validateUsingStringWriter();
	}
	
	public void testThreatRatingMode() throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_THREAT_RATING_MODE, ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
		validateUsingStringWriter();
	}
	
	public void testHumanWelfareTargetMode() throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_HUMAN_WELFARE_TARGET_MODE, TargetModeQuestion.HUMAN_WELFARE_TARGET_CODE);
		validateUsingStringWriter();
	}

	public void testBiophysicalFactorMode() throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_BIOPHYSICAL_FACTOR_MODE, FactorModeQuestion.BIOPHYSICAL_FACTOR_CODE);
		validateUsingStringWriter();
	}

	public void testExtraData() throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getViewData(DiagramView.getViewName()), ViewData.TAG_CURRENT_MODE, DiagramModeQuestion.MODE_STRATEGY_BRAINSTORM);
		validateUsingStringWriter();
	}
	
	public void testHumanWellbeingTargetAsHiddenType() throws Exception
	{
		CodeList hiddenTypeCodes = new CodeList();
		hiddenTypeCodes.add(HumanWelfareTargetSchema.OBJECT_NAME);
		getProject().fillObjectUsingCommand(getProject().getMainDiagramObject().getRef(), ResultsChainDiagram.TAG_HIDDEN_TYPES, hiddenTypeCodes.toString());
		ProjectForTesting projectForTesting = validateUsingStringWriter();
		
		ORefList importedDiagramObjectRefs = projectForTesting.getPool(ConceptualModelDiagramSchema.getObjectType()).getRefList();
		assertEquals("there should be only one conceptual model diagram?", 1, importedDiagramObjectRefs.size());
		ORef conceptualModelDiagramRef = importedDiagramObjectRefs.getFirstElement();
		ConceptualModelDiagram conceptualModelDiagram = ConceptualModelDiagram.find(projectForTesting, conceptualModelDiagramRef);
		CodeList importedHiddenTypeCodes = conceptualModelDiagram.getHiddenTypes();
		assertEquals("incorrect number of hidden types?", 1, importedHiddenTypeCodes.size());
		assertEquals("incorrect hidden type?", HumanWelfareTargetSchema.OBJECT_NAME, importedHiddenTypeCodes.firstElement());
	}
	
	public void testStrategyCalculatedCostElement() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		
		ResourceAssignment resourceAssignmentWithJustOneResource = getProject().createResourceAssignment();
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		getProject().fillObjectUsingCommand(resourceAssignmentWithJustOneResource, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		addEmptyDateUnitEffortList(strategy, resourceAssignmentWithJustOneResource);
		
		getProject().addResourceAssignment(strategy, 1.0, new DateUnit());
		
		validateUsingStringWriter();
	}

	private void addEmptyDateUnitEffortList(Strategy strategy, ResourceAssignment resourceAssignmentWithJustOneResource)	throws Exception
	{
		getProject().addResourceAssignment(strategy, resourceAssignmentWithJustOneResource, new DateUnitEffortList());
	}
	
	public void testImportWorkUnitDays() throws Exception
	{	
		getProject().fillWorkUnitDay();
		validateUsingStringWriter();
	}
	
	public void testQuarterColumnVisibility() throws Exception
	{
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY, QuarterColumnsVisibilityQuestion.HIDE_QUARTER_COLUMNS_CODE);
		validateUsingStringWriter();
	}
	
	public void testProjectWithStressBasedThreatRatingData() throws Exception
	{
		Cause threat = getProject().createCause();
		getProject().populateCause(threat);
		getProject().enableAsThreat(threat);
		DiagramFactor threatDiagramFactor = getProject().createAndAddFactorToDiagram(getProject().getMainDiagramObject(), threat.getRef());

		Target target = getProject().createTarget();
		ORefList stressRefs = target.getStressRefs();
		stressRefs.add(getProject().createAndPopulateStress());
		stressRefs.add(getProject().createAndPopulateStress());
		getProject().fillObjectUsingCommand(target, Target.TAG_STRESS_REFS, stressRefs.toString());
		DiagramFactor targetDiagramFactor = getProject().createAndAddFactorToDiagram(getProject().getMainDiagramObject(), target.getRef());
		
		getProject().createDiagramLinkAndAddToDiagramModel(threatDiagramFactor, targetDiagramFactor);
		assertEquals("incorrect threat stress ratings created?", 2, getProject().getThreatStressRatingPool().size());
		ProjectForTesting projectImportedInto = validateUsingStringWriter();
		assertEquals("incorrect threat stress ratings imported?", 2, projectImportedInto.getThreatStressRatingPool().size());
	}
	
	public void testProjectWithSimpleThreatRatingData() throws Exception
	{
		getProject().populateSimpleThreatRatingCommentsData(getProject().getSampleUserText());
		getProject().populateSimpleThreatRatingValues();
		validateUsingStringWriter();
	}
	
	public void testImportAbstractTargetStatus() throws Exception
	{
		HumanWelfareTarget humanWelfareTarget = getProject().createHumanWelfareTarget();
		getProject().fillObjectUsingCommand(humanWelfareTarget, HumanWelfareTarget.TAG_TARGET_STATUS, StatusQuestion.FAIR);
		
		Target target = getProject().createTarget();
		getProject().fillObjectUsingCommand(target, Target.TAG_TARGET_STATUS, StatusQuestion.GOOD);
		
		validateUsingStringWriter();
	}
	
	public void testTaggedObjectSetIdsForDiagrams() throws Exception
	{
		DiagramFactor diagramFactor = getProject().createAndPopulateDiagramFactor();
		getProject().tagDiagramFactor(diagramFactor.getWrappedORef());
		
		ProjectForTesting importedProject = validateUsingStringWriter();
		ORef conceptualDiagramRef = importedProject.getConceptualModelDiagramPool().getRefList().getFirstElement();
		ConceptualModelDiagram diagram = ConceptualModelDiagram.find(importedProject, conceptualDiagramRef);
		ORefList taggedObjectSetRefs = diagram.getSelectedTaggedObjectSetRefs();
		assertEquals("incorrect number of tagged object set objects imported?", 1, taggedObjectSetRefs.size());
		ORef taggedObjectSetORef = taggedObjectSetRefs.getFirstElement();
		
		assertTrue("Tagged object set ref should be valid?", TaggedObjectSet.is(taggedObjectSetORef));
	}
	
	public void testXmpz2NameSpaceContext() throws Exception
	{
		Xmpz2XmlImporter xmlImporter = new Xmpz2XmlImporter(getProject(), new NullProgressMeter());
		AbstractXmlNamespaceContext namespaceContext = xmlImporter.getNamespaceContext();
		assertEquals("incorrect namespace?", Xmpz2XmlConstants.NAME_SPACE, namespaceContext.getNameSpace());
		assertEquals("incorrect prefix?", Xmpz2XmlConstants.RAW_PREFIX, namespaceContext.getPrefix());
	}
	
	public void testImportEmptyProject() throws Exception
	{
		validateUsingStringWriter();
	}
	
	public void testImportWithNoBudgetDetailsElement() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		getProject().addExpenseAssignment(strategy, new DateUnitEffortList());
		validateUsingStringWriter();
	}
	
	public void testThreatReductionResultRelatedThreatImport() throws Exception
	{
		ORef threatRef = getProject().createThreat();
		ThreatReductionResult threatReductionResult = getProject().createThreatReductionResult();
		getProject().fillObjectUsingCommand(threatReductionResult, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, threatRef);
		validateUsingStringWriter();
	}
	
	public void testThreatReductionResultRelatedThreatId() throws Exception
	{
		ThreatReductionResult threatReductionResult = getProject().createThreatReductionResult();
		final ORef threatRef = getProject().createThreat();
		getProject().fillObjectUsingCommand(threatReductionResult, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, threatRef);
		Project filledProject = validateUsingStringWriter();
		ORefList allThreatReductionResultRefs = filledProject.getThreatReductionResultPool().getRefList();
		assertEquals("Threat reduction result was not imported?", 1, allThreatReductionResultRefs.size());
		ORef threatReductionResultRef = allThreatReductionResultRefs.getFirstElement();
		ThreatReductionResult importedThreatReductionResult = ThreatReductionResult.find(filledProject, threatReductionResultRef);
		assertEquals("incorrect related threat ref used?", threatRef, importedThreatReductionResult.getRelatedThreatRef());
	}
	
	public void testImportFilledProject() throws Exception
	{
		getProject().populateEverything();
		AbstractTarget target = getProject().createAndPopulateHumanWelfareTarget();
        getProject().createAndPopulateBiophysicalFactor();
        getProject().createAndPopulateBiophysicalResult();
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createAndPopulateIndicator(strategy);
		getProject().addThresholdWithXmlEscapedData(indicator);
		Task task = getProject().createAndPopulateTask(indicator, "TASK");
		Goal goal = getProject().createAndPopulateGoal(target);
		getProject().addProgressReport(task);
		getProject().addProgressReport(indicator);
		getProject().addProgressReport(strategy);
		getProject().addProgressPercent(goal);
		getProject().addExpenseWithValue(strategy);
		getProject().addResourceAssignment(strategy);
		getProject().createAndPopulateThreatReductionResult();
		
		PointList bendPointList = TestLinkBendPointsMoveHandler.createBendPointList();
		getProject().createLinkCellWithBendPoints(bendPointList);
		getProject().createAndPopulateIndicator(strategy);
		validateUsingStringWriter();
	}
	
	private ProjectForTesting validateUsingStringWriter() throws Exception
	{
		UnicodeXmlWriter projectWriter = createWriter(getProject());
		String exportedProjectXml = projectWriter.toString();	

		return validateImport(exportedProjectXml);
	}

	protected ProjectForTesting validateImport(String exportedProjectXml) throws Exception
	{
		ProjectForTesting projectToImportInto = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToImportInto");
		Xmpz2XmlImporter xmlImporter = new Xmpz2XmlImporter(projectToImportInto, new NullProgressMeter());
		
		StringInputStreamWithSeek stringInputStream = new StringInputStreamWithSeek(exportedProjectXml);
		try
		{
			xmlImporter.importProject(stringInputStream);
		}
		finally
		{
			stringInputStream.close();
		}
		
		UnicodeXmlWriter secondWriter = createWriter(projectToImportInto);
		assertEquals("Exports from projects do not match?", exportedProjectXml, secondWriter.toString());
		
		return projectToImportInto;
	}

	public void testRelevancyOverridesWithLinks() throws Exception
	{
		DiagramFactor strategyDiagramFactor = getProject().createAndAddFactorToDiagram(StrategySchema.getObjectType());
		Strategy strategy = Strategy.find(getProject(), strategyDiagramFactor.getWrappedORef());
		Task activity = getProject().createActivity(strategy);
		Indicator indicator = getProject().createIndicator(strategy);
		DiagramFactor causeDiagramFactor = getProject().createAndAddFactorToDiagram(CauseSchema.getObjectType());
		Cause cause = Cause.find(getProject(), causeDiagramFactor.getWrappedORef());
		Objective objective = getProject().createObjective(cause);
		getProject().createDiagramFactorLinkAndAddToDiagram(strategyDiagramFactor, causeDiagramFactor);
		
		assertEquals(new ORefList(strategy.getRef()), objective.getRelevantStrategyRefs());
		assertEquals(new ORefList(activity.getRef()), objective.getRelevantActivityRefs());
		assertEquals(new ORefList(), objective.getRelevantIndicatorRefList());
		verifyRoundTripExportImport();
		
		getProject().executeCommands(objective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(strategy.getRef()));
		getProject().executeCommands(objective.createCommandsToEnsureStrategyOrActivityIsRelevant(activity.getRef()));
		getProject().executeCommands(objective.createCommandsToEnsureIndicatorIsRelevant(indicator.getRef()));
		assertEquals(new ORefList(), objective.getRelevantStrategyRefs());
		assertEquals(new ORefList(activity.getRef()), objective.getRelevantActivityRefs());
		assertEquals(new ORefList(indicator.getRef()), objective.getRelevantIndicatorRefList());
		verifyRoundTripExportImport();
		
	}

	public void testRelevancyOverridesWithoutLinks() throws Exception
    {
        Task nearbyActivity = getProject().createActivity();
        Strategy mainStrategy = Strategy.find(getProject(), nearbyActivity.getOwnerRef());
        Indicator nearbyIndicator = getProject().createIndicator(mainStrategy);
        Objective nearbyObjective = getProject().createObjective(mainStrategy);

        Strategy otherStrategy = getProject().createStrategy();
        Indicator otherIndicator = getProject().createIndicator(otherStrategy);
        Task otherActivity = getProject().createActivity();

        assertEquals(new ORefList(mainStrategy.getRef()), nearbyObjective.getRelevantStrategyRefs());
        assertEquals(new ORefList(nearbyActivity.getRef()), nearbyObjective.getRelevantActivityRefs());
        assertEquals(new ORefList(nearbyIndicator.getRef()), nearbyObjective.getRelevantIndicatorRefList());

        verifyRoundTripExportImport();

        getProject().executeCommands(nearbyObjective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(mainStrategy.getRef()));
        getProject().executeCommands(nearbyObjective.createCommandsToEnsureStrategyOrActivityIsIrrelevant(nearbyActivity.getRef()));
        getProject().executeCommands(nearbyObjective.createCommandsToEnsureIndicatorIsIrrelevant(nearbyIndicator.getRef()));
        assertEquals(new ORefList(), nearbyObjective.getRelevantStrategyRefs());
        assertEquals(new ORefList(), nearbyObjective.getRelevantActivityRefs());
        assertEquals(new ORefList(), nearbyObjective.getRelevantIndicatorRefList());
        verifyRoundTripExportImport();

        getProject().executeCommands(nearbyObjective.createCommandsToEnsureStrategyOrActivityIsRelevant(otherStrategy.getRef()));
        getProject().executeCommands(nearbyObjective.createCommandsToEnsureStrategyOrActivityIsRelevant(otherActivity.getRef()));
        getProject().executeCommands(nearbyObjective.createCommandsToEnsureIndicatorIsRelevant(otherIndicator.getRef()));
        assertEquals(new ORefList(otherStrategy.getRef()), nearbyObjective.getRelevantStrategyRefs());
        assertEquals(new ORefList(otherActivity.getRef()), nearbyObjective.getRelevantActivityRefs());
        assertEquals(new ORefList(otherIndicator.getRef()), nearbyObjective.getRelevantIndicatorRefList());
        verifyRoundTripExportImport();
    }

	public void testRelevancyWithKeaIndicators() throws Exception
	{
		Target target = getProject().createKeaModeTarget();
		KeyEcologicalAttribute kea = getProject().createKea(target);
		Indicator indicator = getProject().createIndicator(kea);
		Goal goal = getProject().createGoal(target);
		assertEquals(new ORefList(indicator.getRef()), goal.getRelevantIndicatorRefList());
		verifyRoundTripExportImport();
	}

	public void testTaxonomyClassificationContainer() throws Exception
	{
		Target target = getProject().createTarget();
		getProject().populateBaseObject(target);
		verifyRoundTripExportImport();
	}

	public static UnicodeXmlWriter createWriter(ProjectForTesting projectToUse) throws Exception
	{
		Xmpz2XmlExporter exporter = new MockXmpz2XmlExporterWithoutTimeStampForTesting(projectToUse);
		UnicodeXmlWriter writer = UnicodeXmlWriter.create();
		exporter.exportProject(writer);
		writer.flush();
		
		return writer;
	}

	public void testGroupBoxes() throws Exception
	{
		DiagramFactor groupBoxDiagramFactor = getProject().createAndAddFactorToDiagram(GroupBoxSchema.getObjectType());
		DiagramFactor strategyDiagramFactor = getProject().createAndAddFactorToDiagram(StrategySchema.getObjectType());
		getProject().populateDiagramFactorGroupBox(groupBoxDiagramFactor, strategyDiagramFactor);
		verifyRoundTripExportImport();
	}
	
	public void testTaxonomySchemas() throws Exception
	{
		MiradiShareTaxonomy taxonomySchema = getProject().createAndPopulateMiradiShareTaxonomy();
		String xml = exportProject(getProject());
		CodeList topLevelCodes = taxonomySchema.getTopLevelTaxonomyElementCodes();
		assertContains("Taxonomy top level code not found in XML output?", topLevelCodes.get(0), xml);
	}
}
