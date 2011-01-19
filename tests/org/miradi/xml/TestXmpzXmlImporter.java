/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml;

import java.awt.Dimension;
import java.awt.Point;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.Cause;
import org.miradi.objects.Dashboard;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.questions.DiagramLegendQuestion;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;
import org.miradi.questions.OpenStandardsDynamicProgressStatusQuestion;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.xml.wcs.XmpzXmlExporter;
import org.miradi.xml.xmpz.XmpzXmlImporter;

public class TestXmpzXmlImporter extends TestCaseWithProject
{
	public TestXmpzXmlImporter(String name)
	{
		super(name);
	}
	
	public void testValidateEmptyProject() throws Exception
	{
		validateUsingStringWriter();
	}
	
	private Dashboard getDashboard() throws Exception
	{
		ORef dashbaordRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		
		return Dashboard.find(getProject(), dashbaordRef);
	}

	public void testDynamicProgressCodesForAutomaticChoice() throws Exception
	{
		verifyDynamicProgressCodesInSchema(OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE);
	}

	public void testDynamicProgressCodesForManualChoice()	throws Exception
	{
		getProject().createTarget();
		final String codeToSetToManual = OpenStandardsConceptualizeQuestion.SELECT_CONSERVATION_TARGETS_CODE;
		StringChoiceMap choiceMap = new StringChoiceMap();
		choiceMap.put(codeToSetToManual, OpenStandardsDynamicProgressStatusQuestion.IN_PROGRESS_CODE);
		getProject().fillObjectUsingCommand(getDashboard(), Dashboard.TAG_PROGRESS_CHOICE_MAP, choiceMap.toString());

		verifyDynamicProgressCodesInSchema(codeToSetToManual);
	}

	private void verifyDynamicProgressCodesInSchema(String thirdLevelCode) throws Exception
	{
		Dashboard dashboard = getDashboard();
		OpenStandardsDynamicProgressStatusQuestion question = new OpenStandardsDynamicProgressStatusQuestion(getProject().getCachedDashboardEffectiveMap(), thirdLevelCode);
		CodeList allCodes = question.getAllCodes();
		for (int index = 0; index < allCodes.size(); ++index)
		{
			StringChoiceMap choiceMap = new StringChoiceMap();
			choiceMap.put(thirdLevelCode, allCodes.get(index));
			getProject().fillObjectUsingCommand(dashboard, Dashboard.TAG_PROGRESS_CHOICE_MAP, choiceMap.toString());
			validateUsingStringWriter();
		}
	}
	
	public void testDefaultIndicatorRelevancyLifeCycle() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Objective objective = getProject().createObjective(strategy);
		Indicator indicator = getProject().createIndicator(strategy);
		assertEquals("indicator on same factor is no relevant by default?", 1, objective.getRelevantIndicatorRefList().size());
		
		ProjectForTesting projectToImportInto = validateUsingStringWriter();
		ORefList objeciveRefs = projectToImportInto.getObjectivePool().getRefList();
		Objective importedObjective = Objective.find(projectToImportInto, objeciveRefs.getFirstElement());
		ORefList relevantIndicators = importedObjective.getRelevantIndicatorRefList();
		assertEquals("default indicator is no longer relevant?", 1, importedObjective.getRelevantIndicatorRefList().size());
		assertEquals("relevant default indicator changed?", indicator.getRef(), relevantIndicators.getFirstElement());		
	}
	
	public void testIndicatorRelevancyLifeCycle() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Objective objective = getProject().createObjective(strategy);
		
		Strategy indicatorOwner = getProject().createStrategy();
		Indicator relevantIndicator = getProject().createIndicator(indicatorOwner);
		Indicator nonRelevantIndicator = getProject().createIndicator(indicatorOwner);
		
		RelevancyOverrideSet indicatorRelevancyIndicators = new RelevancyOverrideSet();
		indicatorRelevancyIndicators.add(new RelevancyOverride(relevantIndicator.getRef(), true));
		indicatorRelevancyIndicators.add(new RelevancyOverride(nonRelevantIndicator.getRef(), false));
		getProject().fillObjectUsingCommand(objective, Objective.TAG_RELEVANT_INDICATOR_SET, indicatorRelevancyIndicators.toString());
		
		ProjectForTesting projectToImportInto = validateUsingStringWriter();
		ORef importedObjectiveRef = projectToImportInto.getObjectivePool().getRefList().getFirstElement();
		Objective importedObjective = objective.find(projectToImportInto, importedObjectiveRef);
		ORefList importedRelevantIndicatorRefs = importedObjective.getRelevantIndicatorRefList();
		assertEquals("incorrect relevant indicator ref count?", 1, importedRelevantIndicatorRefs.size());
		assertEquals("incorrect relevant indicator imported?", relevantIndicator.getRef(), importedRelevantIndicatorRefs.getFirstElement());
	}
	
	public void testDefaultStrategyRelevancyLifeCycle() throws Exception
	{
		Strategy objectiveOwner = getProject().createStrategy();
		getProject().createObjective(objectiveOwner);
		
		ProjectForTesting projectToImportInto = validateUsingStringWriter(); 
		ORefList objeciveRefs = projectToImportInto.getObjectivePool().getRefList();
		ORef importedObjectiveRef = objeciveRefs.getFirstElement();
		Objective objective = Objective.find(projectToImportInto, importedObjectiveRef);
		ORefList relevantStrategies = objective.getRelevantStrategyRefs();
		assertEquals("default strategy is no longer relevant?", 1, relevantStrategies.size());
		assertEquals("relevant default strategy changed?", objectiveOwner.getRef(), relevantStrategies.getFirstElement());
	}
	
	public void testStrategyAndActivityRelevancyLifeCycle() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Strategy objectiveAndActivityOwner = getProject().createStrategy();
		Objective objective = getProject().createObjective(objectiveAndActivityOwner);
		Task activity = getProject().createTask(objectiveAndActivityOwner);
		
		getProject().createFactorLink(strategy.getRef(), objectiveAndActivityOwner.getRef());
		
		RelevancyOverrideSet relevantStrategies = new RelevancyOverrideSet();
		relevantStrategies.add(new RelevancyOverride(strategy.getRef(), true));
		relevantStrategies.add(new RelevancyOverride(activity.getRef(), false));
		relevantStrategies.add(new RelevancyOverride(objectiveAndActivityOwner.getRef(), false));
		getProject().fillObjectUsingCommand(objective, Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevantStrategies.toString());

		ORefList relevantStrategyAndActivityRefs = objective.getRelevantStrategyAndActivityRefs();
		assertEquals("incorrect relevant strategy count?", 1, relevantStrategyAndActivityRefs.size());
		assertTrue("relevant strategy is not included in list?", relevantStrategies.contains(strategy.getRef()));
		
		ProjectForTesting projectToImportInto = validateUsingStringWriter(); 
		ORefList objeciveRefs = projectToImportInto.getObjectivePool().getRefList();
		assertEquals("incorrect objective count after import?", 1, objeciveRefs.size());
		
		ORef objectiveRef = objeciveRefs.getFirstElement();
		Objective importedObjective = Objective.find(projectToImportInto, objectiveRef);
		ORefList relevantStrategiesAndActivitiesAfterImport = importedObjective.getRelevantStrategyAndActivityRefs();
		assertEquals("imported relevancy list is not same as exported list?", objective.getRelevantStrategyAndActivityRefs(), relevantStrategiesAndActivitiesAfterImport);
	}
	
	public void testExpenseAssignmentLifeCycle() throws Exception
	{
		ExpenseAssignment expense = getProject().createExpenseAssignment();
		DateUnitEffortList lis = new DateUnitEffortList();
		DateUnit month = new DateUnit("2008-09");
		DateUnitEffort dateUnitEffort = new DateUnitEffort(month, 22.9);
		lis.add(dateUnitEffort);
		getProject().fillObjectUsingCommand(expense, ExpenseAssignment.TAG_DATEUNIT_EFFORTS, lis.toString());
		
		BudgetCategoryOne categoryOne = getProject().createCategoryOne();
		getProject().fillObjectUsingCommand(expense, ExpenseAssignment.TAG_CATEGORY_ONE_REF, categoryOne.getRef().toString());
		
		BudgetCategoryTwo categoryTwo = getProject().createCategoryTwo();
		getProject().fillObjectUsingCommand(expense, ExpenseAssignment.TAG_CATEGORY_TWO_REF, categoryTwo.getRef().toString());
		
		validateUsingStringWriter();
	}

	public void testProjectWithStressBasedThreatRatingData() throws Exception
	{
		getProject().populateStressBasedThreatRatingCommentsData();
		getProject().createThreatTargetDiagramLinkWithRating();
		validateUsingStringWriter();
	}
	
	public void testProjectWithSimpleThreatRatingData() throws Exception
	{
		getProject().populateSimpleThreatRatingCommentsData();
		getProject().populateSimpleThreatRatingValues();
		validateUsingStringWriter();
	}
	
	public void testValidateFilledProject() throws Exception
	{
		getProject().createAndPopulateDiagramLink();
		createFilledDiagramFactor();
		getProject().createDiagramFactorLink();
		getProject().createAndPopulateGroupBoxDiagramLink();
		getProject().createAndPopulateGoal(getProject().createTarget());
		getProject().createandpopulateThreatReductionResult();
		getProject().populateEverything();
		createFilledResultsChainDiagram();
		getProject().createObjective(getProject().createCause());		
		getProject().createAndPopulateExpenseAssignment();
		
		validateUsingStringWriter();
	}
	
	public void testDeletedOrphans() throws Exception
	{
		validateUsingStringWriter();
		
		String fakeOrphanText = "blah blah<>{}\\/[]\"!";
		getProject().appendToQuarantineFile(fakeOrphanText);
		Project imported = validateUsingStringWriter();
		assertContains(fakeOrphanText, imported.getQuarantineFileContents());
	}

	private void createFilledDiagramFactor() throws Exception
	{
		DiagramFactor diagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_LOCATION, EnhancedJsonObject.convertFromPoint(new Point(100, 12)));
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(45, 45)));
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE, TextBoxZOrderQuestion.FRONT_CODE);
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FONT_STYLE, DiagramFactorFontStyleQuestion.BOLD_CODE);
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FONT_SIZE, DiagramFactorFontSizeQuestion.LARGEST_FONT_SIZE_CODE);
	}

	private void createFilledResultsChainDiagram() throws Exception
	{
		ORef resultsChainRef = getProject().createResultsChainDiagram();
		TaggedObjectSet taggedOjectSet = getProject().createTaggedObjectSet();
		ORefList taggedObjectSetRefs = new ORefList(taggedOjectSet);
		getProject().fillObjectUsingCommand(resultsChainRef, DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, taggedObjectSetRefs);
		getProject().fillObjectUsingCommand(resultsChainRef, ResultsChainDiagram.TAG_LABEL, "SomeLabel");
		getProject().fillObjectUsingCommand(resultsChainRef, ResultsChainDiagram.TAG_SHORT_LABEL, "SomeShortLabel");
		getProject().fillObjectUsingCommand(resultsChainRef, ResultsChainDiagram.TAG_DETAIL, "SomeDetails");
		getProject().fillObjectUsingCommand(resultsChainRef, ResultsChainDiagram.TAG_ZOOM_SCALE, "2.0");
		
		CodeList hiddentTypeCodes = new CodeList();
		hiddentTypeCodes.add(DiagramLegendQuestion.STRESS_HIDDEN_TYPE_CODE);
		getProject().fillObjectUsingCommand(resultsChainRef, ResultsChainDiagram.TAG_HIDDEN_TYPES, hiddentTypeCodes.toString());
	}
	
	private ProjectForTesting validateUsingStringWriter() throws Exception
	{
		UnicodeStringWriter firstWriter = createWriter(getProject());
		
		ProjectForTesting projectToImportInto = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToImportInto");
		XmpzXmlImporter xmlImporter = new XmpzXmlImporter(projectToImportInto);
		StringInputStreamWithSeek stringInputputStream = new StringInputStreamWithSeek(firstWriter.toString());
		try
		{
			xmlImporter.importProject(stringInputputStream);
		}
		finally
		{
			stringInputputStream.close();	
		}
		
		UnicodeStringWriter secondWriter = createWriter(projectToImportInto);
		assertEquals("Exports from projects do not match?", firstWriter.toString(), secondWriter.toString());
		
		return projectToImportInto;
	}

	private UnicodeStringWriter createWriter(ProjectForTesting project) throws Exception
	{
		XmpzXmlExporter exporter = new XmpzXmlExporter(project);
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		exporter.setWriter(writer);
		exporter.exportProject(writer);
		writer.flush();
		
		return writer;
	}
}
