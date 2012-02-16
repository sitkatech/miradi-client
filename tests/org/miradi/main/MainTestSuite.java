/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.main;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.martus.util.TestMultiCalendar;
import org.martus.util.xml.TestSimpleXmlParser;
import org.miradi.commands.TestCommandBeginTransaction;
import org.miradi.commands.TestCommandCreateObject;
import org.miradi.commands.TestCommandDeleteObject;
import org.miradi.commands.TestCommandEndTransaction;
import org.miradi.commands.TestCommandExecutedEvent;
import org.miradi.commands.TestCommandSetFactorSize;
import org.miradi.commands.TestCommandSetObjectData;
import org.miradi.commands.TestCommandSetThreatRating;
import org.miradi.commands.TestCommands;
import org.miradi.diagram.TestBendPointSelectionHelper;
import org.miradi.diagram.TestDelete;
import org.miradi.diagram.TestDiagramAddFactor;
import org.miradi.diagram.TestDiagramAddFactorLink;
import org.miradi.diagram.TestDiagramComponent;
import org.miradi.diagram.TestDiagramModel;
import org.miradi.diagram.TestDiagramView;
import org.miradi.diagram.TestEAMGraphCellByFactorTypeSorter;
import org.miradi.diagram.TestEamGraphCell;
import org.miradi.diagram.TestGroupBoxLinking;
import org.miradi.diagram.TestLayerSorter;
import org.miradi.diagram.TestLinkCreator;
import org.miradi.diagram.TestSetFactorSize;
import org.miradi.diagram.TestUndoAndRedo;
import org.miradi.diagram.arranger.TestMeglerArranger;
import org.miradi.diagram.cells.TestDiagramFactor;
import org.miradi.diagram.cells.TestLinkCell;
import org.miradi.forms.TestFieldPanelSpec;
import org.miradi.forms.TestFormConstant;
import org.miradi.forms.TestFormFieldData;
import org.miradi.forms.TestFormFieldLabel;
import org.miradi.forms.TestFormRow;
import org.miradi.forms.TestPanelHolderSpec;
import org.miradi.ids.TestBaseId;
import org.miradi.ids.TestIdList;
import org.miradi.legacyprojects.TestDataUpgrader;
import org.miradi.legacyprojects.TestDataUpgraderForMiradi3;
import org.miradi.objectdata.TestAbstractUserStringDataWithHtmlFormatting;
import org.miradi.objectdata.TestDateData;
import org.miradi.objectdata.TestDateRangeData;
import org.miradi.objectdata.TestIntegerData;
import org.miradi.objectdata.TestNumberData;
import org.miradi.objectdata.TestRefListListData;
import org.miradi.objecthelpers.TestBaseObjectRollupValues;
import org.miradi.objecthelpers.TestCategorizedQuantity;
import org.miradi.objecthelpers.TestCodeToChoiceMap;
import org.miradi.objecthelpers.TestCodeToCodeListMap;
import org.miradi.objecthelpers.TestCodeToCodeMap;
import org.miradi.objecthelpers.TestCodeToUserStringMap;
import org.miradi.objecthelpers.TestDateUnitEffort;
import org.miradi.objecthelpers.TestDateUnitEffortList;
import org.miradi.objecthelpers.TestMapList;
import org.miradi.objecthelpers.TestORef;
import org.miradi.objecthelpers.TestORefList;
import org.miradi.objecthelpers.TestORefSet;
import org.miradi.objecthelpers.TestObjectDeepCopier;
import org.miradi.objecthelpers.TestOldToNewDiagramFactorMap;
import org.miradi.objecthelpers.TestRelevancyOverride;
import org.miradi.objecthelpers.TestRelevancyOverrideSet;
import org.miradi.objecthelpers.TestResultsChainCreatorHelper;
import org.miradi.objecthelpers.TestStringRefMap;
import org.miradi.objecthelpers.TestThreatStressRatingEnsurer;
import org.miradi.objecthelpers.TestThreatTargetVirtualLinkHelper;
import org.miradi.objecthelpers.TestTimePeriodCosts;
import org.miradi.objecthelpers.TestTimePeriodCostsMap;
import org.miradi.objects.TestAccountingCode;
import org.miradi.objects.TestAssignment;
import org.miradi.objects.TestAudience;
import org.miradi.objects.TestBaseObject;
import org.miradi.objects.TestCategoryOne;
import org.miradi.objects.TestCategoryTwo;
import org.miradi.objects.TestCause;
import org.miradi.objects.TestChainWalker;
import org.miradi.objects.TestConceptualModelDiagram;
import org.miradi.objects.TestConceptualModelThreatRatings;
import org.miradi.objects.TestCostAllocationRule;
import org.miradi.objects.TestDashboard;
import org.miradi.objects.TestDashboardStatusMapsCache;
import org.miradi.objects.TestDiagramChainObject;
import org.miradi.objects.TestDiagramContentsObject;
import org.miradi.objects.TestDiagramLink;
import org.miradi.objects.TestDiagramObject;
import org.miradi.objects.TestExpense;
import org.miradi.objects.TestFactor;
import org.miradi.objects.TestFactorLink;
import org.miradi.objects.TestFosProjectData;
import org.miradi.objects.TestFundingSource;
import org.miradi.objects.TestGoal;
import org.miradi.objects.TestHumanWelfareTarget;
import org.miradi.objects.TestIndicator;
import org.miradi.objects.TestIntermediateResult;
import org.miradi.objects.TestIucnRedlistSpecies;
import org.miradi.objects.TestKeyEcologicalAttribute;
import org.miradi.objects.TestMeasurement;
import org.miradi.objects.TestObjectFindOwnerAndFindReferrer;
import org.miradi.objects.TestObjectManager;
import org.miradi.objects.TestObjective;
import org.miradi.objects.TestOrganization;
import org.miradi.objects.TestOtherNotableSpecies;
import org.miradi.objects.TestPlanningViewConfiguration;
import org.miradi.objects.TestProgressPercent;
import org.miradi.objects.TestProgressReport;
import org.miradi.objects.TestProjectMetadata;
import org.miradi.objects.TestProjectResource;
import org.miradi.objects.TestRareProjectData;
import org.miradi.objects.TestRatingCriterion;
import org.miradi.objects.TestReportTemplate;
import org.miradi.objects.TestResultsChainDiagram;
import org.miradi.objects.TestScopeBox;
import org.miradi.objects.TestStrategy;
import org.miradi.objects.TestStress;
import org.miradi.objects.TestSubTarget;
import org.miradi.objects.TestTableSettings;
import org.miradi.objects.TestTaggedObjectSet;
import org.miradi.objects.TestTask;
import org.miradi.objects.TestTextBox;
import org.miradi.objects.TestThreatRatingBundle;
import org.miradi.objects.TestThreatRatingCommentsData;
import org.miradi.objects.TestThreatReductionResult;
import org.miradi.objects.TestThreatStressRating;
import org.miradi.objects.TestThreatTargetChainObject;
import org.miradi.objects.TestTncProjectData;
import org.miradi.objects.TestValueOption;
import org.miradi.objects.TestViewData;
import org.miradi.objects.TestWcpaProjectData;
import org.miradi.objects.TestWcsProjectData;
import org.miradi.objects.TestWwfProjectData;
import org.miradi.objects.TestXenodata;
import org.miradi.objects.TestXslTemplate;
import org.miradi.project.TestCausePool;
import org.miradi.project.TestCommandExecutor;
import org.miradi.project.TestDateUnit;
import org.miradi.project.TestFactorDeleteHelper;
import org.miradi.project.TestFactorLinkPool;
import org.miradi.project.TestGroupBoxPool;
import org.miradi.project.TestHumanWelfareTargetPool;
import org.miradi.project.TestIdAssigner;
import org.miradi.project.TestIntermediateResultPool;
import org.miradi.project.TestMpzToMpfConverter;
import org.miradi.project.TestProject;
import org.miradi.project.TestProjectCalendar;
import org.miradi.project.TestProjectCommandExecutions;
import org.miradi.project.TestProjectInfo;
import org.miradi.project.TestProjectRepairer;
import org.miradi.project.TestProjectSaver;
import org.miradi.project.TestProjectTotalCalculator;
import org.miradi.project.TestRealProject;
import org.miradi.project.TestScopeBoxPool;
import org.miradi.project.TestSimpleThreatFormula;
import org.miradi.project.TestSimpleThreatRatingFramework;
import org.miradi.project.TestStrategyPool;
import org.miradi.project.TestStressBasedThreatFormula;
import org.miradi.project.TestStressBasedThreatRatingFramework;
import org.miradi.project.TestTNCViabilityFormula;
import org.miradi.project.TestTargetPool;
import org.miradi.project.TestTextBoxPool;
import org.miradi.project.TestThreatReductionResultPool;
import org.miradi.questions.TestChoiceItem;
import org.miradi.questions.TestChoiceQuestion;
import org.miradi.ratings.TestRatingChoice;
import org.miradi.ratings.TestRatingQuestion;
import org.miradi.ratings.TestStrategyRatingSummary;
import org.miradi.utils.TestAbstractTableExporter;
import org.miradi.utils.TestBaseObjectDateAndIdComparator;
import org.miradi.utils.TestBendPointList;
import org.miradi.utils.TestCodeList;
import org.miradi.utils.TestColumnSequenceSaver;
import org.miradi.utils.TestConproMiradiHabitatCodeMap;
import org.miradi.utils.TestDateRange;
import org.miradi.utils.TestDelimitedFileLoader;
import org.miradi.utils.TestDiagramCorruptionDetector;
import org.miradi.utils.TestDoubleUtilities;
import org.miradi.utils.TestEditableHtmlPane;
import org.miradi.utils.TestEnhancedJsonObject;
import org.miradi.utils.TestFloatingPointFormatter;
import org.miradi.utils.TestHtmlUtilities;
import org.miradi.utils.TestLogging;
import org.miradi.utils.TestOptionalDouble;
import org.miradi.utils.TestPointList;
import org.miradi.utils.TestRtfWriter;
import org.miradi.utils.TestStringUtilities;
import org.miradi.utils.TestTaxonomyFileLoader;
import org.miradi.utils.TestThreatStressRatingHelper;
import org.miradi.utils.TestTranslations;
import org.miradi.utils.TestXmlUtilities2;
import org.miradi.views.budget.ImportAccountingCodesDoerTest;
import org.miradi.views.diagram.TestDiagramAliasPaster;
import org.miradi.views.diagram.TestLinkBendPointsMoveHandler;
import org.miradi.views.diagram.doers.TestDeleteAnnotationDoer;
import org.miradi.views.planning.TestFullTimeEmployeeCalculationsInsideModel;
import org.miradi.views.planning.TestPlanningViewMainTableModel;
import org.miradi.views.planning.TestTreeRebuilder;
import org.miradi.views.planning.TestWorkPlanRowColumnProvider;
import org.miradi.views.threatrating.TestTargetSummartyRowTableModel;
import org.miradi.views.umbrella.TestUndoRedo;
import org.miradi.views.workplan.TestDeleteActivity;
import org.miradi.xml.TestXmpzXmlImporter;
import org.miradi.xml.conpro.TestConProCodeMapHelper;
import org.miradi.xml.conpro.exporter.TestConproXmlExporter;
import org.miradi.xml.conpro.importer.TestConproXmlImporter;
import org.miradi.xml.wcs.TestXmpzExporter;

public class MainTestSuite extends TestSuite
{
	public MainTestSuite(String name, Locale localeToUse)
	{
		super(name);
		
		locale = localeToUse;
		
		addAllTests();
	}

	private void addAllTests()
	{
		// database package
		addTest(new TestSuite(TestDataUpgrader.class));
		addTest(new TestSuite(TestDataUpgraderForMiradi3.class));
		
		// forms package
		addTest(new TestSuite(TestFieldPanelSpec.class));
		addTest(new TestSuite(TestPanelHolderSpec.class));
		addTest(new TestSuite(TestFormConstant.class));
		addTest(new TestSuite(TestFormFieldLabel.class));
		addTest(new TestSuite(TestFormFieldData.class));
		addTest(new TestSuite(TestFormRow.class));
		
		// main package
		addTest(new TestSuite(TestAutomaticProjectSaver.class));
		addTest(new TestSuite(TestCommandExecutedEvents.class));
		addTest(new TestSuite(TestMainMenu.class));
		addTest(new TestSuite(TestTransferableMiradiList.class));
		addTest(new TestSuite(TestEAM.class));
		
		// project package
		addTest(new TestSuite(TestThreatReductionResultPool.class));
		addTest(new TestSuite(TestTextBoxPool.class));
		addTest(new TestSuite(TestScopeBoxPool.class));
		addTest(new TestSuite(TestIntermediateResultPool.class));
		addTest(new TestSuite(TestFactorLinkPool.class));
		addTest(new TestSuite(TestStrategyPool.class));
		addTest(new TestSuite(TestTargetPool.class));
		addTest(new TestSuite(TestHumanWelfareTargetPool.class));
		addTest(new TestSuite(TestCausePool.class));
		addTest(new TestSuite(TestProject.class));
		addTest(new TestSuite(TestProjectCommandExecutions.class));
		addTest(new TestSuite(TestProjectCalendar.class));
		addTest(new TestSuite(TestDateUnit.class));
		addTest(new TestSuite(TestProjectRepairer.class));
		addTest(new TestSuite(TestRealProject.class));
		addTest(new TestSuite(TestIdAssigner.class));
		addTest(new TestSuite(TestProjectInfo.class));
		addTest(new TestSuite(TestSimpleThreatRatingFramework.class));
		addTest(new TestSuite(TestSimpleThreatFormula.class));
		addTest(new TestSuite(TestTNCViabilityFormula.class));
		addTest(new TestSuite(TestFactorDeleteHelper.class));
		addTest(new TestSuite(TestStressBasedThreatFormula.class));
		addTest(new TestSuite(TestStressBasedThreatRatingFramework.class));
		addTest(new TestSuite(TestGroupBoxPool.class));
		addTest(new TestSuite(TestProjectTotalCalculator.class));
		addTest(new TestSuite(TestCommandExecutor.class));
		addTest(new TestSuite(TestProjectSaver.class));
		addTest(new TestSuite(TestMpzToMpfConverter.class));
		
		//questions package
		addTest(new TestSuite(TestChoiceItem.class));
		addTest(new TestSuite(TestChoiceQuestion.class));
		
		// utils package
		addTest(new TestSuite(TestEnhancedJsonObject.class));
		addTest(new TestSuite(TestLogging.class));
		addTest(new TestSuite(TestTranslations.class));
		addTest(new TestSuite(TestDelimitedFileLoader.class));
		addTest(new TestSuite(TestTaxonomyFileLoader.class));
		addTest(new TestSuite(TestDateRange.class));
		addTest(new TestSuite(TestConproMiradiHabitatCodeMap.class));
		addTest(new TestSuite(TestBaseObjectDateAndIdComparator.class));
		addTest(new TestSuite(TestDiagramCorruptionDetector.class));
		addTest(new TestSuite(TestRtfWriter.class));
		addTest(new TestSuite(TestThreatStressRatingHelper.class));
		addTest(new TestSuite(TestColumnSequenceSaver.class));
		addTest(new TestSuite(TestOptionalDouble.class));
		addTest(new TestSuite(TestAbstractTableExporter.class));
		addTest(new TestSuite(TestFloatingPointFormatter.class));
		addTest(new TestSuite(TestDoubleUtilities.class));	
		addTest(new TestSuite(TestStringUtilities.class));
		addTest(new TestSuite(TestHtmlUtilities.class));
		addTest(new TestSuite(TestEditableHtmlPane.class));
		
		// diagram package
		addTest(new TestSuite(TestDiagramModel.class));
		addTest(new TestSuite(TestDiagramView.class));
		addTest(new TestSuite(TestDiagramComponent.class));
		addTest(new TestSuite(TestBendPointSelectionHelper.class));
		addTest(new TestSuite(TestLinkBendPointsMoveHandler.class));
		addTest(new TestSuite(TestLinkCreator.class));
		addTest(new TestSuite(TestGroupBoxLinking.class));
		addTest(new TestSuite(TestLayerSorter.class));
		addTest(new TestSuite(TestEAMGraphCellByFactorTypeSorter.class));
		
		addTest(new TestSuite(TestMeglerArranger.class));

		// factors package
		addTest(new TestSuite(TestDiagramAddFactorLink.class));
		addTest(new TestSuite(TestDelete.class));
		addTest(new TestSuite(TestEamGraphCell.class));
		addTest(new TestSuite(TestDiagramAddFactor.class));
		addTest(new TestSuite(TestDiagramFactor.class));
		addTest(new TestSuite(TestSetFactorSize.class));
		addTest(new TestSuite(TestUndoAndRedo.class));
		addTest(new TestSuite(TestLinkCell.class));
		
		//objectdata package
		addTest(new TestSuite(TestAbstractUserStringDataWithHtmlFormatting.class));
		addTest(new TestSuite(TestDateData.class));
		addTest(new TestSuite(TestDateRangeData.class));
		addTest(new TestSuite(TestIntegerData.class));
		addTest(new TestSuite(TestNumberData.class));
		addTest(new TestSuite(TestRefListListData.class));
		
		//objecthelpers package
		addTest(new TestSuite(TestMapList.class));
		addTest(new TestSuite(TestORef.class));
		addTest(new TestSuite(TestObjectDeepCopier.class));
		addTest(new TestSuite(TestORefSet.class));
		addTest(new TestSuite(TestRelevancyOverride.class));
		addTest(new TestSuite(TestRelevancyOverrideSet.class));
		addTest(new TestSuite(TestStringRefMap.class));
		addTest(new TestSuite(TestThreatStressRatingEnsurer.class));
		addTest(new TestSuite(TestTimePeriodCosts.class));
		addTest(new TestSuite(TestTimePeriodCostsMap.class));
		addTest(new TestSuite(TestDateUnitEffort.class));
		addTest(new TestSuite(TestDateUnitEffortList.class));
		addTest(new TestSuite(TestBaseObjectRollupValues.class));
		addTest(new TestSuite(TestResultsChainCreatorHelper.class));
		addTest(new TestSuite(TestCategorizedQuantity.class));
		addTest(new TestSuite(TestThreatTargetVirtualLinkHelper.class));
		addTest(new TestSuite(TestDashboardStatusMapsCache.class));
		addTest(new TestSuite(TestOldToNewDiagramFactorMap.class));
		
		// objects package
		addTest(new TestSuite(TestStrategy.class));
		addTest(new TestSuite(TestCause.class));
		addTest(new TestSuite(TestDiagramLink.class));
		addTest(new TestSuite(TestFactorLink.class));
		addTest(new TestSuite(TestFactor.class));
		addTest(new TestSuite(TestConceptualModelThreatRatings.class));
		addTest(new TestSuite(TestGoal.class));
		addTest(new TestSuite(TestIdList.class));
		addTest(new TestSuite(TestBaseId.class));
		addTest(new TestSuite(TestCodeList.class));
		addTest(new TestSuite(TestCodeToCodeMap.class));
		addTest(new TestSuite(TestCodeToUserStringMap.class));
		addTest(new TestSuite(TestCodeToChoiceMap.class));
		addTest(new TestSuite(TestCodeToCodeListMap.class));
		addTest(new TestSuite(TestIndicator.class));
		addTest(new TestSuite(TestKeyEcologicalAttribute.class));
		addTest(new TestSuite(TestObjective.class));
		addTest(new TestSuite(TestObjectManager.class));
		addTest(new TestSuite(TestChainWalker.class));
		addTest(new TestSuite(TestDiagramChainObject.class));
		addTest(new TestSuite(TestThreatTargetChainObject.class));
		addTest(new TestSuite(TestProjectMetadata.class));
		addTest(new TestSuite(TestProjectResource.class));
		addTest(new TestSuite(TestTask.class));
		addTest(new TestSuite(TestThreatRatingBundle.class));
		addTest(new TestSuite(TestRatingCriterion.class));
		addTest(new TestSuite(TestValueOption.class));
		addTest(new TestSuite(TestViewData.class));
		addTest(new TestSuite(TestORefList.class));
		addTest(new TestSuite(TestAssignment.class));
		addTest(new TestSuite(TestProjectResource.class));
		addTest(new TestSuite(TestFundingSource.class));
		addTest(new TestSuite(TestAccountingCode.class));
		addTest(new TestSuite(TestPointList.class));
		addTest(new TestSuite(TestBendPointList.class));
		addTest(new TestSuite(TestDiagramContentsObject.class));
		addTest(new TestSuite(TestObjectFindOwnerAndFindReferrer.class));
		addTest(new TestSuite(TestBaseObject.class));
		addTest(new TestSuite(TestConceptualModelDiagram.class));
		addTest(new TestSuite(TestResultsChainDiagram.class));
		addTest(new TestSuite(TestThreatReductionResult.class));
		addTest(new TestSuite(TestIntermediateResult.class));
		addTest(new TestSuite(TestTextBox.class));
		addTest(new TestSuite(TestScopeBox.class));
		addTest(new TestSuite(TestPlanningViewConfiguration.class));
		addTest(new TestSuite(TestDiagramObject.class));
		addTest(new TestSuite(TestWwfProjectData.class));
		addTest(new TestSuite(TestCostAllocationRule.class));
		addTest(new TestSuite(TestMeasurement.class));
		addTest(new TestSuite(TestStress.class));
		addTest(new TestSuite(TestThreatStressRating.class));
		addTest(new TestSuite(TestSubTarget.class));
		addTest(new TestSuite(TestProgressReport.class));
		addTest(new TestSuite(TestRareProjectData.class));
		addTest(new TestSuite(TestWcsProjectData.class));
		addTest(new TestSuite(TestTncProjectData.class));
		addTest(new TestSuite(TestFosProjectData.class));
		addTest(new TestSuite(TestOrganization.class));
		addTest(new TestSuite(TestWcpaProjectData.class));
		addTest(new TestSuite(TestXenodata.class));
		addTest(new TestSuite(TestProgressPercent.class));
		addTest(new TestSuite(TestReportTemplate.class));
		addTest(new TestSuite(TestTaggedObjectSet.class));
		addTest(new TestSuite(TestTableSettings.class));
		addTest(new TestSuite(TestThreatRatingCommentsData.class));
		addTest(new TestSuite(TestExpense.class));
		addTest(new TestSuite(TestHumanWelfareTarget.class));
		addTest(new TestSuite(TestIucnRedlistSpecies.class));
		addTest(new TestSuite(TestOtherNotableSpecies.class));
		addTest(new TestSuite(TestAudience.class));
		addTest(new TestSuite(TestCategoryOne.class));
		addTest(new TestSuite(TestCategoryTwo.class));
		addTest(new TestSuite(TestDashboard.class));
		addTest(new TestSuite(TestXmlUtilities2.class));
		addTest(new TestSuite(TestXslTemplate.class));
			
		// commands package
		addTest(new TestSuite(TestCommands.class));
		addTest(new TestSuite(TestCommandCreateObject.class));
		addTest(new TestSuite(TestCommandSetObjectData.class));
		addTest(new TestSuite(TestCommandDeleteObject.class));
		addTest(new TestSuite(TestCommandSetFactorSize.class));
		addTest(new TestSuite(TestCommandSetThreatRating.class));
		addTest(new TestSuite(TestCommandBeginTransaction.class));
		addTest(new TestSuite(TestCommandEndTransaction.class));
		addTest(new TestSuite(TestCommandExecutedEvent.class));
		
		// ratings package
		addTest(new TestSuite(TestRatingChoice.class));
		addTest(new TestSuite(TestRatingQuestion.class));
		addTest(new TestSuite(TestStrategyRatingSummary.class));
		
		// view.diagram package
		addTest(new TestSuite(org.miradi.views.diagram.TestInsertFactorLinkDoer.class));
		addTest(new TestSuite(org.miradi.views.diagram.TestInsertFactorDoer.class));
		addTest(new TestSuite(org.miradi.views.diagram.TestLayerManager.class));
		addTest(new TestSuite(org.miradi.views.diagram.TestDiagramPaster.class));
		addTest(new TestSuite(TestDiagramAliasPaster.class));
		addTest(new TestSuite(TestDeleteAnnotationDoer.class));
		
		// view.planning package
		addTest(new TestSuite(TestFullTimeEmployeeCalculationsInsideModel.class));
		addTest(new TestSuite(TestPlanningViewMainTableModel.class));
		addTest(new TestSuite(TestTreeRebuilder.class));
		addTest(new TestSuite(TestWorkPlanRowColumnProvider.class));
		
		// view.strategicplan package
		addTest(new TestSuite(TestDeleteActivity.class));
		
		// view.buget
		addTest(new TestSuite(ImportAccountingCodesDoerTest.class));
		
		// view.threatmatrix package
		addTest(new TestSuite(TestTargetSummartyRowTableModel.class));
		
		// view.summary.doers package

		// view.umbrella package
		addTest(new TestSuite(TestUndoRedo.class));
		

		
		// martus-utils
		addTest(new TestSuite(TestSimpleXmlParser.class));
		addTest(new TestSuite(TestMultiCalendar.class));
		
		//xml.export
		addTest(new TestSuite(TestXmpzExporter.class));
		
		//xml.conpro
		addTest(new TestSuite(TestConProCodeMapHelper.class));
		
		//xml.conpro.export
		addTest(new TestSuite(TestConproXmlExporter.class));
		
		//xml.conpro.importer
		addTest(new TestSuite(TestConproXmlImporter.class));
		addTest(new TestSuite(TestXmpzXmlImporter.class));		
	}

	@Override
	public void run(TestResult result)
	{
		Locale originalLocale = Locale.getDefault();
		try
		{
			Locale.setDefault(locale);

			reportAnyTempFiles("Existing temp file: ");
			super.run(result);
			reportAnyTempFiles("Orphaned temp file: ");
		}
		finally 
		{
			Locale.setDefault(originalLocale);
		}
	}
	
	public void reportAnyTempFiles(String message)
	{
		File systemTempDirectory = getSystemTempDirectory();
		
		String[] allTempFileNames = systemTempDirectory.list();
		for(int i = 0; i < allTempFileNames.length; ++i)
		{
			String fileName = allTempFileNames[i];
			if(fileName.startsWith("$$$"))
				System.out.println("WARNING: " + message + fileName);
		}
	}

	private File getSystemTempDirectory()
	{
		File merelyToFindTempDirectory = createTempFileToLocateTempDirectory();
		File systemTempDirectory = merelyToFindTempDirectory.getParentFile();
		merelyToFindTempDirectory.delete();
		return systemTempDirectory;
	}

	private File createTempFileToLocateTempDirectory()
	{
		try
		{
			return File.createTempFile("$$$MainTests", null);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Unable to create temp file!");
		}
	}
	
	private Locale locale;
}