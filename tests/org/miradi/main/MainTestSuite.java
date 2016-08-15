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

package org.miradi.main;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.martus.util.TestMultiCalendar;
import org.martus.util.xml.TestSimpleXmlParser;
import org.miradi.commands.*;
import org.miradi.diagram.*;
import org.miradi.diagram.arranger.TestMeglerArranger;
import org.miradi.diagram.cells.TestDiagramFactor;
import org.miradi.diagram.cells.TestLinkCell;
import org.miradi.forms.*;
import org.miradi.ids.TestBaseId;
import org.miradi.ids.TestIdList;
import org.miradi.legacyprojects.TestDataUpgrader;
import org.miradi.legacyprojects.TestDataUpgraderForMiradi3;
import org.miradi.migrations.*;
import org.miradi.objectdata.*;
import org.miradi.objecthelpers.*;
import org.miradi.objects.*;
import org.miradi.project.*;
import org.miradi.questions.TestChoiceItem;
import org.miradi.questions.TestChoiceQuestion;
import org.miradi.questions.TestCountriesSorter;
import org.miradi.ratings.TestRatingChoice;
import org.miradi.ratings.TestRatingQuestion;
import org.miradi.ratings.TestStrategyRatingSummary;
import org.miradi.utils.*;
import org.miradi.views.budget.TestImportAccountingCodesDoer;
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
import org.miradi.xml.TestXmpz2ForwardMigration;
import org.miradi.xml.TestXmpz2SchemaCreator;
import org.miradi.xml.TestXmpz2XmlExporter;
import org.miradi.xml.TestXmpz2XmlImporter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

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
		addTest(new TestSuite(TestProjectTotalCalculatorStrategy.class));
		addTest(new TestSuite(TestCommandExecutor.class));
		addTest(new TestSuite(TestProjectSaver.class));
		addTest(new TestSuite(TestMpzToMpfConverter.class));
		addTest(new TestSuite(TestMpfToMpzConverter.class));
		
		//questions package
		addTest(new TestSuite(TestChoiceItem.class));
		addTest(new TestSuite(TestChoiceQuestion.class));
		addTest(new TestSuite(TestCountriesSorter.class));
		
		// utils package
		addTest(new TestSuite(TestEnhancedJsonObject.class));
		addTest(new TestSuite(TestLogging.class));
		addTest(new TestSuite(TestTranslations.class));
		addTest(new TestSuite(TestDelimitedFileLoader.class));
		addTest(new TestSuite(TestTaxonomyFileLoader.class));
		addTest(new TestSuite(TestDateRange.class));
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
		addTest(new TestSuite(TestZipUtilities.class));
		addTest(new TestSuite(TestFileUtilities.class));
		addTest(new TestSuite(TestBaseObjectDeepCopier.class));
		addTest(new TestSuite(TestLanguagePackFileFilter.class));
		
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
		addTest(new TestSuite(TestIntegerData.class));
		addTest(new TestSuite(TestNumberData.class));
		addTest(new TestSuite(TestRefListListData.class));
		
		//objecthelpers package
		addTest(new TestSuite(TestMapList.class));
		addTest(new TestSuite(TestORef.class));
		addTest(new TestSuite(TestBaseObjectDeepCopierWithRelatedObjectsToJson.class));
		addTest(new TestSuite(TestORefSet.class));
		addTest(new TestSuite(TestRelevancyOverride.class));
		addTest(new TestSuite(TestRelevancyOverrideSet.class));
		addTest(new TestSuite(TestStringRefMap.class));
		addTest(new TestSuite(TestThreatStressRatingEnsurer.class));
		addTest(new TestSuite(TestAssignedLeaderEnsurer.class));
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
		addTest(new TestSuite(TestTarget.class));
		addTest(new TestSuite(TestIucnRedlistSpecies.class));
		addTest(new TestSuite(TestOtherNotableSpecies.class));
		addTest(new TestSuite(TestAudience.class));
		addTest(new TestSuite(TestCategoryOne.class));
		addTest(new TestSuite(TestCategoryTwo.class));
		addTest(new TestSuite(TestDashboard.class));
		addTest(new TestSuite(TestXmlUtilities2.class));
		addTest(new TestSuite(TestXslTemplate.class));
		addTest(new TestSuite(TestOwnedBaseObjects.class));
		addTest(new TestSuite(TestMiradiShareProjectData.class));
		addTest(new TestSuite(TestMiradiShareTaxonomy.class));
		addTest(new TestSuite(TestMiradiShareTaxonomyAssociation.class));
		addTest(new TestSuite(TestFutureStatus.class));
		addTest(new TestSuite(TestBiDirectionalHashMap.class));
		addTest(new TestSuite(TestBiophysicalResult.class));
		addTest(new TestSuite(TestBiophysicalFactor.class));
		addTest(new TestSuite(TestTimeframe.class));
		addTest(new TestSuite(TestMiradiShareAccountingClassificationAssociation.class));

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
		
		// view.budget
		addTest(new TestSuite(TestImportAccountingCodesDoer.class));
		
		// view.threatmatrix package
		addTest(new TestSuite(TestTargetSummartyRowTableModel.class));
		
		// view.summary.doers package

		// view.umbrella package
		addTest(new TestSuite(TestUndoRedo.class));

		// martus-utils
		addTest(new TestSuite(TestSimpleXmlParser.class));
		addTest(new TestSuite(TestMultiCalendar.class));
		
		//xml.export
		addTest(new TestSuite(TestXmpz2XmlExporter.class));

		//xml.importer
		addTest(new TestSuite(TestXmpz2XmlImporter.class));
		addTest(new TestSuite(TestXmpz2ForwardMigration.class));
		
		addTest(new TestSuite(TestXmpz2SchemaCreator.class));

		//mpf migrations
		addTest(new TestSuite(TestMigrationManager.class));
		addTest(new TestSuite(TestMigrationResult.class));
		addTest(new TestSuite(TestMigrationTo4.class));
		addTest(new TestSuite(TestMigrationTo5.class));
		addTest(new TestSuite(TestMigrationTo6.class));
		addTest(new TestSuite(TestMigrationTo8.class));
		addTest(new TestSuite(TestMigrationTo9.class));
		addTest(new TestSuite(TestMigrationTo10.class));
		addTest(new TestSuite(TestMigrationTo11.class));
		addTest(new TestSuite(TestMigrationTo12.class));
		addTest(new TestSuite(TestMigrationTo13.class));
		addTest(new TestSuite(TestMigrationTo14.class));
		addTest(new TestSuite(TestMigrationTo15.class));
		addTest(new TestSuite(TestMigrationTo16.class));
		addTest(new TestSuite(TestMigrationTo17.class));
		addTest(new TestSuite(TestMigrationTo18.class));
		addTest(new TestSuite(TestMigrationTo19.class));
		addTest(new TestSuite(TestMigrationTo20.class));
		addTest(new TestSuite(TestMigrationTo21.class));
		addTest(new TestSuite(TestMigrationTo22.class));
		addTest(new TestSuite(TestMigrationTo23.class));
		addTest(new TestSuite(TestMigrationTo24.class));
		addTest(new TestSuite(TestMigrationTo25.class));
		addTest(new TestSuite(TestMigrationTo26.class));
		addTest(new TestSuite(TestMigrationTo27.class));
		addTest(new TestSuite(TestMigrationTo28.class));
		addTest(new TestSuite(TestMigrationTo29.class));
		addTest(new TestSuite(TestMigrationTo30.class));
		addTest(new TestSuite(TestMigrationTo31.class));
		addTest(new TestSuite(TestMigrationTo32.class));
		addTest(new TestSuite(TestMigrationTo33.class));
		addTest(new TestSuite(TestMigrationTo34.class));
		addTest(new TestSuite(TestMigrationTo35.class));
		addTest(new TestSuite(TestMigrationTo36.class));
		addTest(new TestSuite(TestMigrationTo37.class));
		addTest(new TestSuite(TestMigrationTo38.class));
		addTest(new TestSuite(TestMigrationTo39.class));

		addTest(new TestSuite(TestReverseMigration.class));
		addTest(new TestSuite(TestVersionRange.class));
		addTest(new TestSuite(TestRawProject.class));
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